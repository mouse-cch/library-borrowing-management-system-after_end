package com.example.afterend.exception;

import lombok.Getter;

/**
 * 业务异常
 * @author cch
 * @since 2026-06-29
 */
@Getter
public class BusinessException extends RuntimeException {
    private final Integer code;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }
}
