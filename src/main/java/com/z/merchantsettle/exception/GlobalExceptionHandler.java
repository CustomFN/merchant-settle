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
        LOGGER.error("授权异常", e);
        return ReturnResult.fail("授权异常");
    }

    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException e) {
        LOGGER.error("运行时异常", e);
        return ReturnResult.fail("服务器错误,请联系管理员");
    }

    @ExceptionHandler(UpmException.class)
    public Object handleUpmException(UpmException e) {
        LOGGER.error("权限异常", e);
        return ReturnResult.fail(e.getMsg());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        LOGGER.error("请求异常", e);
        return ReturnResult.fail("请求方法错误");
    }
}
