package com.z.merchantsettle.modules.customer.constants;

public enum  CustomerTypeEnum {

    CUSTOMER_TYPE_ENTERPRISE(1, "营业执照"),
    CUSTOMER_TYPE_PERSON(2, "个人证件");

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

    CustomerTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getByCode(int code) {
        for (CustomerTypeEnum type : values()) {
            if (code == type.getCode()) {
                return type.getDesc();
            }
        }
        throw new RuntimeException("不存在此状态");
    }
}
