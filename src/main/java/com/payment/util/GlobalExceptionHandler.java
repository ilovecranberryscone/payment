package com.payment.util;

import java.net.BindException;
import java.util.NoSuchElementException;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.payment.exception.BizException;
import com.payment.vo.ResultResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     *  HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     *  주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResultResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    	final ResultResponse response = ResultResponse.builder().errCode("E01").errMsg(e.getMessage()).build();
        return response;
    }

    /**
     * @ModelAttribut 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    protected ResultResponse handleBindException(BindException e) {
    	final ResultResponse response = ResultResponse.builder().errCode("E02").errMsg(e.getMessage()).build();
        return response;
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResultResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    	final ResultResponse response = ResultResponse.builder().errCode("E03").errMsg(e.getMessage()).build();
        return response;
    }
    
    @ExceptionHandler(BizException.class)
    protected ResultResponse handleBizException(BizException e) {
        final ResultResponse response = ResultResponse.builder().errCode("E04").errMsg(e.getMessage()).build();
        return response;
    }
    @ExceptionHandler(NoSuchElementException.class)
    protected ResultResponse handleException(NoSuchElementException e) {
        final ResultResponse response = ResultResponse.builder().errCode("E05").errMsg(e.getMessage()).build();
        return response;
    }
    
    @ExceptionHandler(Exception.class)
    protected ResultResponse handleException(Exception e) {
        final ResultResponse response = ResultResponse.builder().errCode("E99").errMsg(e.getMessage()).build();
        return response;
    }
}