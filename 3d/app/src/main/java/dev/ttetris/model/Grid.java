package dev.ttetris.model;

import dev.ttetris.StarGLSurfaceView;
import dev.ttetris.util.MatrixState;
import dev.ttetris.util.Shader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class Grid {
	private float [] vertices;
	private short[][] indices;
	private FloatBuffer vertexBuffer;
	private ShortBuffer[] indexBuffer;
	private int N;
    public float xAngle = 0f;

	public Grid(StarGLSurfaceView mv, int n) {
		N = n + 1;
		vertices = new float[3 * N * N];
		indices = new short[N][N];
		indexBuffer = new ShortBuffer[2 * N];

		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();

		int x = 0;
		short y = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				vertices[x++] = j;
				vertices[x++] = i;
				vertices[x++] = 0;
				indices[i][j] = y++;
			}
			addIndexToBuffer(i, i);     // added the first half
		}
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) 
				indices[i][j] = (short) (i + N * j);
			addIndexToBuffer(i + N, i); // added the second half
		}
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
        colorBuffer = getDirectBuffer(colors);
        initShader(mv);
	}
	
	private void addIndexToBuffer(int bufferPointer, int arrayPointer) {
		ByteBuffer ibb = ByteBuffer.allocateDirect(N * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer[bufferPointer] = ibb.asShortBuffer();
		indexBuffer[bufferPointer].put(indices[arrayPointer]);
		indexBuffer[bufferPointer].position(0);
	}

	float colors[] = { 1f, 1f, 0f, 1.0f};  // yellow
    public FloatBuffer getDirectBuffer(float[] buffer) { 
        ByteBuffer bb = ByteBuffer.allocateDirect(buffer.length * 4); 
        bb.order(ByteOrder.nativeOrder()); 
        FloatBuffer directBuffer = bb.asFloatBuffer(); 
        directBuffer.put(buffer); 
        directBuffer.position(0); 
        return directBuffer; 
    }
	private FloatBuffer colorBuffer;
    
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
    
	public void drawSelf(){
		GLES20.glUseProgram(mProgram);
		Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);          // 初始化变换矩阵
		//Matrix.translateM(mMMatrix, 0, -2.5f, 2.5f, -4.5f); // 设置沿Z轴正向位移1
        Matrix.rotateM(mMMatrix, 0, xAngle, 0, 0, 1);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, Grid.getFinalMatrix(mMMatrix), 0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glLineWidth(3.0f);
        for (int i = 0; i < 2 * N; i++) {
            GLES20.glDrawElements(GLES20.GL_LINE_STRIP, N, GLES20.GL_UNSIGNED_SHORT, indexBuffer[i]);
        }
	}
}
