package dev.ttetris.util;

public enum TextureMinFilter {
    LINEAR("LINEAR", 1, 9729),
    NEAREST_MIPMAP_NEAREST("NEAREST_MIPMAP_NEAREST", 2, 9984),
    NEAREST_MIPMAP_LINEAR("NEAREST_MIPMAP_LINEAR", 3, 9986),
    LINEAR_MIPMAP_NEAREST("LINEAR_MIPMAP_NEAREST", 4, 9985),
    LINEAR_MIPMAP_LINEAR("LINEAR_MIPMAP_LINEAR", 5, 9987);

    private String name;
    private int value;
    private int valueb;

    private TextureMinFilter(String name, int arg2, int arg3) {
        this.name = name;
        this.value = arg2;
        this.valueb = arg3;
    }

    public int getValue() {
        return this.value;
    }

    public boolean needMipMap() {
        switch (this.value) {
        default:
            return false;
        case 9984:
        case 9985:
        case 9986:
        case 9987:
        }
        return true;
    }
}
