package dev.ttetris.model;

import dev.ttetris.model.CubeColor;
import dev.ttetris.util.VertexArray;
import dev.ttetris.shader.TextureShaderProgram;
import dev.ttetris.shader.Shader;		 
import dev.ttetris.util.ShaderHelper;		
import java.io.Serializable;
import java.nio.ShortBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;		
import android.content.Context;		
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.opengl.GLUtils;
import android.opengl.GLES11Ext;

public class Cube implements Cloneable, Comparable<Cube>, Serializable {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 2;
	public static float[] mProjMatrix = new float[16];
	public static float[] mMVPMatrix = new float[16];
	public static float[] mVMatrix = new float[16];
    public static float[] mMMatrix = new float[16];
    public float xAngle = 0f; // yAngle zAngle
    private final float size = 0.5f; 
    private final float one = 0.9f;
    private float[] texCoords = new float[] { one, 0.1f, 0.1f, 0.1f, 0.1f, one, one, one, 0.1f, 0.1f, 0.1f, one, one, one, one, 0.1f,
                                              one, one, one, 0.1f, 0.1f, 0.1f, 0.1f, one, 0.1f, one, one, one, one, 0.1f, 0.1f, 0.1f,
                                              0.1f, 0.1f, 0.1f, one, one, one, one, 0.1f, one, 0.1f, 0.1f, 0.1f, 0.1f, one, one, one};  
    String mVertexShader;		
    String mFragmentShader;		
    int vertexShaderHandle;		
    int fragmentShaderHandle;
    int mProgram;		
 	int mPositionHandle;		
    int mTexCoordHandle;		
 	int mMVPMatrixHandle;		
    FloatBuffer mVertexBuffer;		
 	FloatBuffer mColorBuffer;
    ShortBuffer drawListBuffer;
    private CubeColor color; 
    public float [] coords;
    private float x;
    private float y;
    private float z;
    private int colorIdx;
    public CubeColor getColor() { return this.color; }
    public void setX(float paramFloat) { this.x = paramFloat; }
    public void setY(float paramFloat) { this.y = paramFloat; }
    public void setZ(float paramFloat) { this.z = paramFloat; }
    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getZ() { return this.z; }

    public Cube(Context context, CubeColor paramCubeColor, int paramInt1, int paramInt2, int paramInt3) {
        this.color = paramCubeColor;
        this.x = paramInt1;
        this.y = paramInt2;
        this.z = paramInt3;
        coords = new float[72]; 
        setCoordinates();
        initVertexData();
        initShader(context);
    }

    public Cube(CubeColor paramCubeColor, int paramInt1, int paramInt2, int paramInt3) {
        this.color = paramCubeColor;
        this.x = paramInt1;
        this.y = paramInt2;
        this.z = paramInt3;
        coords = new float[72]; 
        setCoordinates();
        initVertexData(); 
    }
    
    public void setCoordinates() { 
        float [] res = {x-size, y+size, z-size, // 0 
                        x+size, y+size, z-size, // 1
                        x+size, y+size, z+size, // 2
                        x-size, y+size, z+size, // 3
                        x-size, y-size, z-size, // 4
                        x+size, y-size, z-size, // 7
                        x+size, y-size, z+size, // 6
                        x-size, y-size, z+size};// 5
        float [] fin = new float[72];
        int j = 0;
        for (int i = 0; i < drawOrder0.length; i++) {
            fin[j++] = res[drawOrder0[i] * 3];
            fin[j++] = res[drawOrder0[i] * 3 + 1];
            fin[j++] = res[drawOrder0[i] * 3 + 2];
        }
        this.coords = fin;
    }

    private boolean isActiveFlag; // for activeBlock
    public void setActiveFlag(boolean v) { this.isActiveFlag = v; }
    public boolean getActiveFlag() { return this.isActiveFlag; }
    private static final short drawOrder[] = {0, 1, 3, 2, 4, 5, 7, 6, 8, 9, 11, 10, 12, 13, 15, 14, 16, 17, 19, 18, 20, 21, 23, 22};
    private static final short drawOrder0[] = {0, 1, 2, 3, 4, 5, 6, 7, 7, 6, 2, 3, 4, 5, 1, 0, 5, 1, 2, 6, 4, 0, 3, 7}; // 前后上下右左
    private VertexArray verticeArr;
    private VertexArray textureArr;

    public void draw(int texId) { 
        initVertexData();
        
		Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);           // 初始化变换矩阵
        Matrix.translateM(mMMatrix, -0, -2.5f, -2.5f, -5f);

        if (getActiveFlag()) {
            if (Model.isFrameZRotating[0]) {       // anti-
                Matrix.rotateM(mMMatrix, 0, xAngle, 0, 0, 1);
            } else if (Model.isFrameZRotating[1]) { // clock-wise
                Matrix.rotateM(mMMatrix, 0, -xAngle, 0, 0, 1);
            } else if (Model.isFrameXRotating[0]) {                // minor bug here for X rotating
                Matrix.translateM(mMMatrix, 0, -2.5f, -2.5f, -5f);  //2.5 5
                Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);
                Matrix.translateM(mMMatrix, 0, 2.5f, 2.5f, 5f);
            } else if (Model.isFrameXRotating[1]) {
                Matrix.translateM(mMMatrix, 0, -2.5f, -2.5f, -5f);
                Matrix.rotateM(mMMatrix, 0, -xAngle, 1, 0, 0);
                Matrix.translateM(mMMatrix, 0, 2.5f, 2.5f, 5f);
            }
            Matrix.translateM(mMMatrix, 0, 2f, 2f, 4.5f);
        } else 
            Matrix.translateM(mMMatrix, 0, 0.5f, 0.5f, 0.5f);    
        
        Matrix.translateM(mMMatrix, 0, -0.5f, -0.5f, -0.5f);
        if (Model.isFrameZRotating[0]) {       // anti-
            Matrix.rotateM(mMMatrix, 0, xAngle, 0, 0, 1);
        } else if (Model.isFrameZRotating[1]) { // clock-wise
            Matrix.rotateM(mMMatrix, 0, -xAngle, 0, 0, 1);
        } else if (Model.isFrameXRotating[0]) {
            Matrix.translateM(mMMatrix, 0, -2.5f, -2.5f, -5f);
            Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);
            Matrix.translateM(mMMatrix, 0, 2.5f, 2.5f, 5f);
        } else if (Model.isFrameXRotating[1]) {
            Matrix.translateM(mMMatrix, 0, -2.5f, -2.5f, -5f);
            Matrix.rotateM(mMMatrix, 0, -xAngle, 1, 0, 0);
            Matrix.translateM(mMMatrix, 0, 2.5f, 2.5f, 5f);
        }
        Matrix.translateM(mMMatrix, 0, 0.5f, 0.5f, 0.5f);   
        
        GLES20.glUseProgram(mProgram); // 绘制时使用着色程序		
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, Cube.getFinalMatrix(mMMatrix), 0);        // 将最终的变换矩阵传入渲染管线

        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer); // 将顶点位置数据传入渲染管线
        GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, mColorBuffer);  // 将顶点纹理数据传入渲染管线
        // 允许用到的属性数据数组
        GLES20.glEnableVertexAttribArray(mPositionHandle); // 启用顶点位置数据
        GLES20.glEnableVertexAttribArray(mTexCoordHandle); // 启用顶点纹理坐标数据

        // 绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);	       // 设置使用的纹理编号
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId); // 绑定指定的纹理 ID

        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(mPositionHandle);		
        GLES20.glDisableVertexAttribArray(mTexCoordHandle);  
    }

    public void initShader(Context mv) {                           
 		mVertexShader = Shader.loadFromAssetsFile("vertex.sh", mv.getResources()); // 加载顶点着色器的脚本内容
 		mFragmentShader = Shader.loadFromAssetsFile("frag.sh", mv.getResources()); // 加载片元着色器的脚本内容
        vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, mVertexShader);		
        fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShader);		
        // 基本顶点着色器与片元着色器创建程序
        mProgram = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,		
                                                     new String[]{"texture", "vPosition", "vTexCoordinate", "uMVPMatrix"});		
 		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");      // 获取程序中顶点位置属性引用
        mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "vTexCoordinate"); // 获取程序中顶点纹理坐标属性引用
 		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");   // 获取程序中总变换矩阵引用
 	}    

    public void setColor(CubeColor color) {
        this.color = color;
        switch(color) {
        case Amethyst:    this.colorIdx = 0; return;
        case Anchient:    this.colorIdx = 1; return;
        case Brass:       this.colorIdx = 2; return;
        case LapisLazuli: this.colorIdx = 3; return;
        case Marble:      this.colorIdx = 4; return;
        case MarbleRough: this.colorIdx = 5; return;
        case Oak:         this.colorIdx = 6; return;
        case WhiteMarble: this.colorIdx = 7; return;
        }
    }

    public void initVertexData() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(coords.length * 4); 
 		vbb.order(ByteOrder.nativeOrder());		
 		mVertexBuffer = vbb.asFloatBuffer();		
 		mVertexBuffer.put(coords);		
 		mVertexBuffer.position(0);
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoords.length * 4);		
 		cbb.order(ByteOrder.nativeOrder());		
 		mColorBuffer = cbb.asFloatBuffer();		
 		mColorBuffer.put(texCoords);		
 		mColorBuffer.position(0);
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }
    
    public static float[] getFinalMatrix(float [] spec) {
		mMVPMatrix = new float[16];
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0); 
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
		return mMVPMatrix;
	}

    public Cube clone() {
        try {
            Cube localCube = (Cube)super.clone();
            return localCube;
        } catch (CloneNotSupportedException localCloneNotSupportedException) {
        }
        return null;
    }
    public int compareTo(Cube paramCube) { return Math.abs(this.x - paramCube.x) < 0.00000001f ? 1 : 0; }    
}
