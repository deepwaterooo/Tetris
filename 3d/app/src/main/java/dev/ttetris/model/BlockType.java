package dev.ttetris.model;

import dev.ttetris.model.Cube;

public final class BlockType {
    public static final Cube[] leftBootType;
    public static final Cube[] leftLightningType;
    public static final Cube[] lineType;
    public static final Cube[] rightBootType;
    public static final Cube[] rightLightningType;
    public static final Cube[] roofType;
    public static final Cube[] squareType;

    static {
        Cube[] arrayOfCube1 = new Cube[4];
        arrayOfCube1[0] = new Cube(1, 0, 0, 0);
        arrayOfCube1[1] = new Cube(1, 0, 1, 0);
        arrayOfCube1[2] = new Cube(1, 1, 0, 0);
        arrayOfCube1[3] = new Cube(1, 1, 1, 0);
        squareType = arrayOfCube1; // Ìï, red
        
        Cube[] arrayOfCube2 = new Cube[4];
        arrayOfCube2[0] = new Cube(2, 0, -1, 0);
        arrayOfCube2[1] = new Cube(2, 0, 0, 0);
        arrayOfCube2[2] = new Cube(2, 0, 1, 0);
        arrayOfCube2[3] = new Cube(2, 0, 2, 0);
        lineType = arrayOfCube2;   // I, green
        
        Cube[] arrayOfCube3 = new Cube[4];
        arrayOfCube3[0] = new Cube(3, 0, 0, 0);
        arrayOfCube3[1] = new Cube(3, 0, 1, 0);
        arrayOfCube3[2] = new Cube(3, 1, 1, 0);
        arrayOfCube3[3] = new Cube(3, 1, 2, 0);
        leftLightningType = arrayOfCube3; // 3
        
        Cube[] arrayOfCube4 = new Cube[4];
        arrayOfCube4[0] = new Cube(4, 1, 0, 0);
        arrayOfCube4[1] = new Cube(4, 1, 1, 0);
        arrayOfCube4[2] = new Cube(4, 0, 1, 0);
        arrayOfCube4[3] = new Cube(4, 0, 2, 0);
        rightLightningType = arrayOfCube4;
        
        Cube[] arrayOfCube5 = new Cube[4];
        arrayOfCube5[0] = new Cube(5, 0, 0, 0);
        arrayOfCube5[1] = new Cube(5, -1, 0, 0);
        arrayOfCube5[2] = new Cube(5, 1, 0, 0);
        arrayOfCube5[3] = new Cube(5, 0, 1, 0);
        roofType = arrayOfCube5;
        
        Cube[] arrayOfCube6 = new Cube[4];
        arrayOfCube6[0] = new Cube(6, 0, 0, 0);
        arrayOfCube6[1] = new Cube(6, 1, 0, 0);
        arrayOfCube6[2] = new Cube(6, 1, 1, 0);
        arrayOfCube6[3] = new Cube(6, 1, 2, 0);
        leftBootType = arrayOfCube6;
        
        Cube[] arrayOfCube7 = new Cube[4];
        arrayOfCube7[0] = new Cube(7, 1, 0, 0);
        arrayOfCube7[1] = new Cube(7, 0, 0, 0);
        arrayOfCube7[2] = new Cube(7, 0, 1, 0);
        arrayOfCube7[3] = new Cube(7, 0, 2, 0);
        rightBootType = arrayOfCube7;
    }
}
