package com.z.merchantsettle.modules.customer.constants;

public enum SettleCycleEnum {

    THREE_DAYS(1, "3天"),
    SEVEN_DAYS(2, "7天"),
    THIRTY_DAYS(3, "30天"),
    LAST_DAY(4, "每月最后一天");

    private int code;
    private String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    SettleCycleEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getByCode(int code) {
        for (SettleCycleEnum type : values()) {
            if (code == type.getCode()) {
                return type.getDesc();
            }
        }
        throw new RuntimeException("不存在此状态");
    }
}
