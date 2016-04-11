package dev.ttetris.util;

public enum TextureIndex {
    TEXTURE0(0),
    TEXTURE1(1),
    TEXTURE2(2),
    TEXTURE3(3),
    TEXTURE4(4),
    TEXTURE5(5),
    TEXTURE6(6),
    TEXTURE7(7);
    private int value;

    private TextureIndex(int arg3) {
        this.value = arg3;
    }

    public int getValue() {
        return this.value;
    }
}
