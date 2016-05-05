package dev.ttetris.model;

final class CubeShift {
    private int dx;
    private int dy;
    private int dz;
    
    public CubeShift(int paramInt1, int paramInt2, int paramInt3) {
        this.dx = paramInt1;
        this.dy = paramInt2;
        this.dz = paramInt3;
    }

    public int getDx() {
        return this.dx;
    }

    public int getDy() {
        return this.dy;
    }

    public int getDz() {
        return this.dz;
    }
}
