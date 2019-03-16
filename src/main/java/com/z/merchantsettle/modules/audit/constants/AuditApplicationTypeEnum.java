package com.z.merchantsettle.modules.audit.constants;

public enum AuditApplicationTypeEnum {

    AUDIT_NEW(21,"新建"),
    AUDIT_UPDATE(22,"修改");

    private int code;
    private String desc;

    AuditApplicationTypeEnum(int code, String desc) {
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
        for (AuditApplicationTypeEnum type : values()) {
            if (code == type.getCode()) {
                return type.getDesc();
            }
        }
        throw new RuntimeException("不存在此状态");
    }
}
