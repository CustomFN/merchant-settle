package com.z.merchantsettle.modules.poi.constants;

public enum WmDeliveryTypeEnum {
    PLATEFORM_DELIVERY(1, "平台配送"),
    BUSINESS_DELIVERY(2, "商家自配");


    private int code;
    private String desc;

    WmDeliveryTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

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

    public static String getByCode(int code) {
        for (WmDeliveryTypeEnum deliveryTypeEnum : values()) {
            if (code == deliveryTypeEnum.getCode()) {
                return deliveryTypeEnum.getDesc();
            }
        }
        throw new RuntimeException("不存在此状态");
    }
}
