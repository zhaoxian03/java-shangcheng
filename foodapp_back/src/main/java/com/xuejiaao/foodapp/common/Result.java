// common/Result.java
package com.xuejiaao.foodapp.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {
    private Integer code;
    private String message;
    private Object data;
    private Map<String, Object> extra = new HashMap<>();

    // 私有构造方法
    private Result() {}

    private Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private Result(Integer code, String message, Object data, Map<String, Object> extra) {
        this.code = code;
        this.message = message;
        this.data = data;
        if (extra != null) {
            this.extra = extra;
        }
    }

    // 成功静态方法
    public static Result success() {
        return new Result(200, "操作成功");
    }

    public static Result success(Object data) {
        return new Result(200, "操作成功", data);
    }

    public static Result success(String message, Object data) {
        return new Result(200, message, data);
    }

    // 错误静态方法
    public static Result error(Integer code, String message) {
        return new Result(code, message);
    }

    public static Result error(Integer code, String message, Map<String, Object> extra) {
        return new Result(code, message, null, extra);
    }

    public static Result error(Integer code, String message, Object data, Map<String, Object> extra) {
        return new Result(code, message, data, extra);
    }

    public static Result error(String message) {
        return error(500, message);
    }

    // 添加额外信息（链式调用）
    public Result addExtra(String key, Object value) {
        this.extra.put(key, value);
        return this;
    }

    // 快速创建带有extra的错误结果
    public static Result errorWithExtra(Integer code, String message, String extraKey, Object extraValue) {
        Map<String, Object> extra = new HashMap<>();
        extra.put(extraKey, extraValue);
        return new Result(code, message, null, extra);
    }
}