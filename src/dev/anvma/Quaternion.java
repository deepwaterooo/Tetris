package dev.anvma;

public final class Quaternion { // ËÄÔª
    public static final Quaternion IDENTITY = new Quaternion(1.0F, 0.0F, 0.0F, 0.0F);
    public final float x;
    public final float y;
    public final float z;
    public final float w;

    public Quaternion(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        this.w = paramFloat1;
        this.x = paramFloat2;
        this.y = paramFloat3;
        this.z = paramFloat4;
    }

    public Quaternion(Vector3 paramVector3, float paramFloat) {
        float f1 = (float)Math.toRadians(0.5D * paramFloat); // ¹Â¶È
        float f2 = (float)Math.sin(f1);
        this.x = (f2 * paramVector3.x);
        this.y = (f2 * paramVector3.y);
        this.z = (f2 * paramVector3.z);
        this.w = ((float)Math.cos(f1));
    }

    public Quaternion multiply(Quaternion paramQuaternion) {
        return new Quaternion(this.w * paramQuaternion.w - this.x * paramQuaternion.x - this.y * paramQuaternion.y - this.z * paramQuaternion.z,
                              this.w * paramQuaternion.x + this.x * paramQuaternion.w + this.y * paramQuaternion.z - this.z * paramQuaternion.y,
                              this.w * paramQuaternion.y + this.y * paramQuaternion.w + this.z * paramQuaternion.x - this.x * paramQuaternion.z,
                              this.w * paramQuaternion.z + this.z * paramQuaternion.w + this.x * paramQuaternion.y - this.y * paramQuaternion.x
                              );
    }

    public float[] toAngleAxis() {
        float f1 = this.x * this.x + this.y * this.y + this.z * this.z;
        float f6 = 0.0f;
        float f3 = 0.0f;
        float f2 = 0.0f;
        float f4 = 0.0f;
        if (f1 > 0.0D) {
            f6 = (float)(1.0D / Math.sqrt(f1));
            f3 = (float)Math.toDegrees(2.0D * Math.acos(this.w));
            f2 = f6 * this.x;
            f4 = f6 * this.y;
        }
        for (float f5 = f6 * this.z; ; f5 = 0.0F) {
            return new float[] { f3, f2, f4, f5 };
            /*
            f2 = 1.0F;
            f3 = 0.0F;
            f4 = 0.0F;
            */
        }
    }

    public Matrix3 toRotationMatrix() {
        float f1 = 2.0F * this.x;
        float f2 = 2.0F * this.y;
        float f3 = 2.0F * this.z;
        float f4 = f1 * this.w;
        float f5 = f2 * this.w;
        float f6 = f3 * this.w;
        float f7 = f1 * this.x;
        float f8 = f2 * this.x;
        float f9 = f3 * this.x;
        float f10 = f2 * this.y;
        float f11 = f3 * this.y;
        float f12 = f3 * this.z;
        return new Matrix3(1.0F - (f10 + f12), f8 - f6, f9 + f5,
                           f8 + f6, 1.0F - (f7 + f12), f11 - f4,
                           f9 - f5, f11 + f4, 1.0F - (f7 + f10));
    }

    public String toString() {
        return "[w=" + this.w + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + "]";
    }
}
