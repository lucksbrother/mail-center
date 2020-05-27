package com.haier.mailcenter.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 组装返回值类
 * TODO 待改造枚举类型，将统一返回值封装到一个枚举类中统一管理
 */
public class ResponseMap {

    public static Map<String, Object> assembleResultMessage(int error, String msg) {
        Map<String, Object> result = new HashMap<>();
        result.put("error", error);
        result.put("message", msg);
        return result;
    }

    public static Map<String, Object> assembleResultMessage(int error, String msg, String ex) {
        Map<String, Object> result = new HashMap<>();
        result.put("error", error);
        result.put("message", msg);
        result.put("ex", ex);
        return result;
    }
}
