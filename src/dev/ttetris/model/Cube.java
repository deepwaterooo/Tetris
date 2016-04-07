package dev.ttetris.model;

import java.io.Serializable;

public class Cube implements Cloneable, Comparable<Cube>, Serializable {
    private static final long serialVersionUID = 6144113039836213006L;
    private CubeColor color; // indicating color and color
    private float size;
    private float [] coords;
    private int x;
    private int y;
    private int z;
    /*
    private int rx;
    private int ry;
    private int rz;
    */

    public Cube(CubeColor paramCubeColor, float size, int paramInt1, int paramInt2, int paramInt3) {
        //int paramInt4, int paramInt5, int paramInt6) {
        this.color = paramCubeColor;
        this.size = size;
        this.x = paramInt1;
        this.y = paramInt2;
        this.z = paramInt3;
        coords = new float[24];
        setCubeCoordinates();
        /*
        this.rx = paramInt4;
        this.ry = paramInt5;
        this.rz = paramInt6; */
    }

    public Cube clone() {
        try {
            Cube localCube = (Cube)super.clone();
            return localCube;
        } catch (CloneNotSupportedException localCloneNotSupportedException) {
        }
        return null;
    }

    public void setCubeCoordinates() { // eventually may need to set size to someth different
        float [] res = {
            x-size, y-size, z-size, // 0
            x+size, y-size, z-size,  // 1
            x+size, y+size, z-size,   // 2
            x-size, y+size, z-size,  // 3
            x-size, y-size, z+size,  // 4
            x+size, y-size, z+size,   // 5
            x+size, y+size, z+size,    // 6
            x-size, y+size, z+size    // 7
        };
        coords = res;
    }
    
    public float[] getCubeCoordinates() {
        return coords;
    }
    
    public int compareTo(Cube paramCube) {
        return this.x - paramCube.x;
    }

    public CubeColor getColor() { return this.color; }
    public void setX(int paramInt) { this.x = paramInt; }
    public void setY(int paramInt) { this.y = paramInt; }
    public void setZ(int paramInt) { this.z = paramInt; }
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getZ() { return this.z; }

    public void setSize(float size) {
        this.size = size;
        

    }
    /*    
    public void setRx(int paramInt) { this.rx = paramInt; }
    public void setRy(int paramInt) { this.ry = paramInt; }
    public void setRz(int paramInt) { this.rz = paramInt; }
    public int getRx() { return this.rx; }
    public int getRy() { return this.ry; }
    public int getRz() { return this.rz; }
    */
}
