package com.example.take_out.exception;

/**
 * 业务异常
 */

public class ServiceException extends RuntimeException {
    public ServiceException(String msg) {
        super(msg);
    }
}
