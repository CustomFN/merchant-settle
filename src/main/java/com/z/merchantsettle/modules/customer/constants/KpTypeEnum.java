package com.z.merchantsettle.modules.customer.constants;

public enum KpTypeEnum {

    SIGNER(1, "签约人");
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

    KpTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getByCode(int code) {
        for (KpTypeEnum type : values()) {
            if (code == type.getCode()) {
                return type.getDesc();
            }
        }
        throw new RuntimeException("不存在此状态");
    }
}
