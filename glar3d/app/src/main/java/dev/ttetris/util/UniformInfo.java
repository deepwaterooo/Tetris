package dev.ttetris.util;

import java.util.HashMap;
import java.util.Map;

public class UniformInfo {
    private static Map<Integer, String> typeMap = new HashMap();
    public final String name;
    public final int size;
    public final int type;

    static {
        typeMap.put(Integer.valueOf(5126), "FLOAT");
        typeMap.put(Integer.valueOf(35664), "FLOAT_VEC2");
        typeMap.put(Integer.valueOf(35665), "FLOAT_VEC3");
        typeMap.put(Integer.valueOf(35666), "FLOAT_VEC4");
        typeMap.put(Integer.valueOf(5124), "INT");
        typeMap.put(Integer.valueOf(35667), "INT_VEC2");
        typeMap.put(Integer.valueOf(35668), "INT_VEC3");
        typeMap.put(Integer.valueOf(35669), "INT_VEC4");
        typeMap.put(Integer.valueOf(35670), "BOOL");
        typeMap.put(Integer.valueOf(35671), "BOOL_VEC2");
        typeMap.put(Integer.valueOf(35672), "BOOL_VEC3");
        typeMap.put(Integer.valueOf(35673), "BOOL_VEC4");
        typeMap.put(Integer.valueOf(35674), "FLOAT_MAT2");
        typeMap.put(Integer.valueOf(35675), "FLOAT_MAT3");
        typeMap.put(Integer.valueOf(35676), "FLOAT_MAT4");
        typeMap.put(Integer.valueOf(35678), "SAMPLER_2D");
        typeMap.put(Integer.valueOf(35680), "SAMPLER_CUBE");
    }

    public UniformInfo(String paramString, int paramInt1, int paramInt2) {
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
        return "[name=" + this.name + ", size=" + this.size + ", type=" + getTypeName() + "]";
    }
}
