package dev.ttetris.model;

import dev.ttetris.StarGLSurfaceView;
import dev.ttetris.GLImage;
import dev.ttetris.model.CubeColor;
import dev.ttetris.util.MatrixState;
import dev.ttetris.util.Shader;
import dev.ttetris.util.ShaderHelper;
import java.io.Serializable;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.opengl.GLUtils;
import android.opengl.GLES11Ext;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Cube implements Cloneable, Comparable<Cube>, Serializable {
	public static float[] mVMatrix = new float[16];
	public static float[] mProjMatrix = new float[16];
	public static float[] mMVPMatrix = new float[16];
    public static float[] mMMatrix = new float[16]; // 具体物体的移动旋转矩阵，旋转、平移
    private static final int COORDS_PER_VERTEX = 3;
    private static final int VALUES_PER_COLOR = 4;
    //private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    //private final int COLOR_STRIDE = VALUES_PER_COLOR * 4;
    private ShortBuffer drawListBuffer;
	int mProgram;
	int mMVPMatrixHandle;
	int mPositionHandle;
	int mColorHandle;
    int mTextureCoordinateHandle;
	FloatBuffer mVertexBuffer;
	FloatBuffer mColorBuffer;
    private final float size = 0.5f; 
    public float xAngle = 0f;
    private float[] texCoords = new float[] { size, 0, 0, 0, 0, size, size, size, 0, 0, 0, size,
                                              size, size, size, 0, size, size, size, 0, 0, 0, 0, size,
                                              0, size, size, size, size, 0, 0, 0, 0, 0, 0, size,
                                              size, size, size, 0, size, 0, 0, 0, 0, size, size, size}; // 4 points per surface, 8 * 6 = 48
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

    public Cube(StarGLSurfaceView mv, CubeColor paramCubeColor, int paramInt1, int paramInt2, int paramInt3) {
        this.color = paramCubeColor;
        this.x = paramInt1;
        this.y = paramInt2;
        this.z = paramInt3;
        coords = new float[72]; 
        setCoordinates();
        initShader(mv);
    }

    private boolean isActiveFlag; // for activeBlock
    public void setActiveFlag(boolean v) { this.isActiveFlag = v; }
    public boolean getActiveFlag() { return this.isActiveFlag; }

	public void drawSelf() { // isActiveFlag -- activeBlock; Model.isFrameXRotating
        initVertexData();
        
		Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);           // 初始化变换矩阵
        if (getActiveFlag()) {
            if (Model.isFrameZRotating[0]) {       // anti-
                Matrix.rotateM(mMMatrix, 0, xAngle, 0, 0, 1);
            } else if (Model.isFrameZRotating[1]) { // clock-wise
                Matrix.rotateM(mMMatrix, 0, -xAngle, 0, 0, 1);
            } else if (Model.isFrameXRotating[0]) {                // minor bug here for X rotating
                Matrix.translateM(mMMatrix, 0, 0, -2.5f, -5f);
                Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);
                Matrix.translateM(mMMatrix, 0, 0, 2.5f, 5f);
            } else if (Model.isFrameXRotating[1]) {
                Matrix.translateM(mMMatrix, 0, 0, -2.5f, -5f);
                Matrix.rotateM(mMMatrix, 0, -xAngle, 1, 0, 0);
                Matrix.translateM(mMMatrix, 0, 0, 2.5f, 5f);
            }
            Matrix.translateM(mMMatrix, 0, 2f, 2f, 0.5f);
        } else 
            Matrix.translateM(mMMatrix, 0, 0.5f, 0.5f, 0.5f);    

        Matrix.translateM(mMMatrix, 0, -0.5f, -0.5f, -0.5f); 
        if (Model.isFrameZRotating[0]) {       // anti-
            Matrix.rotateM(mMMatrix, 0, xAngle, 0, 0, 1);
        } else if (Model.isFrameZRotating[1]) { // clock-wise
            Matrix.rotateM(mMMatrix, 0, -xAngle, 0, 0, 1);
        } else if (Model.isFrameXRotating[0]) {
            Matrix.translateM(mMMatrix, 0, 0, -2.5f, -5f);
            Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);
            Matrix.translateM(mMMatrix, 0, 0, 2.5f, 5f);
        } else if (Model.isFrameXRotating[1]) {
            Matrix.translateM(mMMatrix, 0, 0, -2.5f, -5f);
            Matrix.rotateM(mMMatrix, 0, -xAngle, 1, 0, 0);
            Matrix.translateM(mMMatrix, 0, 0, 2.5f, 5f);
        }
        Matrix.translateM(mMMatrix, 0, 0.5f, 0.5f, 0.5f);    

        //GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        //GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        
		GLES20.glUseProgram(mProgram); // 绘制时使用着色程序
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "texture"); // textureParamHandle, 返回一个于着色器程序中变量名为"texture"相关联的索引
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "vTexCoordinate");
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        GLES20.glEnableVertexAttribArray(mPositionHandle); 
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, mVertexBuffer); // 3

        GLES20.glBindTexture(GLES20.GL_TEXTURE0, texture[0]);
        //GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // 指定一个当前的textureParamHandle对象为一个全局的uniform 变量
        GLES20.glUniform1i(mColorHandle, 0); // textureParamHandle

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle); 
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 0, mColorBuffer); // 4 --> 2
        
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, Cube.getFinalMatrix(mMMatrix), 0); // MVP
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);        
	}

	String mVertexShader;
	String mFragmentShader;
    private int vertexShaderHandle;
    private int fragmentShaderHandle;
    
    public void initShader(StarGLSurfaceView mv) {                                // should I set it to be static ?
		mVertexShader = Shader.loadFromAssetsFile("vertex.sh", mv.getResources());
		mFragmentShader = Shader.loadFromAssetsFile("frag.sh", mv.getResources());		

        vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, mVertexShader);
        fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShader);
        mProgram = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                                                     new String[]{"texture", "vPosition", "vTexCoordinate", "uMVPMatrix"});
        loadTexture(0);
	}

    private int[] texture = new int[8];
    /*
    public void loadTexture(int i) {
        IntBuffer intBuffer = IntBuffer.allocate(1);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        GLES20.glGenTextures(1, intBuffer);
        texture[0] = intBuffer.get(0); // 1 2 3 4 5 6 7 8
        if (texture[i] == 0) Log.w("Cube: ", "Could not generate a new OpenGL texture object.");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
        GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, GLES20.GL_TRUE);   // ?
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, GLImage.bitmap[i], 0); // 128 * 128 RGB

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        //GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        } */

    public void loadTexture(int i) {
        //IntBuffer intBuffer = IntBuffer.allocate(8);
        //GLES20.glGenTextures(8, intBuffer);
        int [] textureId = new int[1];
        GLES20.glGenTextures(1, textureId, 0);

        //texture[i] = intBuffer.get(i); // 1 2 3 4 5 6 7 8
        //if (texture[i] == 0) Log.w("Cube: ", "Could not generate a new OpenGL texture object.");
        //        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[i]);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLImage.bitmap[0], 0);
        int width = GLImage.bitmap[0].getWidth();
        int height = GLImage.bitmap[0].getHeight();
        
        //GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, width, height, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, GLImage.bitmap[0]);
        /*         
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR); // LINEAR
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLImage.bitmap[i], 0); */
    } 

    public Cube(CubeColor paramCubeColor, int paramInt1, int paramInt2, int paramInt3) {
        this.color = paramCubeColor;
        this.x = paramInt1;
        this.y = paramInt2;
        this.z = paramInt3;
        coords = new float[72]; 
        setCoordinates();
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
    
    private static final float cubeColor[] = {1.0f, 1.0f, 0.0f, 1.0f};   // supposed to change
    private static final short drawOrder[] = {0, 1, 3, 2,  4, 5, 7, 6,  8, 9, 11, 10, 12, 13, 15, 14, 16, 17, 19, 18, 20, 21, 23, 22};
    private static final short drawOrder0[] = {4, 7, 6, 5, 0, 3, 2, 1, 3, 5, 6, 2, 0, 1, 7, 4 ,1, 2, 6, 7, 0, 4, 5, 3};
    public void setCoordinates() { 
        float [] res = {x-size, y+size, z-size, // 0 
                        x+size, y+size, z-size, // 1
                        x+size, y+size, z+size, // 2
                        x-size, y+size, z+size, // 3
                        x-size, y-size, z-size, // 4
                        x-size, y-size, z+size, // 5
                        x+size, y-size, z+size, // 6
                        x+size, y-size, z-size};
        float [] fin = new float[72];
        int j = 0;
        for (int i = 0; i < drawOrder0.length; i++) {
            fin[j++] = res[drawOrder0[i] * 3];
            fin[j++] = res[drawOrder0[i] * 3 + 1];
            fin[j++] = res[drawOrder0[i] * 3 + 2];
        }
        coords = fin;
    }

    public void initVertexData() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(coords.length * 4); // coords
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(coords);
		mVertexBuffer.position(0);
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

		ByteBuffer cbb = ByteBuffer.allocateDirect(texCoords.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put(texCoords);
		mColorBuffer.position(0);
    }

	public static float[] getFinalMatrix(float[] spec) {
		mMVPMatrix = new float[16];
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
		return mMVPMatrix;
	}
}
