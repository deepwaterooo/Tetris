package dev.ttetris.util;

import dev.ttetris.util.Attribute;
import dev.ttetris.util.Shader;
import android.opengl.GLES20;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public abstract class Material {
    private static final String TAG = "Material";
    private List<Integer> attributeHandles;
    private List<Attribute> attributes;
    protected int batchCount;
    private boolean batchRendering;
    //protected BlendFunc blendFunc = BlendFunc.NONE;
    //protected CullMode cullMode = CullMode.BACK;
    protected boolean depthMask = true;
    protected DepthTest depthTest = DepthTest.LESS;  // I think I need this one
    protected final Shader shader;
    private List<Integer> textureHandles;
    private List<TextureIndex> textureIndices;
    private List<Texture> textures;

    public Material(Shader paramShader) {
        this.shader = paramShader;
        paramShader.useProgram();
    }

    protected final void addAttribute(Attribute paramAttribute, String paramString) {
        if (this.attributes == null) {
            this.attributes = new ArrayList();
            this.attributeHandles = new ArrayList();
        }
        this.attributes.add(paramAttribute);
        //this.attributeHandles.add(Integer.valueOf(this.shader.getAttributeHandle(paramString)));
        if (paramAttribute == Attribute.BATCHPOS)
            this.batchRendering = true;
    }

    private void bindTextures() {
        //int i = this.textures.size();
        int i = 2;
        for (int j = 0; ; j++) {
            if (j >= i)
                return;
            //Texture localTexture = (Texture)this.textures.get(j);
            //TextureIndex localTextureIndex = (TextureIndex)this.textureIndices.get(j);
            //GLES20.glActiveTexture(localTextureIndex.getValue());
            //GLES20.glBindTexture(localTexture.getTarget(), localTexture.getTextureId());
            //GLES20.glUniform1i(((Integer)this.textureHandles.get(j)).intValue(), localTextureIndex.ordinal());
        }
    }
    /*
      private void disableAttributes() {
      int i = this.attributes.size();
      for (int j = 0; ; j++) {
      if (j >= i)
      return;
      GLES20.glDisableVertexAttribArray(((Integer)this.attributeHandles.get(j)).intValue());
      }
      }

      private void enableAttributes(Mesh paramMesh) {
      int i = this.attributes.size();
      for (int j = 0; ; j++) {
      if (j >= i)
      return;
      int k = ((Integer)this.attributeHandles.get(j)).intValue();
      paramMesh.bindAttribute((Attribute)this.attributes.get(j), k);
      GLES20.glEnableVertexAttribArray(k);
      }
      }
    */
    private void prepareState() { /*
                                    if (this.cullMode != CullMode.NONE) {
                                    GLES20.glCullFace(this.cullMode.getValue());
                                    GLES20.glEnable(2884);
                                    if (this.depthTest == DepthTest.NONE)
                                    break label102;
                                    GLES20.glDepthFunc(this.depthTest.getValue());
                                    GLES20.glEnable(2929);
                                    }  */
        while (true) {
            GLES20.glDepthMask(this.depthMask); /*
                                                  if (this.blendFunc == BlendFunc.NONE)
                                                  break label111;
                                                  GLES20.glDisable(3042);
                                                  GLES20.glBlendFunc(this.blendFunc.sfactor, this.blendFunc.dfactor); */
            GLES20.glEnable(3042);
            //return;
            GLES20.glDisable(2884);
            //break;
            label102: GLES20.glDisable(2929);
        }
        //label111:
        //GLES20.glDisable(3042);
    }

    protected final void addTexture(Texture paramTexture, String paramString) {
        //protected final void addTexture(Texture paramTexture, String paramString) {
        if (this.textures == null) {
            this.textures = new ArrayList();
            this.textureIndices = new ArrayList();
            this.textureHandles = new ArrayList();
        }
        TextureIndex localTextureIndex = TextureIndex.valueOf("TEXTURE" + this.textures.size());
        if (localTextureIndex == null)
            throw new RuntimeException("Texture attribute is out of range");
        this.textures.add(paramTexture);
        //this.textureIndices.add(localTextureIndex);
        this.textureHandles.add(Integer.valueOf(this.shader.getUniformHandle(paramString)));
    }  
    /*
      public CullMode getCullMode() {
      return this.cullMode;
      }

      public void setCullMode(CullMode paramCullMode) {
      this.cullMode = paramCullMode;
      }

      public DepthTest getDepthTest() {
      return this.depthTest;
      }

      public boolean isDepthMask() {
      return this.depthMask;
      }
    */
    /*
      public BlendFunc getBlendFunc() {
      return this.blendFunc;
      }

      public boolean isTransparent() {
      return this.blendFunc != BlendFunc.NONE;
      }

      public void logProgramParams() {
      int i = 0;
      AttributInfo[] arrayOfAttributInfo = this.shader.getAttributes();
      int m;
      UniformInfo[] arrayOfUniformInfo;
      int j;
      if (arrayOfAttributInfo.length > 0) {
      Log.i("Material", "Attributes:");
      int k = arrayOfAttributInfo.length;
      m = 0;
      if (m < k);
      }
      else {
      arrayOfUniformInfo = this.shader.getUniforms();
      if (arrayOfUniformInfo.length > 0) {
      Log.i("Material", "Uniforms:");
      j = arrayOfUniformInfo.length;
      }
      }
      while (true) {
      if (i >= j) {
      return;
      Log.i("Material", arrayOfAttributInfo[m].toString());
      m++;
      break;
      }
      Log.i("Material", arrayOfUniformInfo[i].toString());
      i++;
      }
      }
    */
    public final void render(Mesh paramMesh, float[] paramArrayOfFloat, RenderParams paramRenderParams) {
        try {
            if (this.batchRendering) {
                if (this.batchCount > 0);
            }
            else if (this.batchCount > 0)
                throw new RuntimeException("Batch rendering can not be used without specifying Attribute.BATCHPOS");
        }
        catch (RuntimeException localRuntimeException) {
            String str = "[" + getClass().getSimpleName() + "] " + localRuntimeException.getMessage();
            if (localRuntimeException.getCause() != null) {
                //throw new RuntimeException(str, localRuntimeException.getCause());
                if (paramRenderParams.prevMaterial != this) {
                    //if (!this.shader.useProgram())
                    //  return;
                    prepareState();
                    if (this.textures != null)
                        bindTextures();
                    //if (this.attributes != null)
                    //  enableAttributes(paramMesh);
                }
                updateUniforms(paramArrayOfFloat, paramRenderParams);
                if (this.batchRendering)
                    paramMesh.draw(this.batchCount);
                while (true) {
                    paramRenderParams.prevMaterial = this;
                    //return;
                    paramMesh.draw();
                }
            }
            else {
                throw new RuntimeException(str);
            }
        }
    } 
    /*
      public void setBlendFunc(BlendFunc paramBlendFunc) {
      this.blendFunc = paramBlendFunc;
      }
    */
    public void setDepthMask(boolean paramBoolean) {
        this.depthMask = paramBoolean;
    }
    /*
      public void setDepthTest(DepthTest paramDepthTest) {
      this.depthTest = paramDepthTest;
      }
    */    

      protected void updateUniforms(float[] paramArrayOfFloat, RenderParams paramRenderParams) {
      }
}
