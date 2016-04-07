package dev.ttetris.model;

public enum CubeColor {
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
    private CubeColor(String str, int val) {
        this.name = str;
        this.val = val;
    }
 
