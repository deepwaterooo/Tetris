package ru.igsoft.tetris.view.material;

import ru.igsoft.anogl.AssetManager;
import ru.igsoft.anogl.Attribute;
import ru.igsoft.anogl.DepthTest;
import ru.igsoft.anogl.Material;

public class BackgroundMaterial extends Material
{
  public BackgroundMaterial(AssetManager paramAssetManager, String paramString)
  {
    super(paramAssetManager.getShader("Shaders/Background.glsl"));
    addTexture(paramAssetManager.getTexture(paramString), "sBaseMap");
    addAttribute(Attribute.POSITION, "aPosition");
    this.depthTest = DepthTest.NONE;
    this.depthMask = false;
  }
}

/* Location:           /home/jenny/android/dex2jar-0.0.9.15/classes_dex2jar.jar
 * Qualified Name:     ru.igsoft.tetris.view.material.BackgroundMaterial
 * JD-Core Version:    0.6.2
 */