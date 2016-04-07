package dev.ttetris.model;

class BlockMeta {
    private float centerX;
    private float centerY;
    private float centerZ;
    private CubeShift[] shifts;
    private CubeType type;

    public BlockMeta(CubeType paramCubeType, CubeShift[] paramArrayOfCubeShift,
                     float paramFloat1, float paramFloat2, float paramFloat3) {
        this.type = paramCubeType;
        this.shifts = paramArrayOfCubeShift;
        this.centerX = paramFloat1;
        this.centerY = paramFloat2;
        this.centerZ = paramFloat3;
    }

    public CubeType getType() { return this.type; }
    public CubeShift[] getShifts() { return this.shifts; }
    public float getCenterX() { return this.centerX; }
    public float getCenterY() { return this.centerY; }
    public float getCenterZ() { return this.centerZ; }
}
