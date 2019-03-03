package com.z.merchantsettle.modules.poi.domain;

import lombok.Data;

@Data
public class WmPoiOpLog {

    private Integer id;
    private Integer wmPoiId;
    private String module;
    private String content;
    private String opUser;
    private Long ctime;

}
