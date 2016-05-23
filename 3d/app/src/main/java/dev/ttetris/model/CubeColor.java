package dev.ttetris.model;

public enum CubeColor {
    Brass(0xffff0000, (byte) 1),   // red
    Anchient(0xff00ff00, (byte) 2), // green 
    Amethyst(0xff0000ff, (byte) 3),  // blue
    Oak(0xffffff00, (byte) 4),       // yellow
    MarbleRough(0xff00ffff, (byte) 5),  // cyan
    LapisLazuli(0xffffffff, (byte) 6), // white
    WhiteMarble(0xffff00ff, (byte) 7),// magenta
    Marble(0x20320617, (byte) 8), // transparent
    Hidden(0x12345678, (byte)9);

    public final int color;
    public final byte value;
    private CubeColor(int color, byte value) {
        this.color = color;
        this.value = value;
    }
    /*Brass("Brass", (byte)1),
      Anchient("Anchient", (byte)2),
      Amethyst("Amethyst", (byte)3),
      Oak("Oak", (byte)4),
      MarbleRough("MarbleRough", (byte)5),
      LapisLazuli("LapisLazuli", (byte)6),
      WhiteMarble("WhiteMarble", (byte)7),
      Marble("Marble", (byte)8),
      Hidden("Hidden", (byte)9);

    private final String name;
    private final byte val;
    private CubeColor(String str, byte val) {
        this.name = str;
        this.val = val;
    }      */
}
