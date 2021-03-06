package dev.ttetris.model;

//import dev.ttetris.StarGLSurfaceView;
import dev.ttetris.util.MatrixState;
import dev.ttetris.util.Shader;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class Cube implements Cloneable, Comparable<Cube>, Serializable {
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
    static float[] mMMatrix = new float[16];

    private CubeColor color; 
    private float [] coords;
    private int x;
    private int y;
    private int z;
    private int rx;
    private int ry;
    private int rz;
     
    public Cube(CubeColor paramCubeColor, int paramInt1, int paramInt2, int paramInt3) {
        this(paramCubeColor, paramInt1, paramInt2, paramInt3, 0, 0, 0);
    }

    public Cube(CubeColor paramCubeColor, int paramInt1, int paramInt2, int paramInt3,
                int paramInt4, int paramInt5, int paramInt6) {
        this.color = paramCubeColor;
        this.x = paramInt1;
        this.y = paramInt2;
        this.z = paramInt3;
        this.rx = paramInt4;
        this.ry = paramInt5;
        this.rz = paramInt6;
    }
    /*
    public Cube(StarGLSurfaceView mv, float size, int paramInt1, int paramInt2, int paramInt3,
                int paramInt4, int paramInt5, int paramInt6) {
        this.color = paramCubeColor;
        this.x = paramInt1;
        this.y = paramInt2;
        this.z = paramInt3;
        this.rx = paramInt4;
        this.ry = paramInt5;
        this.rz = paramInt6;
    }
    */
    public Cube clone() {
        try {
            Cube localCube = (Cube)super.clone();
            return localCube;
        } catch (CloneNotSupportedException localCloneNotSupportedException) {
        }
        return null;
    }
    /*
    public void setCubeCoordinates() { 
        float [] res = {
            x-size, y-size, z-size, // 0
            x+size, y-size, z-size,  // 1
            x+size, y+size, z-size,   // 2
            x-size, y+size, z-size,  // 3
            x-size, y-size, z+size,  // 4
            x+size, y-size, z+size,   // 5
            x+size, y+size, z+size,    // 6
            x-size, y+size, z+size    // 7
        };
        coords = res;
    }
    
    public float[] getCubeCoordinates() {
        return coords;
    }
    */
    public int compareTo(Cube paramCube) {  // don't think this method is complete
        return Math.abs(this.x - paramCube.x) < 0.00000001f ? 1 : 0;
    }

    public CubeColor getColor() { return this.color; }
    public void setX(int paramInt) { this.x = paramInt; }
    public void setY(int paramInt) { this.y = paramInt; }
    public void setZ(int paramInt) { this.z = paramInt; }
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getZ() { return this.z; }

    public void setRx(int paramInt) { this.rx = paramInt; }
    public void setRy(int paramInt) { this.ry = paramInt; }
    public void setRz(int paramInt) { this.rz = paramInt; }
    public int getRx() { return this.rx; }
    public int getRy() { return this.ry; }
    public int getRz() { return this.rz; }

    /*
    public void setSize(float size) {
        this.size = size;
        } */
    
    private static final float cubeColor[] = {1.0f, 1.0f, 1.0f, 1.0f};
    private static final short drawOrder[] = { // for cubes
        0, 1, 2, 3, 0, 4, 5, 1,
        1, 2, 6, 5, 5, 6, 7, 4,
        7, 6, 2, 3, 3, 7, 4, 0};
    
    private void initVertexData() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(coords.length*4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(coords);
		mVertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

		ByteBuffer cbb = ByteBuffer.allocateDirect(cubeColor.length*4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put(cubeColor);
		mColorBuffer.position(0);
    }
    /*    
    public void initShader(StarGLSurfaceView mv){
		mVertexShader = Shader.loadFromAssetsFile("vertex.sh", mv.getResources());
		mFragmentShader = Shader.loadFromAssetsFile("frag.sh", mv.getResources());		
		mProgram = Shader.createProgram(mVertexShader, mFragmentShader);
		
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	}
    */
	public static float[] mVMatrix = new float[16];
	public static float[] mProjMatrix = new float[16];
	public static float[] mMVPMatrix = new float[16];
    
	public void drawSelf(){
		GLES20.glUseProgram(mProgram);

		Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
		Matrix.translateM(mMMatrix, 0, -4.2f, -2.2f, -2.0f);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, Cube.getFinalMatrix(mMMatrix), 0);

        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, mVertexBuffer);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 4, mColorBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        /*
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        */
        GLES20.glLineWidth(3.0f);
        GLES20.glDrawElements(GLES20.GL_LINE_LOOP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        /*
		GLES20.glUseProgram(mProgram);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(),0);
		GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT, false,3*4, mVertexBuffer);
		GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 4*4, mColorBuffer);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glEnableVertexAttribArray(mColorHandle);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vCount);
		*/
	}

	public static float[] getFinalMatrix(float[] spec) {
		mMVPMatrix = new float[16];
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
		return mMVPMatrix;
	}
    
}
