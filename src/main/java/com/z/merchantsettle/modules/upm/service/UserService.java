package com.z.merchantsettle.modules.upm.service;


import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.modules.upm.domain.UserSearchParam;
import com.z.merchantsettle.modules.upm.domain.bo.User;

public interface UserService {

    PageData<User> getUserList(UserSearchParam param, Integer pageNum, Integer pageSize);

    void saveOrUpdate(User user);

    void deleteByUserId(String userId);

    User getUserByUserId(String userId);

    Boolean hasRole(String userId, String roleName);
}
