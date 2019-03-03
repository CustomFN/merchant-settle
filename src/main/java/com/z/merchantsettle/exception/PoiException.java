package com.z.merchantsettle.exception;

public class PoiException extends RuntimeException {

    private Integer code;
    private String msg;

    public PoiException() {}

    public PoiException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
