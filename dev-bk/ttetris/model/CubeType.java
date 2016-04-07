package dev.ttetris.model;

public enum CubeType {
    Hidden("Hidden", 0),  // move to be 8
    Brass("Brass", 1),
    Anchient("Anchient", 2),
    Amethyst("Amethyst", 3),
    Oak("Oak", 4),
    MarbleRough("MarbleRough", 5),
    LapisLazuli("LapisLazuli", 6),
    WhiteMarble("WhiteMarble", 7),
    Marble("Marble", 8);

    private final String name;
    private final int val;
    private CubeType(String str, int val) {
        this.name = str;
        this.val = val;
    }
    //public static CubeType[] arrayOfCubeType = new CubeType[9];
}
