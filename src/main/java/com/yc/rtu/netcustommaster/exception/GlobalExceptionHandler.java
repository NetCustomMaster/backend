package com.yc.rtu.netcustommaster.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public void handleAsyncException() {
    }
}