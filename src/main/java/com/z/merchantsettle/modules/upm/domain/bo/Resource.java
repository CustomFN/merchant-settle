package com.z.merchantsettle.modules.upm.domain.bo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Resource {

    private Integer id;

    @NotBlank(message = "资源id不能为空")
    private String resourceId;

    @NotBlank(message = "资源路径不能为空")
    private String resourcePath;

    private String description;

    @NotBlank(message = "资源名称不能为空")
    private String resourceName;

    @NotBlank(message = "父级资源id不能为空")
    private String parentId;

    @NotNull(message = "资源排序值不能为空")
    @Min(1)
    private Double sortOrder;

    private String icon;

    @NotNull(message = "层级不能为空")
    @Min(0)
    private Integer level;

    private Integer status;

    private Long ctime;

    private Integer valid;

    List<Resource> childResource;
}
