package com.z.merchantsettle.modules.upm.domain.db;

import lombok.Data;

@Data
public class UserDB {

    private Integer id;

    private String userId;

    private String userName;

    private String userPassword;

    private String avatar;

    private String salt;

    private Long ctime;

    private Long utime;

    private Integer valid;
}
