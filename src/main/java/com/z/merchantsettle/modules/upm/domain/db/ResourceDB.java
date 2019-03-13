package com.z.merchantsettle.modules.upm.domain.db;

import lombok.Data;

@Data
public class ResourceDB {

    private Integer id;
    private String resourceId;
    private String resourcePath;
    private String description;
    private String resourceName;
    private String parentId;
    private Double sortOrder;
    private Integer level;
    private Integer resourceType;
    private Integer status;
    private Long ctime;
    private Integer valid;
}
