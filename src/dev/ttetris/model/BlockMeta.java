package dev.ttetris.model;

public class BlockMeta {
    private int centerX;
    private int centerY;
    private int centerZ;
    private CubeShift[] shifts;
    private CubeColor color;

    public BlockMeta(CubeColor paramCubeColor, CubeShift[] paramArrayOfCubeShift,
                     int paramInt1, int paramInt2, int paramInt3) {
        this.color = paramCubeColor;
        this.shifts = paramArrayOfCubeShift;
        this.centerX = paramInt1;
        this.centerY = paramInt2;
        this.centerZ = paramInt3;
    }

    public CubeColor getColor() { return this.color; }
    public CubeShift[] getShifts() { return this.shifts; }
    public int getCenterX() { return this.centerX; }
    public int getCenterY() { return this.centerY; }
    public int getCenterZ() { return this.centerZ; }
}
