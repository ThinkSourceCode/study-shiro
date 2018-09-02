package com.rao.study.shiro.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlOperation {
    private static final Logger log = LoggerFactory.getLogger(SqlOperation.class);
    private static String url = "jdbc:mysql://localhost:3306/my_shiro";
    private static String username = "root";
    private static String password = "123456";

    private static Connection connection;

    static {
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            connection = DriverManager.getConnection(url, username, password);
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }

    public  static User login(String username,String password){

        User user = null;
        try {
            PreparedStatement pstmt = connection.prepareStatement("select * from t_user where username = ? and password = ?");
            pstmt.setString(1,username);
            pstmt.setString(2,password);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                user = new User();
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return user;
    }

    public static List<Role> getRoles(String username){
        List<Role> roles = new ArrayList<Role>();
        try {
            PreparedStatement pstmt = connection.prepareStatement("select r.id,rolename from t_role r left join t_user_role ur on r.id = ur.role_id left join t_user u on ur.user_id = u.id where username = ?");
            pstmt.setString(1,username);
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
