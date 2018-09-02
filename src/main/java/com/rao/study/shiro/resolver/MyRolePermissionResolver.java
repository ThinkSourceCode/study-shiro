package com.rao.study.shiro.resolver;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.Arrays;
import java.util.Collection;

//RolePermissionResolver 用于根据角色字符串来解析得到权限集合。
//此处的实现很简单，如果用户拥有 role1，那么就返回一个 “menu:*” 的权限。
public class MyRolePermissionResolver implements RolePermissionResolver {
    public Collection<Permission> resolvePermissionsInRole(String roleString) {//返回对应角色的所有权限
        if("role1".equals(roleString)) {
            return Arrays.asList((Permission)new WildcardPermission("menu:*"));
        }
        return null;
    }
}
