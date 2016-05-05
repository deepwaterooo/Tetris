package dev.ttetris.util;

import android.opengl.GLES20;

public class Texture {
    protected final int target;
    protected final int textureId;

    public Texture(int paramInt, TextureParams paramTextureParams) {
        this.target = paramInt;
        int[] arrayOfInt = new int[1];
        GLES20.glGenTextures(1, arrayOfInt, 0);
        this.textureId = arrayOfInt[0];
        GLES20.glBindTexture(paramInt, this.textureId);
        GLES20.glTexParameterf(paramInt, 10241, paramTextureParams.minFilter.getValue());
        GLES20.glTexParameterf(paramInt, 10240, paramTextureParams.magFilter.getValue());
        GLES20.glTexParameteri(paramInt, 10242, paramTextureParams.wrapS.getValue());
        GLES20.glTexParameteri(paramInt, 10243, paramTextureParams.wrapT.getValue());
    }

    public int getTarget() {
        return this.target;
    }

    public int getTextureId() {
        return this.textureId;
    }
}
