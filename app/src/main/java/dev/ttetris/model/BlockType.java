package dev.ttetris.model;

public final class BlockType {
    public static final CubeShift[] leftBootType;
    public static final CubeShift[] leftLightningType;
    public static final CubeShift[] lineType;
    public static final CubeShift[] rightBootType;
    public static final CubeShift[] rightLightningType;
    public static final CubeShift[] roofType;
    public static final CubeShift[] squareType;

    static {
        CubeShift[] arrayOfCubeShift1 = new CubeShift[4];
        arrayOfCubeShift1[0] = new CubeShift(0, 0, 0);
        arrayOfCubeShift1[1] = new CubeShift(0, 1, 0);
        arrayOfCubeShift1[2] = new CubeShift(1, 0, 0);
        arrayOfCubeShift1[3] = new CubeShift(1, 1, 0);
        squareType = arrayOfCubeShift1; //
        
        CubeShift[] arrayOfCubeShift2 = new CubeShift[4];
        arrayOfCubeShift2[0] = new CubeShift(0, 0, 0);
        arrayOfCubeShift2[1] = new CubeShift(0, 1, 0);
        arrayOfCubeShift2[2] = new CubeShift(0, 2, 0);
        arrayOfCubeShift2[3] = new CubeShift(0, 3, 0);
        lineType = arrayOfCubeShift2;   // I
        
        CubeShift[] arrayOfCubeShift3 = new CubeShift[4];
        arrayOfCubeShift3[0] = new CubeShift(0, 0, 0);
        arrayOfCubeShift3[1] = new CubeShift(0, 1, 0);
        arrayOfCubeShift3[2] = new CubeShift(1, 1, 0);
        arrayOfCubeShift3[3] = new CubeShift(1, 2, 0);
        leftLightningType = arrayOfCubeShift3;
        
        CubeShift[] arrayOfCubeShift4 = new CubeShift[4];
        arrayOfCubeShift4[0] = new CubeShift(1, 0, 0);
        arrayOfCubeShift4[1] = new CubeShift(1, 1, 0);
        arrayOfCubeShift4[2] = new CubeShift(0, 1, 0);
        arrayOfCubeShift4[3] = new CubeShift(0, 2, 0);
        rightLightningType = arrayOfCubeShift4;
        
        CubeShift[] arrayOfCubeShift5 = new CubeShift[4];
        arrayOfCubeShift5[0] = new CubeShift(0, 0, 0);
        arrayOfCubeShift5[1] = new CubeShift(-1, 0, 0);
        arrayOfCubeShift5[2] = new CubeShift(1, 0, 0);
        arrayOfCubeShift5[3] = new CubeShift(0, 1, 0);
        roofType = arrayOfCubeShift5;
        
        CubeShift[] arrayOfCubeShift6 = new CubeShift[4];
        arrayOfCubeShift6[0] = new CubeShift(0, 0, 0);
        arrayOfCubeShift6[1] = new CubeShift(1, 0, 0);
        arrayOfCubeShift6[2] = new CubeShift(1, 1, 0);
        arrayOfCubeShift6[3] = new CubeShift(1, 2, 0);
        leftBootType = arrayOfCubeShift6;
        
        CubeShift[] arrayOfCubeShift7 = new CubeShift[4];
        arrayOfCubeShift7[0] = new CubeShift(1, 0, 0);
        arrayOfCubeShift7[1] = new CubeShift(0, 0, 0);
        arrayOfCubeShift7[2] = new CubeShift(0, 1, 0);
        arrayOfCubeShift7[3] = new CubeShift(0, 2, 0);
        rightBootType = arrayOfCubeShift7;
    }
}
