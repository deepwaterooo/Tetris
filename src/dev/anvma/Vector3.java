package dev.anvma;

public final class Vector3 {
    public static final Vector3 UNIT_X = new Vector3(1.0F, 0.0F, 0.0F);
    public static final Vector3 UNIT_Y = new Vector3(0.0F, 1.0F, 0.0F);
    public static final Vector3 UNIT_Z = new Vector3(0.0F, 0.0F, 1.0F);
    public static final Vector3 ZERO = new Vector3(0.0F, 0.0F, 0.0F);
    public final float x;
    public final float y;
    public final float z;

    public Vector3(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.x = paramFloat1;
        this.y = paramFloat2;
        this.z = paramFloat3;
    }

    public static Vector3 interpolate(Vector3 paramVector31, Vector3 paramVector32, float paramFloat) {
        return new Vector3(paramVector31.x + paramFloat * (paramVector32.x - paramVector31.x), paramVector31.y + paramFloat * (paramVector32.y - paramVector31.y), paramVector31.z + paramFloat * (paramVector32.z - paramVector31.z));
    }

    public Vector3 add(Vector3 paramVector3) {
        return new Vector3(this.x + paramVector3.x, this.y + paramVector3.y, this.z + paramVector3.z);
    }

    public Vector3 cross(Vector3 paramVector3) {
        return new Vector3(this.y * paramVector3.z - this.z * paramVector3.y, this.z * paramVector3.x - this.x * paramVector3.z, this.x * paramVector3.y - this.y * paramVector3.x);
    }

    public double distance(Vector3 paramVector3) {
        return subtract(paramVector3).length();
    }

    public Vector3 divide(float paramFloat) {
        return new Vector3(this.x / paramFloat, this.y / paramFloat, this.z / paramFloat);
    }

    public float dot(Vector3 paramVector3) {
        return this.x * paramVector3.x + this.y * paramVector3.y + this.z * paramVector3.z;
    }

    public float length() {
        return (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector3 multiply(float paramFloat) {
        return new Vector3(paramFloat * this.x, paramFloat * this.y, paramFloat * this.z);
    }

    public Vector3 normalise() {
        float f1 = length();
        if (f1 > 1.0E-08F) {
            float f2 = 1.0F / f1;
            Vector3 result = new Vector3(f2 * this.x, f2 * this.y, f2 * this.z);
            //this = new Vector3(f2 * this.x, f2 * this.y, f2 * this.z);
            return result;  
        }
        return this;
    }

    public Vector3 subtract(Vector3 paramVector3) {
        return new Vector3(this.x - paramVector3.x, this.y - paramVector3.y, this.z - paramVector3.z);
    }

    public String toString() {
        return "[x=" + this.x + ", y=" + this.y + ", z=" + this.z + "]";
    }
}
