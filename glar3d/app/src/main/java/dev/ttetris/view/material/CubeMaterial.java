package dev.ttetris.view.material;

import android.opengl.GLES20;
import android.opengl.Matrix;
import dev.ttetris.util.AssetManager;
import dev.ttetris.util.Attribute;
import dev.ttetris.util.Material;
import dev.ttetris.util.RenderParams;
import dev.ttetris.util.Shader;
import dev.ttetris.view.ViewConstants;

public class CubeMaterial extends Material {
    private int uEyePositionHandle;
    private int uViewMatrixHandle;
    private float[] uWorldViewMatrix = new float[16];
    private int uWorldViewMatrixHandle;
    private float[] uWorldViewProjMatrix = new float[16];
    private int uWorldViewProjMatrixHandle;

    public CubeMaterial(AssetManager paramAssetManager, String paramString) {
        super(paramAssetManager.getShader("Shaders/Brick.glsl"));
        addTexture(paramAssetManager.getTexture(paramString), "sBaseMap");
        addAttribute(Attribute.POSITION, "aPosition");
        addAttribute(Attribute.NORMAL, "aNormal");
        addAttribute(Attribute.TEXCOORD, "aTexCoord");
        this.uViewMatrixHandle = this.shader.getUniformHandle("uViewMatrix");
        this.uWorldViewMatrixHandle = this.shader.getUniformHandle("uWorldViewMatrix");
        this.uWorldViewProjMatrixHandle = this.shader.getUniformHandle("uWorldViewProjMatrix");
        this.uEyePositionHandle = this.shader.getUniformHandle("uEyePosition");
        GLES20.glUniform3fv(this.shader.getUniformHandle("uLightDirection"), 1, ViewConstants.LIGHT_DIRECTION, 0);
        GLES20.glUniform1f(this.shader.getUniformHandle("uAmbient"), 0.35F);
        GLES20.glUniform4fv(this.shader.getUniformHandle("uSpecular"), 1, ViewConstants.SPECULAR_COLOR, 0);
        GLES20.glUniform1f(this.shader.getUniformHandle("uSpecularPower"), 12.5F);
    }

    public void updateUniforms(float[] paramArrayOfFloat, RenderParams paramRenderParams) {
        GLES20.glUniformMatrix4fv(this.uViewMatrixHandle, 1, false, paramRenderParams.viewMatrix, 0);
        Matrix.multiplyMM(this.uWorldViewMatrix, 0, paramRenderParams.viewMatrix, 0, paramArrayOfFloat, 0);
        GLES20.glUniformMatrix4fv(this.uWorldViewMatrixHandle, 1, false, this.uWorldViewMatrix, 0);
        Matrix.multiplyMM(this.uWorldViewProjMatrix, 0, paramRenderParams.viewProjMatrix, 0, paramArrayOfFloat, 0);
        GLES20.glUniformMatrix4fv(this.uWorldViewProjMatrixHandle, 1, false, this.uWorldViewProjMatrix, 0);
        GLES20.glUniform4fv(this.uEyePositionHandle, 1, paramRenderParams.cameraPositionView, 0);
    }
}
