package dev.ttetris.util;

public enum TextureWrap {
    REPEAT("REPEAT", 0, 0),
    CLAMP_TO_EDGE("CLAMP_TO_EDGE", 1, 33071),
    MIRRORED_REPEAT("MIRRORED_REPEAT", 2, 33648);
    private String name;
    private int value;
    private int valueb;

    private TextureWrap(String name, int arg2, int arg3) {
        this.name = name;
        this.value = arg2;
        this.valueb = arg3;
    }

    public int getValue() {
        return this.value;
    }
}
