package com.z.merchantsettle.modules.customer.constants;

public enum  SettleTypeEnum {

    SYSTEM_AUTO_SETTLE(1, "系统自动结算"),
    PERSON_SELF_SETTLE(2, "商家自提");

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

    SettleTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getByCode(int code) {
        for (SettleTypeEnum type : values()) {
            if (code == type.getCode()) {
                return type.getDesc();
            }
        }
        throw new RuntimeException("不存在此状态");
    }
}
