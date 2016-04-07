package ru.igsoft.tetris.view.material;

import android.opengl.GLES20;
import android.opengl.Matrix;
import ru.igsoft.anogl.AssetManager;
import ru.igsoft.anogl.Attribute;
import ru.igsoft.anogl.Material;
import ru.igsoft.anogl.RenderParams;
import ru.igsoft.anogl.Shader;
import ru.igsoft.tetris.view.BrickBatch;
import ru.igsoft.tetris.view.ViewConstants;

public class BrickBatchMaterial extends Material
  implements BrickBatch
{
  private int uCameraPositionWorldHandle;
  private float[] uTranslations;
  private int uTranslationsHandle;
  private int uWorldMatrixHandle;
  private float[] uWorldViewProjMatrix = new float[16];
  private int uWorldViewProjMatrixHandle;

  public BrickBatchMaterial(AssetManager paramAssetManager, String paramString)
  {
    super(paramAssetManager.getShader("Shaders/BrickBatch.glsl"));
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

  public void updateBatch(int paramInt, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    this.batchCount = paramInt;
    this.uTranslations = paramArrayOfFloat1;
  }

  public void updateUniforms(float[] paramArrayOfFloat, RenderParams paramRenderParams)
  {
    GLES20.glUniformMatrix4fv(this.uWorldMatrixHandle, 1, false, paramArrayOfFloat, 0);
    Matrix.multiplyMM(this.uWorldViewProjMatrix, 0, paramRenderParams.viewProjMatrix, 0, paramArrayOfFloat, 0);
    GLES20.glUniformMatrix4fv(this.uWorldViewProjMatrixHandle, 1, false, this.uWorldViewProjMatrix, 0);
    GLES20.glUniform4fv(this.uCameraPositionWorldHandle, 1, paramRenderParams.cameraPositionWorld, 0);
    GLES20.glUniform3fv(this.uTranslationsHandle, this.batchCount, this.uTranslations, 0);
  }
}

/* Location:           /home/jenny/android/dex2jar-0.0.9.15/classes_dex2jar.jar
 * Qualified Name:     ru.igsoft.tetris.view.material.BrickBatchMaterial
 * JD-Core Version:    0.6.2
 */