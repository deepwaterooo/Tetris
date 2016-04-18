package dev.ttetris.util;

public enum TextureAttachment {
    COLOR(0), DEPTH(1);
    
    private int value;

    private TextureAttachment(int arg3) {
        this.value = arg3;
    }

    public int getValue() {
        return this.value;
    }
}
