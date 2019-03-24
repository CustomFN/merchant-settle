package com.z.merchantsettle.modules.poi.constants;

public class PoiConstant {

    public static final int POI_PARAM_ERROR = 4001;

    public static final int POI_OP_ERROR = 4002;

    public static final int POI_VALID_ERROR = 4003;

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

    public enum PoiModuleStatus {

        STATUS_ERROR(410, "状态错误"),
        TO_INPUT(411, "待输入"),
        AUDING(412, "审核中"),
        AUDIT_REJECT(413, "审核驳回"),
        EFFECT(414, "审核通过");

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

        PoiModuleStatus(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getByCode(int code) {
            for (PoiModuleStatus poiModuleStatus : values()) {
                if (code == poiModuleStatus.getCode()) {
                    return poiModuleStatus.getDesc();
                }
            }
            throw new RuntimeException("不存在此状态");
        }
    }

    public enum PoiCoopState {

        COOPERATING(1, "上单中"),
        OFFLINE(2, "下线中"),
        ONLINE(412, "上线中");

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

        PoiCoopState(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getByCode(int code) {
            for (PoiCoopState poiCoopState : values()) {
                if (code == poiCoopState.getCode()) {
                    return poiCoopState.getDesc();
                }
            }
            throw new RuntimeException("不存在此状态");
        }
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
