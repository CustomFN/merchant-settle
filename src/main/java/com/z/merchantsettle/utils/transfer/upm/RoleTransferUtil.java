package com.z.merchantsettle.utils.transfer.upm;

import com.google.common.collect.Lists;

import com.z.merchantsettle.modules.upm.domain.bo.Role;
import com.z.merchantsettle.modules.upm.domain.db.RoleDB;
import com.z.merchantsettle.utils.TransferUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class RoleTransferUtil {

    public static Role transRoleDB2Bo(RoleDB roleDB) {
        if (roleDB == null) {
            return null;
        }

        Role role = new Role();
        TransferUtil.transferAll(roleDB, role);
        return role;
    }

    public static List<Role> transRoleDBList2BoList(List<RoleDB> roleDBList) {
        if (CollectionUtils.isEmpty(roleDBList)) {
            return Lists.newArrayList();
        }

        List<Role> roleList = Lists.newArrayList();
        for (RoleDB roleDB : roleDBList) {
            Role role = transRoleDB2Bo(roleDB);
            roleList.add(role);
        }
        return roleList;
    }

    public static RoleDB transRoleBo2DB(Role role) {
        if (role == null) {
            return null;
        }

        RoleDB roleDB = new RoleDB();
        TransferUtil.transferAll(role, roleDB);
        return roleDB;
    }

    public static List<RoleDB> transRoleBoList2DBList(List<Role> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }

        List<RoleDB> roleDBList = Lists.newArrayList();
        for (Role role : roleList) {
            RoleDB roleDB = transRoleBo2DB(role);
            roleDBList.add(roleDB);
        }
        return roleDBList;
    }
}
