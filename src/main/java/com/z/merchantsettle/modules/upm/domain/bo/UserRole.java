package com.z.merchantsettle.modules.upm.domain.bo;

import lombok.Data;

import java.util.List;

@Data
public class UserRole {

    private String userId;
    private List<String> roleIdList;

}
