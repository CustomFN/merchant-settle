package com.z.merchantsettle.modules.upm.dao;


import com.z.merchantsettle.modules.upm.domain.UserSearchParam;
import com.z.merchantsettle.modules.upm.domain.db.UserDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    void insertSelective(UserDB userDB);

    void updateByUserIdSelective(UserDB userDB);

    void deleteByUserId(String userId);

    UserDB selectByUserId(String userId);

    List<UserDB> selectList(@Param("userSearchParam") UserSearchParam param);
}
