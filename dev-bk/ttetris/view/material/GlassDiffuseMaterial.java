package ru.igsoft.tetris.view.material;

import android.opengl.GLES20;
import android.opengl.Matrix;
import ru.igsoft.anogl.AssetManager;
import ru.igsoft.anogl.Attribute;
import ru.igsoft.anogl.BlendFunc;
import ru.igsoft.anogl.Material;
import ru.igsoft.anogl.RenderParams;
import ru.igsoft.anogl.Shader;
import ru.igsoft.tetris.view.ViewConstants;

public class GlassDiffuseMaterial extends Material
{
  private int uViewMatrixHandle;
  private float[] uWorldViewInvMatrix = new float[16];
  private int uWorldViewInvMatrixHandle;
  private float[] uWorldViewMatrix = new float[16];
  private float[] uWorldViewProjMatrix = new float[16];
  private int uWorldViewProjMatrixHandle;

  public GlassDiffuseMaterial(AssetManager paramAssetManager)
  {
    super(paramAssetManager.getShader("Shaders/GlassDiffuse.glsl"));
    addTexture(paramAssetManager.getTexture("Textures/Glass.png"), "sGlassMap");
    addAttribute(Attribute.POSITION, "aPosition");
    addAttribute(Attribute.NORMAL, "aNormal");
    addAttribute(Attribute.TANGENT, "aTangent");
    addAttribute(Attribute.TEXCOORD, "aTexCoord");
    this.uViewMatrixHandle = this.shader.getUniformHandle("uViewMatrix");
    this.uWorldViewInvMatrixHandle = this.shader.getUniformHandle("uWorldViewInvMatrix");
    this.uWorldViewProjMatrixHandle = this.shader.getUniformHandle("uWorldViewProjMatrix");
    GLES20.glUniform3fv(this.shader.getUniformHandle("uLightDirection"), 1, ViewConstants.LIGHT_DIRECTION, 0);
    GLES20.glUniform4fv(this.shader.getUniformHandle("uDiffuse"), 1, ViewConstants.GLASS_COLOR, 0);
    this.blendFunc = BlendFunc.MULTIPLY;
    this.depthMask = false;
  }

  protected void updateUniforms(float[] paramArrayOfFloat, RenderParams paramRenderParams)
  {
    GLES20.glUniformMatrix4fv(this.uViewMatrixHandle, 1, false, paramRenderParams.viewMatrix, 0);
    Matrix.multiplyMM(this.uWorldViewMatrix, 0, paramRenderParams.viewMatrix, 0, paramArrayOfFloat, 0);
    Matrix.invertM(this.uWorldViewInvMatrix, 0, this.uWorldViewMatrix, 0);
    GLES20.glUniformMatrix4fv(this.uWorldViewInvMatrixHandle, 1, false, this.uWorldViewInvMatrix, 0);
    Matrix.multiplyMM(this.uWorldViewProjMatrix, 0, paramRenderParams.viewProjMatrix, 0, paramArrayOfFloat, 0);
    GLES20.glUniformMatrix4fv(this.uWorldViewProjMatrixHandle, 1, false, this.uWorldViewProjMatrix, 0);
  }
}

/* Location:           /home/jenny/android/dex2jar-0.0.9.15/classes_dex2jar.jar
 * Qualified Name:     ru.igsoft.tetris.view.material.GlassDiffuseMaterial
 * JD-Core Version:    0.6.2
 */