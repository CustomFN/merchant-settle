package com.z.merchantsettle.modules.audit.constants;

public class AuditConstant {

    public static final int AUDIT_PARAM_ERROR = 2001;

    public static final int AUDIT_OP_ERROR = 2002;

    public static final int AUDIT_STATUS_ERROR = 2003;


    public static class AuditStatus {

        public static final int AUDITING = 201;

        public static final int AUDIT_REJECT = 202;

        public static final int AUDIT_PASS = 203;
    }

    public static class AuditApplicationType {

        public static final int AUDIT_NEW = 21;

        public static final int AUDIT_UPDATE = 22;
    }

    public static class AuditType {

        public static final int CUSTOMER = 1;

        public static final int CUSTOMER_KP = 2;

        public static final int CUSTOMER_CONTRACT = 3;

        public static final int CUSTOMER_SETTLE = 4;

        public static final int POI_BASE_INFO = 5;

        public static final int POI_QUA_INFO = 6;

        public static final int POI_DELIVERY_INFO = 7;

        public static final int POI_BUSINESS_INFO = 8;
    }
}
