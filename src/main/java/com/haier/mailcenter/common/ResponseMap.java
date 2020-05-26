package com.haier.mailcenter.common;

import java.util.HashMap;
import java.util.Map;

public class ResponseMap {

    public static Map<String, Object> assembleResultMessage(int error, String msg, String ex) {
        Map<String, Object> result = new HashMap<>();
        result.put("error", error);
        result.put("message", msg);
        result.put("ex", ex);
        return result;
    }
}
