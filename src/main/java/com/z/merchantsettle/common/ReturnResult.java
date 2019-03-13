package com.z.merchantsettle.common;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class ReturnResult {

    private Integer code;
    private String msg;
    private Object data;

    public ReturnResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private static final Integer OK_CODE = 200;
    private static final String OK_MSG = "success";

    private static final Integer FAIL_CODE = 500;
    private static final String FAIL_MSG = "fail";


    public static Object success() {
        return JSON.toJSON(new ReturnResult(OK_CODE, OK_MSG, null));
    }

    public static Object success(Object data) {
        return JSON.toJSON(new ReturnResult(OK_CODE, OK_MSG, data));
    }

    public static Object success(String msg, Object data) {
        return JSON.toJSON(new ReturnResult(OK_CODE, msg, data));
    }



    public static Object fail() {
        return JSON.toJSON(new ReturnResult(FAIL_CODE, FAIL_MSG, null));
    }

    public static Object fail(Integer code) {
        return JSON.toJSON(new ReturnResult(code, FAIL_MSG, null));
    }

    public static Object fail(String msg) {
        return JSON.toJSON(new ReturnResult(FAIL_CODE, msg, null));
    }

    public static Object fail(Integer code, String msg) {
        return JSON.toJSON(new ReturnResult(code, msg, null));
    }

    public static Object fail(String msg, Object data) {
        return JSON.toJSON(new ReturnResult(FAIL_CODE, msg, data));
    }

    public static Object fail(Integer code, String msg, Object data) {
        return JSON.toJSON(new ReturnResult(code, msg, data));
    }
}
