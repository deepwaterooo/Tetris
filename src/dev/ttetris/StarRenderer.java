package dev.ttetris;

import dev.ttetris.model.BlockType;
import dev.ttetris.model.CubeColor;
import dev.ttetris.model.Cube;
import dev.ttetris.model.Block;
import dev.ttetris.model.BlockMeta;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.util.Log;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import javax.vecmath.Matrix4f;;

public class StarRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "StarRenderer";
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private float mAngle;

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

    private FloatBuffer vertexBuffer;
    private FloatBuffer mColorBuffer;
    private ShortBuffer drawListBuffer;
    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private static final int COORDS_PER_VERTEX = 3;
    private static final int VALUES_PER_COLOR = 4;
    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private final int COLOR_STRIDE = VALUES_PER_COLOR * 4;

    public static final int VERTEX_BUFFER = 0; 
    public static final int TEXTURE_BUFFER = 1; 
    public float mfAngleX = 0.0f; 
    public float mfAngleY = 0.0f; 
    public float gesDistance = 0.0f; 
    private static float one = 1.0f; 

    private Block currBlock;
    
    public enum BlockColor {  // set in Block
		RED(0xffff0000, (byte) 1),
        GREEN(0xff00ff00, (byte) 2),
        BLUE(0xff0000ff, (byte) 3),
        YELLOW(0xffffff00, (byte) 4),
        CYAN(0xff00ffff, (byte) 5),
        WHITE(0xffffffff, (byte) 6),
        MAGENTA(0xffff00ff, (byte) 7),
        TRANSPARENT(0x20320617, (byte) 8);
		private final int color;
		private final byte value;
		private BlockColor(int color, byte value) {
			this.color = color;
			this.value = value;
		}
	}

    private static final float color[] = {1.0f, 1.0f, 1.0f, 1.0f};
    //public short drawOrder0[] = {0, 4, 5, 1, 1, 0, 3, 2, 2, 6, 7, 3, 3, 7, 4, 0//};  // 4 surfaces
    public short drawOrder0[] = {4, 5, 1, 0, 3, 2, 1, 0, 4, 7, 3, 0,   // 3 surfaces
                                 18, 19, 17, 16, 14, 15, 13, 12, 10, 11, 9, 8, 0, 20, 21, 23,
                                 22, 24, 25, 27, 26, 28, 29, 31, 30, 31, 5, 4}; // so far let it be this way

    private static final short drawOrder[] = { // for cubes
        0, 1, 2, 3, 0, 4, 5, 1,
        1, 2, 6, 5, 5, 6, 7, 4,
        7, 6, 2, 3, 3, 7, 4, 0};

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 0.0f, 0.0f); // yellow
        //initShapes();
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
        currBlock = new Block(new BlockMeta(CubeColor.Brass, BlockType.squareType, 1, 1, 0));
    }
     
    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        GLES20.glViewport(0, 0, w, h);           //�����Ӵ�
        float ratio = (float) w / h;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }
     
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        //Matrix.setLookAtM(mViewMatrix, 0, -3.9f, 0f, 3.9f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //Matrix.setLookAtM(mViewMatrix, 0, 3f, 2.2f, 4f, 0f, 0f, 0f, 0f, 1.0f, 0.0f); // seems to be good
        //Matrix.setLookAtM(mViewMatrix, 0, 4.9f, 0f, -2.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f); // this direction is good�෴
        Matrix.setLookAtM(mViewMatrix, 0, 4.2f, 2.2f, 2.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f); // good
        // �ϲ�ͶӰ���ӿھ��� Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        GLES20.glUseProgram(mProgram);
        drawGameFrame();

        // for draw currBlock
        Cube [] cubes = currBlock.getCubes();
        for (int i = 0; i < 4; i++) {
            float squareCoords [] = cubes[i].getCubeCoordinates();
            ByteBuffer vbb = ByteBuffer.allocateDirect(squareCoords.length * 4); 
            vbb.order(ByteOrder.nativeOrder());  // use the device hardware's native byte order
            vertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
            vertexBuffer.put(squareCoords);      // add the coordinates to the FloatBuffer
            vertexBuffer.position(0);            // set the buffer to read the first coordinate
            ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
            dlb.order(ByteOrder.nativeOrder());
            drawListBuffer = dlb.asShortBuffer();
            drawListBuffer.put(drawOrder);
            drawListBuffer.position(0);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(color.length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            mColorBuffer = byteBuffer.asFloatBuffer();
            mColorBuffer.put(color);
            mColorBuffer.position(0);

            //drawLineLoop();
            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);
            GLES20.glEnableVertexAttribArray(mColorHandle);
            GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 4, mColorBuffer);
            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
            StarRenderer.checkGlError("glGetUniformLocation");
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
            StarRenderer.checkGlError("glUniformMatrix4fv");
            GLES20.glLineWidth(5.0f);
            GLES20.glDrawElements(GLES20.GL_LINE_LOOP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
            GLES20.glDisableVertexAttribArray(mPositionHandle);
            GLES20.glDisableVertexAttribArray(mColorHandle);
            
            } 
    }

    private void drawGameFrame() {
        float squareCoords [] = {
            -0.8f, -0.98f, -0.8f,  0.8f, -0.98f, -0.8f,  0.8f, 0.98f, -0.8f,  -0.8f, 0.98f, -0.8f,  // for four surfaces
            -0.8f, -0.98f, 0.8f,  0.8f, -0.98f, 0.8f,  0.8f, 0.98f, 0.8f, -0.8f, 0.98f, 0.8f,
            -0.57143f, -0.98f, -0.8f, -0.57143f,  -0.98f, 0.8f,
            -0.34286f,  -0.98f, -0.8f, -0.34286f,  -0.98f, 0.8f, 
            -0.11428f, -0.98f, -0.8f, -0.11428f, -0.98f, 0.8f,
            0.11429f, -0.98f, -0.8f, 0.11429f, -0.98f, 0.8f,
            0.34286f,  -0.98f, -0.8f, 0.34286f,  -0.98f, 0.8f, 
            0.57143f, -0.98f, -0.8f, 0.57143f,  -0.98f, 0.8f,
            -0.8f, -0.98f, -0.57143f, 0.8f, -0.98f, -0.57143f,
            -0.8f, -0.98f, -0.34286f, 0.8f, -0.98f, -0.34286f,
            -0.8f, -0.98f, -0.11428f, 0.8f, -0.98f, -0.11428f,
            -0.8f, -0.98f, 0.11429f, 0.8f, -0.98f, 0.11429f,
            -0.8f, -0.98f, 0.34286f, 0.8f, -0.98f, 0.34286f,
            -0.8f, -0.98f, 0.57143f, 0.8f, -0.98f, 0.57143f};
        
        ByteBuffer vbb = ByteBuffer.allocateDirect(squareCoords.length * 4); 
        vbb.order(ByteOrder.nativeOrder());  // use the device hardware's native byte order
        vertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
        vertexBuffer.put(squareCoords);      // add the coordinates to the FloatBuffer
        vertexBuffer.position(0);            // set the buffer to read the first coordinate
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder0.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder0);
        drawListBuffer.position(0);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(color.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        mColorBuffer = byteBuffer.asFloatBuffer();
        mColorBuffer.put(color);
        mColorBuffer.position(0);
        drawLineLoop();
    }
    /*
    private void drawLine() { // need to debug
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 6, vertexBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 4, mColorBuffer);
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        StarRenderer.checkGlError("glGetUniformLocation");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        StarRenderer.checkGlError("glUniformMatrix4fv");
        GLES20.glLineWidth(5.0f);
        GLES20.glDrawElements(GLES20.GL_LINES, drawOrder0.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);
        }*/

    private void drawLineLoop() { // need to debug
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 4, mColorBuffer);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        StarRenderer.checkGlError("glGetUniformLocation");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        StarRenderer.checkGlError("glUniformMatrix4fv");
        GLES20.glLineWidth(5.0f);
        GLES20.glDrawElements(GLES20.GL_LINE_LOOP, drawOrder0.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);
    }
    
    private void initShapes(Cube cube) {
        float squareCoords [] = cube.getCubeCoordinates();
        ByteBuffer vbb = ByteBuffer.allocateDirect(squareCoords.length * 4); 
        vbb.order(ByteOrder.nativeOrder());  // use the device hardware's native byte order
        vertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
        vertexBuffer.put(squareCoords);      // add the coordinates to the FloatBuffer
        vertexBuffer.position(0);            // set the buffer to read the first coordinate
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(color.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        mColorBuffer = byteBuffer.asFloatBuffer();
        mColorBuffer.put(color);
        mColorBuffer.position(0);
    }

    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode); // add the source code to the shader and compile it
        GLES20.glCompileShader(shader);
        return shader;
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
    
    /*    // �����嶥������ 
    private float[] vertices = new float[] { -one, -one, one, one, -one, one, 
                                             one, one, one, -one, one, one, -one, -one, -one, -one, one, -one, 
                                             one, one, -one, one, -one, -one, -one, one, -one, -one, one, one, 
                                             one, one, one, one, one, -one, -one, -one, -one, one, -one, -one, 
                                             one, -one, one, -one, -one, one, one, -one, -one, one, one, -one, 
                                             one, one, one, one, -one, one, -one, -one, -one, -one, -one, one, 
                                             -one, one, one, -one, one, -one }; 
    // �������������� 
    private float[] texCoords = new float[] { one, 0, 0, 0, 0, one, one, one, 
                                              0, 0, 0, one, one, one, one, 0, one, one, one, 0, 0, 0, 0, one, 0, 
                                              one, one, one, one, 0, 0, 0, 0, 0, 0, one, one, one, one, 0, one, 
                                              0, 0, 0, 0, one, one, one }; 
    // ����������˳�� 
    private byte[] indices = new byte[] { 0, 1, 3, 2, 4, 5, 7, 6, 8, 9, 11, 10, 12, 13, 15, 14, 16, 17, 19, 18, 20, 21, 23, 22 }; 
    */

    // for rotate and detecting
    // ����������ĳһ��ı�ǣ�0��5��
    public int surface = -1;
    /*
    // ��ȡ����Ļ������
    public FloatBuffer getCoordinate(int coord_id) {
        switch (coord_id) {
        case VERTEX_BUFFER:
            return getDirectBuffer(squareCoords);
            //case TEXTURE_BUFFER:
            //return getDirectBuffer();
        default:
                throw new IllegalArgumentException();
        }
        }

    // ��ȡ�����ε�����˳��
    public ByteBuffer getIndices() {
        return ByteBuffer.wrap(drawListBuffer);
        }*/

    public FloatBuffer getDirectBuffer(float [] buffer) {
        ByteBuffer bb = ByteBuffer.allocateDirect(buffer.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer directBuffer = bb.asFloatBuffer();
        directBuffer.put(buffer);
        directBuffer.position(0);
        return directBuffer;
    }

    // ��������������Բ�����ĵ�
    public Vector3f getSphereCenter() {
        return new Vector3f(0, 0, 0);
    }

    // ��������������Բ�İ뾶��sqrt(3))
    public float getSphereRadius() {
        return 1.732051f;
    }

    private Vector3f transformedSphereCenter = new Vector3f(); 
    //private Ray transformedRay = new Ray(); 
    private Matrix4f matInvertModel = new Matrix4f(); 
    private Vector3f[] mpTriangle = {
        new Vector3f(),
        new Vector3f(), 
        new Vector3f()
    };     

    /** 
     * ������ģ�͵ľ�ȷ��ײ��� 
     * @param ray - ת����ģ�Ϳռ��е����� 
     * @param trianglePosOut - ���ص�ʰȡ��������ζ���λ�� 
     * @return ����ཻ������true 

    public boolean intersect(Ray ray, Vector3f [] trianglePosOut) {
        boolean bFound = false;
        // �洢��л�����������ཻ��ľ��룬���������������������һ��
        float closeDis = 0.0f;
        Vector3f v0, v1, v2;

        // �������6����
        for (int i = 0; i < 6; i++) {
            // ÿ����������������
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    v0 = getVector3f(indices[i * 4 + j]);
                    v1 = getVector3f(indices[i * 4 + j + 1]);
                    v2 = getVector3f(indices[i * 4 + j + 2]);
                } else {
                    // �ڶ���������ʱ������˳�򣬲�Ȼ����Ⱦ���������ڲ�
                    v0 = getVector3f(indices[i * 4 + j]);
                    v1 = getVector3f(indices[i * 4 + j + 2]);
                    v2 = getVector3f(indices[i * 4 + j + 1]);
                }
                // �������ߺ������εĴ�ײ���
                if (ray.intersectTriangle(v0, v1, v2, location)) {
                    // ����������ཻ
                    if (!bFound) {
                        // ����ǳ��μ�⵽����Ҫ�洢����ԭ���������ν���ľ���ֵ
                        bFound = true;
                        closeDis = location.w;
                        trianglePosOut[0].set(v0);
                        trianglePosOut[1].set(v1);
                        trianglePosOut[2].set(v2);
                        surface = i;
                    } else {
                        // ���֮ǰ�Ѿ���⵽�ཻ�¼�������Ҫ�����ཻ����֮ǰ���ཻ��������Ƚϣ����������ߵ������
                        if (closeDis > location.w) {
                            closeDis = location.w;
                            trianglePosOut[0].set(v0);
                            trianglePosOut[1].set(v1);
                            trianglePosOut[2].set(v2);
                            surface = i;
                        }
                    }
                }
            }

        }
        return bFound;
    }

    private Vector3f getVector3f(int start) {
        return new Vector3f(vertices[3 * start], vertices[3 * start + 1], vertices[3 * start + 2]);
    }
    */    
}
