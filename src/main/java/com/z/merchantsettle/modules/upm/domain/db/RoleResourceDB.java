package com.z.merchantsettle.modules.upm.domain.db;

import lombok.Data;

@Data
public class RoleResourceDB {
    private Integer id;
    private String roleId;
    private String resourceId;
    private Long ctime;
    private Integer valid;
}
