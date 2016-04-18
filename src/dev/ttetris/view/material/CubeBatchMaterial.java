package dev.ttetris.view.material;

import android.opengl.GLES20;
import android.opengl.Matrix;
import dev.ttetris.view.ViewConstants;
import dev.ttetris.view.CubeBatch;
import dev.ttetris.util.AssetManager;
import dev.ttetris.util.Attribute;
import dev.ttetris.util.Material;
import dev.ttetris.util.RenderParams;
import dev.ttetris.util.Shader;

public class CubeBatchMaterial extends Material implements CubeBatch {
    private int batchCount;

    private int uCameraPositionWorldHandle;
    private int uWorldMatrixHandle;

    private int uTranslationsHandle;
    private float[] uTranslations;

    private int uWorldViewProjMatrixHandle;
    private float[] uWorldViewProjMatrix = new float[16];

    private final String vertexShaderCode =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "attribute vec4 vColor;" +
        "varying vec4 _vColor;" +
        "void main() {" +
        "  _vColor = vColor;" + 
        "  gl_Position = uMVPMatrix * vPosition;" +
        "}";
    private final String fragmentShaderCode =
        "precision mediump float;" +
        "varying vec4 _vColor;" +
        "void main() {" +
        "  gl_FragColor = _vColor;" +
        "}";

    public CubeBatchMaterial(AssetManager paramAssetManager, String paramString) {
        super(paramAssetManager.getShader("Shaders/CubeBatch.glsl"));
        addTexture(paramAssetManager.getTexture(paramString), "sBaseMap");
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
    }

    public void updateUniforms(float[] paramArrayOfFloat, RenderParams paramRenderParams) {
        GLES20.glUniformMatrix4fv(this.uWorldMatrixHandle, 1, false, paramArrayOfFloat, 0);
        Matrix.multiplyMM(this.uWorldViewProjMatrix, 0, paramRenderParams.viewProjMatrix, 0, paramArrayOfFloat, 0);
        GLES20.glUniformMatrix4fv(this.uWorldViewProjMatrixHandle, 1, false, this.uWorldViewProjMatrix, 0);
        GLES20.glUniform4fv(this.uCameraPositionWorldHandle, 1, paramRenderParams.cameraPositionWorld, 0);
        GLES20.glUniform3fv(this.uTranslationsHandle, this.batchCount, this.uTranslations, 0);
    } 

    public void updateBatch(int paramInt, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2) {
        this.batchCount = paramInt;
        this.uTranslations = paramArrayOfFloat1;
    }
}
