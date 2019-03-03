package com.z.merchantsettle.modules.upm.domain.db;

import lombok.Data;

@Data
public class RoleDB {

    private Integer id;
    private String roleId;
    private String roleName;
    private String description;
    private Integer status;
    private Long ctime;
    private Integer valid;
}
