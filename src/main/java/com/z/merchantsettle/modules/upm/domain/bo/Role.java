package com.z.merchantsettle.modules.upm.domain.bo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class Role {

    private Integer id;

    @NotBlank(message = "角色id不能为空")
    private String roleId;

    @NotBlank(message = "角色名不能为空")
    private String roleName;

    private String description;

    @Min(value = 1, message = "状态错误")
    @Max(value = 2, message = "状态错误")
    private Integer status;

    private Long ctime;

    private Integer valid;

    private List<String> resourceIdList = new ArrayList<>();

}
