package dev.ttetris.model;

import java.io.Serializable;

public class Cube implements Cloneable, Comparable<Cube>, Serializable {
    private static final long serialVersionUID = 6144113039836213006L;
    private int color; 
    private float size;
    private float [] coords;
    private float x;
    private float y;
    private float z;

    public Cube(int paramCubeColor, float size, int paramInt1, int paramInt2, int paramInt3) {
        this.color = paramCubeColor;
        this.size = size;
        this.x = paramInt1;
        this.y = paramInt2;
        this.z = paramInt3;
        coords = new float[24];
        setCubeCoordinates();
    }

    public Cube clone() {
        try {
            Cube localCube = (Cube)super.clone();
            return localCube;
        } catch (CloneNotSupportedException localCloneNotSupportedException) {
        }
        return null;
    }

    public void setCubeCoordinates() { 
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
        return Math.abs(this.x - paramCube.x) < 0.00000001f ? 1 : 0;
    }

    public int getColor() { return this.color; }
    public void setX(float paramFloat) { this.x = paramFloat; }
    public void setY(float paramFloat) { this.y = paramFloat; }
    public void setZ(float paramFloat) { this.z = paramFloat; }
    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getZ() { return this.z; }

    /*
    public void setSize(float size) {
        this.size = size;
        } */
}
