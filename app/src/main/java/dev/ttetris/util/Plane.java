package dev.ttetris.util;

public final class Plane {
    public final float d;
    public final Vector3 normal;

    public Plane(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        this.normal = new Vector3(paramFloat1, paramFloat2, paramFloat3);
        this.d = paramFloat4;
    }

    public Plane(Vector3 paramVector3, float paramFloat) {
        this.normal = paramVector3;
        this.d = paramFloat;
    }

    public Plane normalise() {
        float f1 = this.normal.length(); // 3
        if (f1 > 1.0E-08F) {
            float f2 = 1.0F / f1;
            Plane result = new Plane(this.normal.multiply(f2), f2 * this.d);
            return result;
            //this = new Plane(this.normal.multiply(f2), f2 * this.d);
        }
        return this;
    }

    public Vector3 projectVector(Vector3 paramVector3) {
        return new Matrix3(1.0F - this.normal.x * this.normal.x,
                           -this.normal.x * this.normal.y,
                           -this.normal.x * this.normal.z,
                           -this.normal.y * this.normal.x,
                           1.0F - this.normal.y * this.normal.y,
                           -this.normal.y * this.normal.z,
                           -this.normal.z * this.normal.x,
                           -this.normal.z * this.normal.y,
                           1.0F - this.normal.z * this.normal.z).multiply(paramVector3);
    }
}
