package com.z.merchantsettle.modules.upm.service.impl;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.modules.upm.domain.UserSearchParam;
import com.z.merchantsettle.modules.upm.domain.bo.Role;
import com.z.merchantsettle.modules.upm.domain.bo.UserRole;
import com.z.merchantsettle.modules.upm.domain.db.UserDB;
import com.z.merchantsettle.modules.upm.service.RoleService;
import com.z.merchantsettle.modules.upm.service.UserRoleService;
import com.z.merchantsettle.modules.upm.service.UserService;
import com.z.merchantsettle.modules.upm.dao.UserMapper;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.utils.transfer.upm.UserTransferUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Override
    public PageData<User> getUserList(UserSearchParam param, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserDB> userDBList = userMapper.selectList(param);
        PageInfo<UserDB> pageInfo = new PageInfo<>(userDBList);

        List<User> userList = UserTransferUtil.transUserList2BoList(userDBList);
        List<String> userIdList = Lists.transform(userList, new Function<User, String>() {
            @Override
            public String apply(User input) {
                return input.getUserId();
            }
        });

        if (CollectionUtils.isNotEmpty(userIdList)) {
            Map<String, List<String>> userRoleMap = userRoleService.getByUserIdList(userIdList);

            if (!userRoleMap.isEmpty()) {
                List<String> roleIdList = Lists.newArrayList();
                Collection<List<String>> values = userRoleMap.values();
                for (Collection collection : values) {
                    roleIdList.addAll(collection);
                }

                if (CollectionUtils.isNotEmpty(roleIdList)) {
                    List<Role> roleList = roleService.getRolesByIdList(roleIdList);
                    Map<String, Role> roleMap = Maps.uniqueIndex(roleList, new Function<Role, String>() {
                        @Override
                        public String apply(Role input) {
                            return input.getRoleId();
                        }
                    });
                    for (User user : userList) {
                        List<String> idList = userRoleMap.get(user.getUserId());
                        if (CollectionUtils.isNotEmpty(idList)) {
                            List<String> roleNameList = Lists.newArrayList();
                            for (String id : idList) {
                                Role role = roleMap.get(id);
                                if (role != null) {
                                    roleNameList.add(role.getRoleName());
                                }
                            }
                            user.setRoleIdList(idList);
                            user.setRoleNameList(roleNameList);
                        }
                    }
                }
            }

        }
        return new PageData.Builder<User>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalSize((int) pageInfo.getTotal())
                .totalPage(pageInfo.getPages())
                .data(userList)
                .build();
    }


    @Override
    public void saveOrUpdate(User user) {
        if (StringUtils.isBlank(user.getUserId())) {
            createUserByUserName(user);
        } else {
            UserDB userDBInDB = userMapper.selectByUserId(user.getUserId());
            if (userDBInDB == null) {
                createUserByUserName(user);
            } else {
                UserDB userDB = UserTransferUtil.transUserBo2DB(user);
                userMapper.updateByUserIdSelective(userDB);
            }
        }
    }

    private void createUserByUserName(User user) {
        String userNameSpell = user.getUserNameSpell();
        UserDB lastUserDB = userMapper.selectLastByUserNameSpell(userNameSpell);
        if (lastUserDB == null) {
            user.setUserId(userNameSpell);
        } else {
            String userId = lastUserDB.getUserId();
            String userNum = userId.substring(userNameSpell.length());
            if (StringUtils.isBlank(userNum)) {
                userId = userNameSpell + "02";
            } else {
                int newUserNum = Integer.parseInt(userNum) + 1;
                userId = userNameSpell + String.format("%02d", newUserNum);
            }
            user.setUserId(userId);
        }
        UserDB userDB = UserTransferUtil.transUserBo2DB(user);
        LOGGER.info("saveOrUpdate userDB = {}", JSON.toJSONString(userDB));
        userMapper.insertSelective(userDB);
    }


    @Override
    public void deleteByUserId(String userId) {
        userMapper.deleteByUserId(userId);
    }

    @Override
    public User getUserByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }

        UserDB userDB = userMapper.selectByUserId(userId);
        return UserTransferUtil.transUserDB2Bo(userDB);
    }

    @Override
    public Boolean hasRole(String userId, String roleName) {
        if (StringUtils.isBlank(roleName)) {
            return false;
        }

        Role role = roleService.getByRoleName(roleName);
        if (role == null) {
            return false;
        }

        UserRole userRole = userRoleService.getByUserIdAndRoleId(userId, role.getRoleId());
        if (userRole == null) {
            return false;
        }
        return true;
    }


}
