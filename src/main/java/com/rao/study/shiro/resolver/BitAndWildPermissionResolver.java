package com.rao.study.shiro.resolver;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

//BitAndWildPermissionResolver 实现了 PermissionResolver 接口，并根据权限字符串是否以 “+” 开头来解析权限字符串为 BitPermission 或 WildcardPermission。
public class BitAndWildPermissionResolver implements PermissionResolver {

    public Permission resolvePermission(String permissionString) {//根据制定的权限规则来解析字符串中的权限
        if(permissionString.startsWith("+")) {
            return new BitPermission(permissionString);
        }
        return new WildcardPermission(permissionString);
    }
}