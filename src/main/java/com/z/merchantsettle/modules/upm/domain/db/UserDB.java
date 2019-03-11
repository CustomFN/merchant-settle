package com.z.merchantsettle.modules.upm.domain.db;

import lombok.Data;

@Data
public class UserDB {

    private Integer id;

    private String userId;

    private String userNameSpell;

    private String userName;

    private String userPassword;

    private Integer status;

    private String salt;

    private Long ctime;

    private Long utime;

    private Integer valid;
}
