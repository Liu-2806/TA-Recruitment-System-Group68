package com.bupt.ta.exception;

/**
 * 无权限访问异常。
 */
public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
