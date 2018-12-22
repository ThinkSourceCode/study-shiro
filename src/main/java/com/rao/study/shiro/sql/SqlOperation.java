package com.rao.study.shiro.sql;

import com.rao.study.shiro.domain.Permission;
import com.rao.study.shiro.domain.Role;
import com.rao.study.shiro.domain.User;
import com.rao.study.shiro.domain.UserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqlOperation {
    private static final Logger log = LoggerFactory.getLogger(SqlOperation.class);
    private static final String URL = "jdbc:mysql://localhost:3306/my_shiro";
    private static String USERNAME = "root";
    private static String PASSWORD = "123456";

    private static Connection connection;

    static {
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }

    public  static User login(String username, String password){

        User user = null;
        try {
            PreparedStatement pstmt = connection.prepareStatement("select * from t_user where username = ? and password = ?");
            pstmt.setString(1,username);
            pstmt.setString(2,password);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                user = new User();
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return user;
    }

    public static User loginByToken(String token){
        User user = null;
        try {
            PreparedStatement pstmt = connection.prepareStatement("select u.id,u.username from t_user u left join t_user_token ut on u.id = ut.user_id where token = ?");
            pstmt.setString(1,token);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                user = new User();
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(2));
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return user;
    }

    public static UserToken getUserTokenByToken(String token){
        UserToken userToken = null;
        try {
            PreparedStatement pstmt = connection.prepareStatement("select id,user_id,token,DATE_FORMAT(expired_date,'%Y-%m-%d %H:%i:%s') expired_date from t_user_token where token = ?");
            pstmt.setString(1,token);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                userToken = new UserToken();
                userToken.setId(rs.getInt(1));
                userToken.setUserId(rs.getInt(2));
                userToken.setToken(rs.getString(3));
                userToken.setExpiredDate(LocalDateTime.parse(rs.getString(4), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return userToken;
    }

    public static void saveUserToken(User user,String token){
        try {
            PreparedStatement pstmt = connection.prepareStatement("select count(0) from t_user_token where user_id = ?");
            pstmt.setInt(1,user.getId());
            ResultSet rs = pstmt.executeQuery();
            int count = 0;
            while(rs.next()){
                count = rs.getInt(1);
            }
            if(count!=0){
                pstmt = connection.prepareStatement("update t_user_token set token = ? ,expired_date = ? ,update_date = ? where user_id = ?");
                pstmt.setString(1,token);
                LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);//1天后过期
                pstmt.setString(2,localDateTime.toString());
                pstmt.setString(3,LocalDateTime.now().toString());
                pstmt.setInt(4,user.getId());
                pstmt.executeUpdate();
            }else{
                pstmt = connection.prepareStatement("insert into t_user_token(user_id,token,expired_date,create_date,update_date) values(?,?,?,?,?)");
                pstmt.setInt(1,user.getId());
                pstmt.setString(2,token);
                LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);//1天后过期
                pstmt.setString(3,localDateTime.toString());
                pstmt.setString(4,LocalDateTime.now().toString());
                pstmt.setString(5,LocalDateTime.now().toString());
                pstmt.executeUpdate();
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }

    public static List<Role> getRoles(Integer userId){
        List<Role> roles = new ArrayList<Role>();
        try {
            PreparedStatement pstmt = connection.prepareStatement("select r.id,rolename from t_role r left join t_user_role ur on r.id = ur.role_id left join t_user u on ur.user_id = u.id where u.id = ?");
            pstmt.setInt(1,userId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Role role = new Role();
                role.setId(rs.getInt(1));
                role.setRolename(rs.getString(2));
                roles.add(role);
            }
        }catch (Exception e){

        }
        return roles;
    }

    public  static  List<Permission> getPermissions(int roleId){
        List<Permission> permissions = new ArrayList<Permission>();
        try {
            PreparedStatement pstmt = connection.prepareStatement("select p.id,p.permission from t_permission p left join t_role_permission rp on p.id = rp.permission_id left join t_role r on rp.role_id = r.id where r.id = ?");
            pstmt.setInt(1,roleId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Permission permission = new Permission();
                permission.setId(rs.getInt(1));
                permission.setPermission(rs.getString(2));
                permissions.add(permission);
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return permissions;
    }

}
