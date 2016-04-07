package dev.anogl;

import android.opengl.Matrix;
import dev.anvma.Plane;
import dev.anvma.Vector3;

public class RenderParams
    implements Cloneable {
    public final float[] cameraPositionView = new float[4];
    public final float[] cameraPositionWorld = new float[4];
    public Light light;
    public Material prevMaterial;
    public final float[] projMatrix = new float[16];
    public final float[] reflectionMatrix = new float[16];
    public final float[] viewInvMatrix = new float[16];
    public final float[] viewMatrix = new float[16];
    public final float[] viewProjMatrix = new float[16];

    private void buildReflectionMatrix(Plane paramPlane) {
        this.reflectionMatrix[0] = (1.0F + -2.0F * paramPlane.normal.x * paramPlane.normal.x);
        this.reflectionMatrix[1] = (-2.0F * paramPlane.normal.y * paramPlane.normal.x);
        this.reflectionMatrix[2] = (-2.0F * paramPlane.normal.z * paramPlane.normal.x);
        this.reflectionMatrix[3] = 0.0F;
        this.reflectionMatrix[4] = (-2.0F * paramPlane.normal.x * paramPlane.normal.y);
        this.reflectionMatrix[5] = (1.0F + -2.0F * paramPlane.normal.y * paramPlane.normal.y);
        this.reflectionMatrix[6] = (-2.0F * paramPlane.normal.z * paramPlane.normal.y);
        this.reflectionMatrix[7] = 0.0F;
        this.reflectionMatrix[8] = (-2.0F * paramPlane.normal.x * paramPlane.normal.z);
        this.reflectionMatrix[9] = (-2.0F * paramPlane.normal.y * paramPlane.normal.z);
        this.reflectionMatrix[10] = (1.0F + -2.0F * paramPlane.normal.z * paramPlane.normal.z);
        this.reflectionMatrix[11] = 0.0F;
        this.reflectionMatrix[12] = (-2.0F * paramPlane.normal.x * paramPlane.d);
        this.reflectionMatrix[13] = (-2.0F * paramPlane.normal.y * paramPlane.d);
        this.reflectionMatrix[14] = (-2.0F * paramPlane.normal.z * paramPlane.d);
        this.reflectionMatrix[15] = 1.0F;
    }

    public RenderParams clone() {
        try {
            RenderParams localRenderParams = (RenderParams)super.clone();
            return localRenderParams;
        } catch (CloneNotSupportedException localCloneNotSupportedException) {
            throw new RuntimeException(localCloneNotSupportedException);
        }
    }

    public void makeFrustum(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        float f1 = paramFloat3 * (float)Math.tan(Math.toRadians(0.5D * paramFloat1));
        float f2 = f1 * paramFloat2;
        Matrix.frustumM(this.projMatrix, 0, -f2, f2, -f1, f1, paramFloat3, paramFloat4);
    }

    public void setLookAt(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9) {
        Matrix.setLookAtM(this.viewMatrix, 0, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9);
        Matrix.invertM(this.viewInvMatrix, 0, this.viewMatrix, 0);
        this.cameraPositionWorld[0] = paramFloat1;
        this.cameraPositionWorld[1] = paramFloat2;
        this.cameraPositionWorld[2] = paramFloat3;
        this.cameraPositionWorld[3] = 1.0F;
        Matrix.multiplyMV(this.cameraPositionView, 0, this.viewMatrix, 0, this.cameraPositionWorld, 0);
    }

    public void update() {
        Matrix.multiplyMM(this.viewProjMatrix, 0, this.projMatrix, 0, this.viewMatrix, 0);
        this.prevMaterial = null;
    }
}
