package dev.tetris.view.material;

import android.opengl.GLES20;
import android.opengl.Matrix;
import dev.anogl.AssetManager;
import dev.anogl.Attribute;
import dev.anogl.Material;
import dev.anogl.RenderParams;
import dev.anogl.Shader;
import dev.ttetris.view.BrickBatch;
import dev.ttetris.view.ViewConstants;

public class BrickEnvBatchMaterial extends Material
    implements BrickBatch {
    private int uCameraPositionWorldHandle;
    private float[] uTranslations;
    private int uTranslationsHandle;
    private int uWorldMatrixHandle;
    private float[] uWorldViewProjMatrix = new float[16];
    private int uWorldViewProjMatrixHandle;

    public BrickEnvBatchMaterial(AssetManager paramAssetManager, String paramString) {
        super(paramAssetManager.getShader("Shaders/BrickEnvBatch.glsl"));
        addTexture(paramAssetManager.getTexture(paramString), "sBaseMap");
        addTexture(paramAssetManager.getCubeTexture("Cubemaps/EnvMap.png"), "sEnvMap");
        addAttribute(Attribute.POSITION, "aPosition");
        addAttribute(Attribute.NORMAL, "aNormal");
        addAttribute(Attribute.TEXCOORD, "aTexCoord");
        addAttribute(Attribute.BATCHPOS, "aBatchPos");
        this.uWorldMatrixHandle = this.shader.getUniformHandle("uWorldMatrix");
        this.uWorldViewProjMatrixHandle = this.shader.getUniformHandle("uWorldViewProjMatrix");
        this.uCameraPositionWorldHandle = this.shader.getUniformHandle("uCameraPositionWorld");
        this.uTranslationsHandle = this.shader.getUniformHandle("uTranslations");
        GLES20.glUniform3fv(this.shader.getUniformHandle("uLightDirection"), 1, ViewConstants.LIGHT_DIRECTION, 0);
        GLES20.glUniform1f(this.shader.getUniformHandle("uAmbient"), 0.35F);
        GLES20.glUniform4fv(this.shader.getUniformHandle("uSpecular"), 1, ViewConstants.SPECULAR_COLOR, 0);
        GLES20.glUniform1f(this.shader.getUniformHandle("uSpecularPower"), 12.5F);
        GLES20.glUniform1f(this.shader.getUniformHandle("uReflectionStrength"), 0.3F);
    }

    public void updateBatch(int paramInt, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2) {
        this.batchCount = paramInt;
        this.uTranslations = paramArrayOfFloat1;
    }

    public void updateUniforms(float[] paramArrayOfFloat, RenderParams paramRenderParams) {
        GLES20.glUniformMatrix4fv(this.uWorldMatrixHandle, 1, false, paramArrayOfFloat, 0);
        Matrix.multiplyMM(this.uWorldViewProjMatrix, 0, paramRenderParams.viewProjMatrix, 0, paramArrayOfFloat, 0);
        GLES20.glUniformMatrix4fv(this.uWorldViewProjMatrixHandle, 1, false, this.uWorldViewProjMatrix, 0);
        GLES20.glUniform4fv(this.uCameraPositionWorldHandle, 1, paramRenderParams.cameraPositionWorld, 0);
        GLES20.glUniform3fv(this.uTranslationsHandle, this.batchCount, this.uTranslations, 0);
    }
}
