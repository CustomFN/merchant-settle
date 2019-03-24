package com.z.merchantsettle.modules.customer.constants;

public class CustomerConstant {

    public static final int NO_PERMISSION = 403;

    public static final int CUSTOMER_PARAM_ERROR = 3001;
    public static final int CUSTOMER_OP_ERROR = 3002;
    public static final int CUSTOMER_VALID_ERROR = 3003;


    public static final int EFFECTIVE = 1;
    public static final int NOT_EFFECTIVE = 0;


    public static final String SIGNER_LABEL_A = "A";
    public static final String SIGNER_LABEL_B = "B";

    public enum CustomerStatus {

        TO_AUDIT(0, "待提审"),
        AUDITING(1, "审核中"),
        AUDIT_REJECT(2, "审核驳回"),
        AUDIT_PASS(3, "审核通过");

        private Integer code;
        private String desc;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        CustomerStatus(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getByCode(int code) {
            for (CustomerStatus customerStatus : values()) {
                if (code == customerStatus.getCode()) {
                    return customerStatus.getDesc();
                }
            }
            throw new RuntimeException("不存在此状态");
        }
    }

}
