package com.z.merchantsettle.exception;


import com.z.merchantsettle.common.ReturnResult;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthorizationException.class)
    public Object handleAuthorizationExceptionException(AuthorizationException e) {
        return ReturnResult.fail(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException e) {
        return ReturnResult.fail("运行时异常:" + e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        LOGGER.error("服务器错误", e);
        return ReturnResult.fail("服务器错误，请联系管理员");
    }

    @ExceptionHandler(UpmException.class)
    public Object handleUpmException(UpmException e) {
        return ReturnResult.fail(e.getMsg());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ReturnResult.fail("请求方法错误");
    }
}
