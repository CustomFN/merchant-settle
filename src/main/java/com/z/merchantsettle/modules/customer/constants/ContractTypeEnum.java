package com.z.merchantsettle.modules.customer.constants;

public enum ContractTypeEnum {

    PLATEFORM_CONTRACT(1, "商家与平台合同"),
    AGENT_CONTRACT(2, "商家与代理商合同");
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

    ContractTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getByCode(int code) {
        for (ContractTypeEnum type : values()) {
            if (code == type.getCode()) {
                return type.getDesc();
            }
        }
        throw new RuntimeException("不存在此状态");
    }
}
