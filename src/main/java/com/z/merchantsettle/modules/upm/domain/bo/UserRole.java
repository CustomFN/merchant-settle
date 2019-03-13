package com.z.merchantsettle.modules.upm.domain.bo;

import lombok.Data;

import java.util.List;

@Data
public class UserRole {

    private Integer id;
    private String userId;
    private List<String> roleIdList;

    private Long ctime;
    private Integer valid;

}
