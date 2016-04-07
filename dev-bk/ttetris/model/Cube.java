package dev.ttetris.model;

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
import javax.vecmath.Matrix4f;
import java.io.Serializable;

public class Cube implements Cloneable, Comparable<Cube>, Serializable {
    private static final String TAG = "Cube - basic grid unit";
    /*
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
    */
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
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
    // ����������ĳһ��ı�ǣ�0��5��
    public int surface = -1;


    
    private static final long serialVersionUID = 6144113039836213006L;
    private CubeType type;
    private int x;
    private int y;
    private int z;
    private int rx;
    private int ry;
    private int rz;

    public Cube(CubeType paramCubeType, int paramInt1, int paramInt2, int paramInt3) {
        this(paramCubeType, paramInt1, paramInt2, paramInt3, 0, 0, 0);
    }

    public Cube(CubeType paramCubeType, int paramInt1, int paramInt2, int paramInt3,
                int paramInt4, int paramInt5, int paramInt6) {
        this.type = paramCubeType;
        this.x = paramInt1;
        this.y = paramInt2;
        this.z = paramInt3;
        this.rx = paramInt4;
        this.ry = paramInt5;
        this.rz = paramInt6;
    }

    public Cube clone() {
        try {
            Cube localCube = (Cube)super.clone();
            return localCube;
        } catch (CloneNotSupportedException localCloneNotSupportedException) {
        }
        return null;
    }

    public int compareTo(Cube paramCube) {
        return this.x - paramCube.x;
    }

    public CubeType getType() { return this.type; }

    public void setX(int paramInt) { this.x = paramInt; }
    public void setY(int paramInt) { this.y = paramInt; }
    public void setZ(int paramInt) { this.z = paramInt; }
    public void setRx(int paramInt) { this.rx = paramInt; }
    public void setRy(int paramInt) { this.ry = paramInt; }
    public void setRz(int paramInt) { this.rz = paramInt; }
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getZ() { return this.z; }
    public int getRx() { return this.rx; }
    public int getRy() { return this.ry; }
    public int getRz() { return this.rz; }






    
    // for rotate and detection
    public static final int VERTEX_BUFFER = 0; 
    public static final int TEXTURE_BUFFER = 1; 
    private float mAngle;
    public float mfAngleX = 0.0f; 
    public float mfAngleY = 0.0f; 
    public float gesDistance = 0.0f; 
    //private static float one = 1.0f;
    
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
    private byte[] indices = new byte[] { 0, 1, 3, 2, 4, 5, 7, 6, 8, 9, 11, 10, 
                                          12, 13, 15, 14, 16, 17, 19, 18, 20, 21, 23, 22 }; 
    */
    private static final float color[] = {
        1.0f, 1.0f, 1.0f, 1.0f, 
        1.0f, 1.0f, 1.0f, 1.0f, 
        1.0f, 1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f, 1.0f
 /* 0,    0,    0,  one,  
        one,    0,    0,  one,  
        one,  one,    0,  one,  
        0,  one,    0,  one,  
        0,    0,  one,  one,  
        one,    0,  one,  one,  
        one,  one,  one,  one,  
        0,  one,  one,  one
                          1.0f, 0.0f, 1.0f, 1.0f, 
        0.0f, 1.0f, 1.0f, 1.0f, 
        1.0f, 0.0f, 1.0f, 1.0f, 
        1.0f, 1.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f*/
    };
    private static final short drawOrder[] = {
        0, 1, 2, 3,
        4, 5, 6, 7,
        1, 2, 6, 5,
        0, 3, 7, 4
    };
    /*
    private static final short drawOrder[] = {
        0, 1, 2, 0, 2, 3, 
        0, 1, 4, 1, 5, 4,
        2, 3, 6, 3, 6, 7,
        4, 5, 6, 4, 6, 7,
        0, 3, 4, 3, 4, 7,
        1, 2, 5, 2, 5, 6
        }; */ 
    /*
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        Matrix.setLookAtM(mViewMatrix, 0, -4f, 0f, 4f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // �ϲ�ͶӰ���ӿھ��� Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        GLES20.glUseProgram(mProgram);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);
        //GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 6, vertexBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, COLOR_STRIDE, mColorBuffer);
        //GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 8, mColorBuffer);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        CubeRenderer.checkGlError("glGetUniformLocation");

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        CubeRenderer.checkGlError("glUniformMatrix4fv");

        GLES20.glLineWidth(5.0f);
        
        //GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        GLES20.glDrawElements(GLES20.GL_LINE_LOOP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);

    }
    */
    private void initShapes() {
        float squareCoords [] = {
            -0.8f, -0.98f, -0.8f,
            0.8f, -0.98f, -0.8f,
            0.8f, 0.98f, -0.8f,
            -0.8f, 0.98f, -0.8f,
            -0.8f, -0.98f, 0.8f,
            0.8f, -0.98f, 0.8f,
            0.8f, 0.98f, 0.8f,
            -0.8f, 0.98f, 0.8f};
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

    // for rotate and detecting
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
}
