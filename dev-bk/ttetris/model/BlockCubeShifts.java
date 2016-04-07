package dev.ttetris.model;

final class BlockCubeShifts {
    public static final CubeShift[] leftBootShifts;
    public static final CubeShift[] leftLightningShifts;
    public static final CubeShift[] lineShifts;
    public static final CubeShift[] rightBootShifts;
    public static final CubeShift[] rightLightningShifts;
    public static final CubeShift[] roofShifts;
    public static final CubeShift[] squareShifts;

    static {
        CubeShift[] arrayOfCubeShift1 = new CubeShift[4];
        arrayOfCubeShift1[0] = new CubeShift(0, 0, 0);
        arrayOfCubeShift1[1] = new CubeShift(0, 1, 0);
        arrayOfCubeShift1[2] = new CubeShift(1, 0, 0);
        arrayOfCubeShift1[3] = new CubeShift(1, 1, 0);
        squareShifts = arrayOfCubeShift1; // Ìï
        
        CubeShift[] arrayOfCubeShift2 = new CubeShift[4];
        arrayOfCubeShift2[0] = new CubeShift(0, 0, 0);
        arrayOfCubeShift2[1] = new CubeShift(0, 1, 0);
        arrayOfCubeShift2[2] = new CubeShift(0, 2, 0);
        arrayOfCubeShift2[3] = new CubeShift(0, 3, 0);
        lineShifts = arrayOfCubeShift2;   // I
        
        CubeShift[] arrayOfCubeShift3 = new CubeShift[4];
        arrayOfCubeShift3[0] = new CubeShift(0, 0, 0);
        arrayOfCubeShift3[1] = new CubeShift(0, 1, 0);
        arrayOfCubeShift3[2] = new CubeShift(1, 1, 0);
        arrayOfCubeShift3[3] = new CubeShift(1, 2, 0);
        leftLightningShifts = arrayOfCubeShift3;
        
        CubeShift[] arrayOfCubeShift4 = new CubeShift[4];
        arrayOfCubeShift4[0] = new CubeShift(1, 0, 0);
        arrayOfCubeShift4[1] = new CubeShift(1, 1, 0);
        arrayOfCubeShift4[2] = new CubeShift(0, 1, 0);
        arrayOfCubeShift4[3] = new CubeShift(0, 2, 0);
        rightLightningShifts = arrayOfCubeShift4;
        
        CubeShift[] arrayOfCubeShift5 = new CubeShift[4];
        arrayOfCubeShift5[0] = new CubeShift(0, 0, 0);
        arrayOfCubeShift5[1] = new CubeShift(-1, 0, 0);
        arrayOfCubeShift5[2] = new CubeShift(1, 0, 0);
        arrayOfCubeShift5[3] = new CubeShift(0, 1, 0);
        roofShifts = arrayOfCubeShift5;
        
        CubeShift[] arrayOfCubeShift6 = new CubeShift[4];
        arrayOfCubeShift6[0] = new CubeShift(0, 0, 0);
        arrayOfCubeShift6[1] = new CubeShift(1, 0, 0);
        arrayOfCubeShift6[2] = new CubeShift(1, 1, 0);
        arrayOfCubeShift6[3] = new CubeShift(1, 2, 0);
        leftBootShifts = arrayOfCubeShift6;
        
        CubeShift[] arrayOfCubeShift7 = new CubeShift[4];
        arrayOfCubeShift7[0] = new CubeShift(1, 0, 0);
        arrayOfCubeShift7[1] = new CubeShift(0, 0, 0);
        arrayOfCubeShift7[2] = new CubeShift(0, 1, 0);
        arrayOfCubeShift7[3] = new CubeShift(0, 2, 0);
        rightBootShifts = arrayOfCubeShift7;
    }
}
