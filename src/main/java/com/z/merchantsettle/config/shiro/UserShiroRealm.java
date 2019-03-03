package com.z.merchantsettle.config.shiro;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.modules.upm.service.RoleResourceService;
import com.z.merchantsettle.modules.upm.service.SystemService;
import com.z.merchantsettle.modules.upm.service.UserRoleService;
import com.z.merchantsettle.modules.upm.service.UserService;
import com.z.merchantsettle.utils.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserShiroRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserShiroRealm.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private SystemService systemService;



    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = ShiroUtils.getSysUser();

        List<String> roleIdList = Lists.newArrayList();
        List<String> resourceIdList = Lists.newArrayList();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        if (user.isAdmin()) {
            simpleAuthorizationInfo.addRole("admin");
            simpleAuthorizationInfo.addStringPermission("*");
        } else {
            roleIdList = userRoleService.getRoleIdByUserId(user.getUserId());
            resourceIdList = roleResourceService.getResourceIdByRoleIdList(roleIdList);

            simpleAuthorizationInfo.setRoles(Sets.newHashSet(roleIdList));
            simpleAuthorizationInfo.setStringPermissions(Sets.newHashSet(resourceIdList));
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userId = token.getUsername();
        String password = new String(token.getPassword());

        User user = null;
        try {
            user = systemService.login(userId, password);
        } catch (Exception e) {
            LOGGER.warn("登录失败", e);
            throw new AuthenticationException(e);
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, password, getName());
        return simpleAuthenticationInfo;
    }

    /**
     * 清理缓存权限
     */
    public void clearCachedAuthorizationInfo() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
