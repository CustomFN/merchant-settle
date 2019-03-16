package com.z.merchantsettle.modules.audit.constants;

public enum AuditTypeEnum {

    CUSTOMER(1,"客户基本信息"),
    CUSTOMER_KP(2,"客户KP信息"),
    CUSTOMER_CONTRACT(3,"客户合同息"),
    CUSTOMER_SETTLE(4,"客户结算信息"),
    POI_BASE_INFO(5,"门店基本信息"),
    POI_QUA_INFO(6,"门店资质信息"),
    POI_DELIVERY_INFO(7,"门店配送信息"),
    POI_BUSINESS_INFO(8,"门店营业信息");

    private final  int code;
    private String desc;

    AuditTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getByCode(int code) {
        for (AuditTypeEnum type : values()) {
            if (code == type.getCode()) {
                return type.getDesc();
            }
        }
        throw new RuntimeException("不存在此状态");
    }
}
