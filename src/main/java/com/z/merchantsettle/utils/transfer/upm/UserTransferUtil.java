package com.z.merchantsettle.utils.transfer.upm;

import com.google.common.collect.Lists;

import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.modules.upm.domain.db.UserDB;
import com.z.merchantsettle.utils.TransferUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class UserTransferUtil {

    public static User transUserDB2Bo(UserDB userDB) {
        if (userDB == null) {
            return null;
        }

        User user = new User();
        TransferUtil.transferAll(userDB, user);
        return user;
    }

    public static List<User> transUserList2BoList(List<UserDB> userDBList) {
        if (CollectionUtils.isEmpty(userDBList)) {
            return Lists.newArrayList();
        }

        List<User> userList = Lists.newArrayList();
        for (UserDB userDB : userDBList) {
            User user = transUserDB2Bo(userDB);
            userList.add(user);
        }
        return userList;
    }

    public static UserDB transUserBo2DB(User user) {
        if (user == null) {
            return null;
        }

        UserDB userDB = new UserDB();
        TransferUtil.transferAll(user, userDB);
        return userDB;
    }

    public static List<UserDB> transUserBoList2DBList(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return Lists.newArrayList();
        }

        List<UserDB> userDBList = Lists.newArrayList();
        for (User user : userList) {
            UserDB userDB = transUserBo2DB(user);
            userDBList.add(userDB);
        }
        return userDBList;
    }
}
