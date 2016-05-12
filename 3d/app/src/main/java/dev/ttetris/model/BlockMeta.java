package dev.ttetris.model;

public class BlockMeta {
    private float centerX;
    private float centerY;
    private float centerZ;
    private CubeShift[] shifts;
    private CubeColor color;
    
    public BlockMeta(CubeColor paramCubeColor, CubeShift[] paramArrayOfCubeShift,
                     float paramFloat1, float paramFloat2, float paramFloat3) {
        this.color = paramCubeColor;
        this.shifts = paramArrayOfCubeShift;
        this.centerX = paramFloat1;
        this.centerY = paramFloat2;
        this.centerZ = paramFloat3;
    }

    public CubeColor getColor() { return this.color; }
    public CubeShift[] getShifts() { return this.shifts; }
    public float getCenterX() { return this.centerX; }
    public float getCenterY() { return this.centerY; }
    public float getCenterZ() { return this.centerZ; }
}
