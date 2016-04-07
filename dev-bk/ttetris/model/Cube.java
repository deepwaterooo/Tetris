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
    // 触碰立方体某一面的标记（0—5）
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
    
    /*    // 立方体顶点坐标 
    private float[] vertices = new float[] { -one, -one, one, one, -one, one, 
                                             one, one, one, -one, one, one, -one, -one, -one, -one, one, -one, 
                                             one, one, -one, one, -one, -one, -one, one, -one, -one, one, one, 
                                             one, one, one, one, one, -one, -one, -one, -one, one, -one, -one, 
                                             one, -one, one, -one, -one, one, one, -one, -one, one, one, -one, 
                                             one, one, one, one, -one, one, -one, -one, -one, -one, -one, one, 
                                             -one, one, one, -one, one, -one }; 
    // 立方体纹理坐标 
    private float[] texCoords = new float[] { one, 0, 0, 0, 0, one, one, one, 
                                              0, 0, 0, one, one, one, one, 0, one, one, one, 0, 0, 0, 0, one, 0, 
                                              one, one, one, one, 0, 0, 0, 0, 0, 0, one, one, one, one, 0, one, 
                                              0, 0, 0, 0, one, one, one }; 
    // 三角形描述顺序 
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
        // 合并投影和视口矩阵 Calculate the projection and view transformation
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
    // 获取坐标的缓存对象
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

    // 获取三角形的描述顺序
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

    // 返回立方体外切圆的中心点
    public Vector3f getSphereCenter() {
        return new Vector3f(0, 0, 0);
    }

    // 返回立方体外切圆的半径（sqrt(3))
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
     * 射线与模型的精确碰撞检测 
     * @param ray - 转换到模型空间中的射线 
     * @param trianglePosOut - 返回的拾取后的三角形顶点位置 
     * @return 如果相交，返回true 

    public boolean intersect(Ray ray, Vector3f [] trianglePosOut) {
        boolean bFound = false;
        // 存储着谢线斫号三角形相交点的距离，最后仅仅保留距离最近的那一个
        float closeDis = 0.0f;
        Vector3f v0, v1, v2;

        // 立方体的6个面
        for (int i = 0; i < 6; i++) {
            // 每个面有两个三角形
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    v0 = getVector3f(indices[i * 4 + j]);
                    v1 = getVector3f(indices[i * 4 + j + 1]);
                    v2 = getVector3f(indices[i * 4 + j + 2]);
                } else {
                    // 第二个三角形时，换下顺序，不然会渲染到立方体内部
                    v0 = getVector3f(indices[i * 4 + j]);
                    v1 = getVector3f(indices[i * 4 + j + 2]);
                    v2 = getVector3f(indices[i * 4 + j + 1]);
                }
                // 进行射线和三角形的磁撞检测
                if (ray.intersectTriangle(v0, v1, v2, location)) {
                    // 如果发生了相交
                    if (!bFound) {
                        // 如果是初次检测到，需要存储射线原点与三角形交点的距离值
                        bFound = true;
                        closeDis = location.w;
                        trianglePosOut[0].set(v0);
                        trianglePosOut[1].set(v1);
                        trianglePosOut[2].set(v2);
                        surface = i;
                    } else {
                        // 如果之前已经检测到相交事件，则需要把新相交点与之前的相交点数据相比较，保留离射线点更近的
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
