package dev.ttetris.model;

import dev.ttetris.StarGLSurfaceView;
import dev.ttetris.util.MatrixState;
import dev.ttetris.util.Shader;
import java.io.Serializable;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import android.opengl.GLES20;
import android.opengl.Matrix;
import javax.microedition.khronos.opengles.GL10;

public class Frame { 
    private FloatBuffer vertexBuffer;	
	private ShortBuffer indexBuffer;
	private FloatBuffer colorBuffer;
	private float vertices[] = { 
        0.0f, 0.0f, 0.0f, // 0
        0.0f, 0.0f, 0.0f, // 1
        0.0f, 0.0f, 0.0f, // 2
        0.0f, 0.0f, 0.0f  // 3
    };
	private short[] indices = {0, 1, 0, 2, 0, 3};
	float colors[] = { 0.5f, 0.5f, 0.5f, 0.5f,
                       1f, 0f, 0f, 1f, // red   x
                       1f, 1f, 0f, 1f, // yellow y
                       0f, 0f, 1f, 1f  // blue  z
    };
    public float xAngle = 0f;
    
	public Frame(StarGLSurfaceView mv) {		
		vertices[3] = 5.0f;
		vertices[7] = 5.0f;
		vertices[11] = 5.0f;
		init();
        initShader(mv);
	}
	public Frame(StarGLSurfaceView mv, int n, int h){		
		vertices[3] = n;
		vertices[7] = n;
		vertices[11] = h;
		init();
        initShader(mv);
	}

	public void drawSelf(){
		GLES20.glUseProgram(mProgram);
		Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);          // 初始化变换矩阵

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
        
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, Frame.getFinalMatrix(mMMatrix), 0); 
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glLineWidth(3.0f);
        GLES20.glDrawElements(GLES20.GL_LINES, indices.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	}
    
    private static final long serialVersionUID = 6144113039836213006L;
    private static final int COORDS_PER_VERTEX = 3;
    private static final int VALUES_PER_COLOR = 4;
    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private final int COLOR_STRIDE = VALUES_PER_COLOR * 4;
    private ShortBuffer drawListBuffer;
	int mProgram;
	int mMVPMatrixHandle;
	int mPositionHandle;
	int mColorHandle;
	String mVertexShader;
	String mFragmentShader;
	FloatBuffer mVertexBuffer;
	FloatBuffer mColorBuffer;
    static float[] mMMatrix = new float[16]; // 具体物体的移动旋转矩阵，旋转、平移
	public static float[] mVMatrix = new float[16];
	public static float[] mProjMatrix = new float[16];
	public static float[] mMVPMatrix = new float[16];
	public static float[] getFinalMatrix(float[] spec) {
		mMVPMatrix = new float[16];
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
		return mMVPMatrix;
	}
    
    public void initShader(StarGLSurfaceView mv){
		mVertexShader = Shader.loadFromAssetsFile("vertex.sh", mv.getResources());
		mFragmentShader = Shader.loadFromAssetsFile("frag.sh", mv.getResources());		
		mProgram = Shader.createProgram(mVertexShader, mFragmentShader);
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	}
    
	private void init(){
		vertexBuffer = getDirectBuffer(vertices);
		indexBuffer = getDirectBuffer(indices);
		colorBuffer = getDirectBuffer(colors);
	}
    
    public FloatBuffer getDirectBuffer(float[] buffer) { 
        ByteBuffer bb = ByteBuffer.allocateDirect(buffer.length * 4); 
        bb.order(ByteOrder.nativeOrder()); 
        FloatBuffer directBuffer = bb.asFloatBuffer(); 
        directBuffer.put(buffer); 
        directBuffer.position(0); 
        return directBuffer; 
    }
    
    public ShortBuffer getDirectBuffer(short[] buffer) { 
        ByteBuffer bb = ByteBuffer.allocateDirect(buffer.length * 2); 
        bb.order(ByteOrder.nativeOrder()); 
        ShortBuffer directBuffer = bb.asShortBuffer(); 
        directBuffer.put(buffer); 
        directBuffer.position(0); 
        return directBuffer; 
    }
} 
