package dev.ttetris;

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

public class CubeRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "CubeGLSurfaceViewRender";
    float xrot = 0.0f;
    float yrot = 0.0f;
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
    
    private static final float color[] = {
        1.0f, 0.0f, 1.0f, 1.0f, // 0
        0.0f, 1.0f, 1.0f, 1.0f, // 1
        1.0f, 0.0f, 1.0f, 1.0f, // 
        1.0f, 1.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f
    };/*
        1.0f, 0.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 1.0f, 1.0f};  */
    private static final short drawOrder[] = {
        0, 1, 4, 1, 5, 4,
        0, 1, 2, 0, 2, 3,  // byte
        2, 3, 6, 3, 6, 7,
        4, 5, 6, 4, 6, 7,
        0, 3, 4, 3, 4, 7,
        1, 2, 5, 2, 5, 6
    }; // order to draw vertices
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 0.0f, 0.0f); // yellow ????
        initShapes();
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }
     
    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        GLES20.glViewport(0, 0, w, h);           //设置视窗
        float ratio = (float) w / h;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }
     
    @Override
    public void onDrawFrame(GL10 gl) {
        //float [] scratch = new float[16]; // for triangle
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        // Set the camera position (View matrix) 创建一个视口矩阵
        Matrix.setLookAtM(mViewMatrix, 0, -3.9f, 0f, 3.9f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // 合并投影和视口矩阵 Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // draw square
        GLES20.glUseProgram(mProgram);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);
        //mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor"); // for平面only
        //GLES20.glUniform4fv(mColorHandle, 1, color, 0);                 // for平面only
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, COLOR_STRIDE, mColorBuffer);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        CubeRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        //GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, scratch, 0); // for triangle
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        CubeRenderer.checkGlError("glUniformMatrix4fv");
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                              GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);

        /*        
        // draw triangle: a rotation for triangle generate constant rotation
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);
        // combine rotation matrix with projection and camera view
        // mMVPMatrix factor must be first in order for matrix manipulation product to be right
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        //mTriangle.draw(scratch); // a second section
        GLES20.glUseProgram(mProgram);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 12, triangleVB);
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        CubeRenderer.checkGlError("glGetUniformLocation");
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, scratch, 0);
        CubeRenderer.checkGlError("glUniformMatrix4fv");
        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        */
        
        /*
        gl.glMatrixMode(GL10.GL_MODELVIEW);   //切换至模型观察矩阵
        gl.glLoadIdentity();                  // 重置当前的模型观察矩阵
        GLU.gluLookAt(gl, 0, 0, 3, 0, 0, 0, 0, 1, 0); //设置视点和模型中心位置
     
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeBuff);//设置顶点数据
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
     
        gl.glRotatef(xrot, 1, 0, 0);  //绕着(0,0,0)与(1,0,0)即x轴旋转
        gl.glRotatef(yrot, 0, 1, 0);
         
        gl.glColor4f(1.0f, 0, 0, 1.0f);   //设置颜色，红色
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);  //绘制正方型FRONT面
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
     
        gl.glColor4f(0, 1.0f, 0, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
         
        gl.glColor4f(0, 0, 1.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
        xrot += 1.0f;
        yrot += 0.5f;
        */
    }

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
        vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
        vertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
        vertexBuffer.put(squareCoords);    // add the coordinates to the FloatBuffer
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

    /**
     * Utility method for compiling a OpenGL shader.
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError() method to debug shader coding errors.</p>
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode); // add the source code to the shader and compile it
        GLES20.glCompileShader(shader);
        return shader;
    }

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    * <pre>
    * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
    * If the operation is not successful, the check throws an error.
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}
