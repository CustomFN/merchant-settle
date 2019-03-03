package com.z.merchantsettle.modules.upm.domain.bo;

import lombok.Data;

import java.util.List;

@Data
public class RoleResource {

    private String roleId;
    private List<String> resourceIdList;
    private Long ctime;
    private Integer valid;
}
