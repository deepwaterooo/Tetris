package dev.anogl;

public class Light {
    public final float[] ambientColor = { 0.0F, 0.0F, 0.0F, 1.0F };
    public final float[] diffuseColor = { 1.0F, 1.0F, 1.0F, 1.0F };
    public final float[] specularColor = { 1.0F, 1.0F, 1.0F, 1.0F };

    public void setAmbientColor(float paramFloat) {
        setAmbientColor(paramFloat, paramFloat, paramFloat);
    }

    public void setAmbientColor(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.ambientColor[0] = paramFloat1;
        this.ambientColor[1] = paramFloat2;
        this.ambientColor[2] = paramFloat3;
    }

    public void setDiffuseColor(float paramFloat) {
        setDiffuseColor(paramFloat, paramFloat, paramFloat);
    }

    public void setDiffuseColor(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.diffuseColor[0] = paramFloat1;
        this.diffuseColor[1] = paramFloat2;
        this.diffuseColor[2] = paramFloat3;
    }

    public void setSpecularColor(float paramFloat) {
        setSpecularColor(paramFloat, paramFloat, paramFloat);
    }

    public void setSpecularColor(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.specularColor[0] = paramFloat1;
        this.specularColor[1] = paramFloat2;
        this.specularColor[2] = paramFloat3;
    }
}
