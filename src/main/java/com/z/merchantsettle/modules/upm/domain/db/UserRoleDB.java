package com.z.merchantsettle.modules.upm.domain.db;

import lombok.Data;

@Data
public class UserRoleDB {

    private Integer id;
    private String userId;
    private String roleId;
    private Long ctime;
    private Integer valid;
}
