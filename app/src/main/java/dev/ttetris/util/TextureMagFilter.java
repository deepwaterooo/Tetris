package dev.ttetris.util;

public enum TextureMagFilter {
    NEAREST("NEAREST", 0, 9700), // FOR LATER DEBUG
    LINEAR("LINEAR", 1, 9729); // add one more value

    private String name;
    private int value;
    private int valueb;

    private TextureMagFilter(String name, int arg2, int arg3) {
        this.name = name;
        this.value = arg2;
        this.valueb = arg3;
    }

    public int getValue() {
        return this.value;
    }
}

