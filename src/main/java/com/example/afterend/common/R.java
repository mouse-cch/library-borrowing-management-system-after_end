package com.example.afterend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一API返回结果封装
 * @author cch
 * @since 2026-06-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {
    private Integer code;
    private String msg;
    private T data;
    private String timestamp;

    public static <T> R<T> ok(T data) {
        return new R<>(200, "操作成功", data, java.time.LocalDateTime.now().toString().replace("T", " "));
    }

    public static <T> R<T> ok(String msg, T data) {
        return new R<>(200, msg, data, java.time.LocalDateTime.now().toString().replace("T", " "));
    }

    public static <T> R<T> fail(Integer code, String msg) {
        return new R<>(code, msg, null, java.time.LocalDateTime.now().toString().replace("T", " "));
    }

    public static <T> R<T> fail(String msg) {
        return new R<>(400, msg, null, java.time.LocalDateTime.now().toString().replace("T", " "));
    }
}
