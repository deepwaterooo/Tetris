package dev.anvma;

public final class Matrix3 {
    public final float m00;
    public final float m01;
    public final float m02;
    public final float m10;
    public final float m11;
    public final float m12;
    public final float m20;
    public final float m21;
    public final float m22;

    public Matrix3(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9) {
        this.m00 = paramFloat1;
        this.m01 = paramFloat2;
        this.m02 = paramFloat3;
        this.m10 = paramFloat4;
        this.m11 = paramFloat5;
        this.m12 = paramFloat6;
        this.m20 = paramFloat7;
        this.m21 = paramFloat8;
        this.m22 = paramFloat9;
    }

    public Matrix3(Vector3 paramVector3, double paramDouble) {
        float f1 = (float)Math.toRadians(paramDouble);
        float f2 = (float)Math.cos(f1);
        float f3 = (float)Math.sin(f1);
        float f4 = 1.0F - f2;
        float f5 = paramVector3.x * paramVector3.x;
        float f6 = paramVector3.y * paramVector3.y;
        float f7 = paramVector3.z * paramVector3.z;
        float f8 = f4 * (paramVector3.x * paramVector3.y);
        float f9 = f4 * (paramVector3.x * paramVector3.z);
        float f10 = f4 * (paramVector3.y * paramVector3.z);
        float f11 = f3 * paramVector3.x;
        float f12 = f3 * paramVector3.y;
        float f13 = f3 * paramVector3.z;
        this.m00 = (f2 + f5 * f4);
        this.m01 = (f8 - f13);
        this.m02 = (f9 + f12);
        this.m10 = (f8 + f13);
        this.m11 = (f2 + f6 * f4);
        this.m12 = (f10 - f11);
        this.m20 = (f9 - f12);
        this.m21 = (f10 + f11);
        this.m22 = (f2 + f7 * f4);
    }

    public Matrix3(Vector3 paramVector31, Vector3 paramVector32, Vector3 paramVector33) {
        this.m00 = paramVector31.x;
        this.m01 = paramVector31.y;
        this.m02 = paramVector31.z;
        this.m10 = paramVector32.x;
        this.m11 = paramVector32.y;
        this.m12 = paramVector32.z;
        this.m20 = paramVector33.x;
        this.m21 = paramVector33.y;
        this.m22 = paramVector33.z;
    }

    public Vector3 multiply(Vector3 paramVector3) {
        return new Vector3(this.m00 * paramVector3.x + this.m01 * paramVector3.y + this.m02 * paramVector3.z, this.m10 * paramVector3.x + this.m11 * paramVector3.y + this.m12 * paramVector3.z, this.m20 * paramVector3.x + this.m21 * paramVector3.y + this.m22 * paramVector3.z);
    }
}
