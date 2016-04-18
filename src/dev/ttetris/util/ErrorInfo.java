package dev.ttetris.util;

import java.util.HashMap;
import java.util.Map;

public class ErrorInfo {
    private static Map<Integer, String> errorDescMap = new HashMap();

    static {
        errorDescMap.put(Integer.valueOf(1280), "Enum argument out of range.");
        errorDescMap.put(Integer.valueOf(1286), "Framebuffer is incomplete.");
        errorDescMap.put(Integer.valueOf(1281), "Numeric argument out of range.");
        errorDescMap.put(Integer.valueOf(1282), "Operation illegal in current state.");
        errorDescMap.put(Integer.valueOf(1285), "Not enough memory left to execute command.");
        errorDescMap.put(Integer.valueOf(0), "No error encountered.");
    }

    public static String getDesc(int paramInt) {
        String str = (String)errorDescMap.get(Integer.valueOf(paramInt));
        if (str != null)
            return str;
        return "Unknown error";
    }
}
