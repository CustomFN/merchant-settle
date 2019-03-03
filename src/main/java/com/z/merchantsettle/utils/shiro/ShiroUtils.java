package com.z.merchantsettle.utils.shiro;


import com.z.merchantsettle.config.shiro.UserShiroRealm;
import com.z.merchantsettle.exception.UpmException;
import com.z.merchantsettle.modules.upm.constants.SystemConstant;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;


public class ShiroUtils {
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static void logout() {
        getSubject().logout();
    }

    public static User getSysUser() throws UpmException {

        Object obj = getSubject().getPrincipal();
        if (obj == null) {
            throw new UpmException(SystemConstant.ErrorCode.DATA_NOT_EXIST_ERROR, "获取当前用户异常");
        }

        User user = new User();
        BeanUtils.copyProperties(obj, user);
        return user;
    }

    public static void setSysUser(User user) {
        Subject subject = getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realmName);
        // 重新加载Principal
        subject.runAs(newPrincipalCollection);
    }

    public static void clearCachedAuthorizationInfo() {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserShiroRealm realm = (UserShiroRealm) rsm.getRealms().iterator().next();
        realm.clearCachedAuthorizationInfo();
    }

    public static String getUserId() {
        return getSysUser().getUserId();
    }

    public static String getIp() {
        return getSubject().getSession().getHost();
    }

    public static String getSessionId() {
        return String.valueOf(getSubject().getSession().getId());
    }
}
