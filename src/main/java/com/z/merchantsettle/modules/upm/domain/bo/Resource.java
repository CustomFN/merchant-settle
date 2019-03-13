package com.z.merchantsettle.modules.upm.domain.bo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Resource {

    private Integer id;

    private String resourceId;

    private String resourcePath;

    private String description;

    private String resourceName;

    private String parentId;

    @NotNull(message = "资源排序值不能为空")
    @Min(1)
    private Double sortOrder;

    private Integer resourceType;

    @NotNull(message = "层级不能为空")
    @Min(0)
    private Integer level;

    private Integer status;

    private Long ctime;

    private Integer valid;

    List<Resource> childResource;
}
