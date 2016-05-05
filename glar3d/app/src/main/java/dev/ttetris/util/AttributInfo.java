package dev.ttetris.util;

import java.util.HashMap;
import java.util.Map;

public class AttributInfo {
    private static Map<Integer, String> typeMap = new HashMap();
    public final String name;
    public final int size;
    public final int type;

    static {
        typeMap.put(Integer.valueOf(5126), "FLOAT");
        typeMap.put(Integer.valueOf(35664), "FLOAT_VEC2");
        typeMap.put(Integer.valueOf(35665), "FLOAT_VEC3");
        typeMap.put(Integer.valueOf(35666), "FLOAT_VEC4");
        typeMap.put(Integer.valueOf(35674), "FLOAT_MAT2");
        typeMap.put(Integer.valueOf(35675), "FLOAT_MAT3");
        typeMap.put(Integer.valueOf(35676), "FLOAT_MAT4");
    }

    public AttributInfo(String paramString, int paramInt1, int paramInt2) {
        this.name = paramString;
        this.size = paramInt1;
        this.type = paramInt2;
    }

    public String getTypeName() {
        String str = (String)typeMap.get(Integer.valueOf(this.type));
        if (str == null)
            str = "UNKNOWN";
        return str;
    }

    public String toString() {
        return "name=" + this.name + ", size=" + this.size + ", type=" + getTypeName();
    }
}
