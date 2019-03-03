package com.z.merchantsettle.modules.poi.constants;

public class PoiConstant {

    public static final int POI_PARAM_ERROR = 4001;

    public static final int POI_OP_ERROR = 4002;

    public static final int EFFECTIVE = 1;

    public static class PoiStatus {

        public static final int TO_CLAIM = 401;

        public static final int CLAIMED = 402;

        public static final int IN_COOPERATION = 403;

        public static final int TO_ONLINE = 404;
    }

    public static class PoiModuleName {

        public static final String POI_BASE_INFO = "门店基本信息";

        public static final String POI_QUA = "门店资质信息";

        public static final String POI_DELIVERY_INFO = "门店配送信息";

        public static final String POI_BUSINESS_INFO = "门店营业信息";
    }

    public static class PoiModuleStatus {

        public static final int STATUS_ERROR = 410;

        public static final int TO_INPUT = 411;

        public static final int AUDING = 412;

        public static final int AUDIT_REJECT = 413;

        public static final int EFFECT = 414;
    }

    public static class OrderMealType {

        public static final int ALL = 0;

        public static final int MON = 1;

        public static final int TUE = 2;

        public static final int WED = 3;

        public static final int THU = 4;

        public static final int FRI = 5;

        public static final int SAT = 6;

        public static final int SUM = 7;
    }
}
