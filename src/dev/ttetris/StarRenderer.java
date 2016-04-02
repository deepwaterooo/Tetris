package dev.ttetris;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
/*
import android.opengl.GLU;
import java.nio.ByteBuffer;  
import java.nio.ByteOrder;  
import java.nio.FloatBuffer;  
*/
public class StarRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "StarGLRender";
    private Square mGameSquare;
    private Square mNextSquare;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private float mAngle;
    
    private StarGLSurfaceView mView;
    
    @Override  
    public void onSurfaceCreated(GL10 gl,EGLConfig config){  
        // set the background color to black (rgba)
        //gl.glClearColor(0.419f, 0.137f, 0.55686f, 0.5f); // dark blue
        gl.glClearColor(1.0f, 1.0f, 0.0f, 0.0f); // yellow
        /*        
        // Enable Smooth Shading, default not rally needed
        gl.glShadeModel(GL10.GL_SMOOTH); // 启用阴影平滑（不是必须的）
        // Depth buffer setup 设置深度缓存
        gl.glClearDepthf(1.0f);
        // Enables depth testing 启用深度测试
        gl.glEnable(GL10.GL_DEPTH_TEST);
        // the type of depth testing to do 所用深度测试的类型
        gl.glDepthFunc(GL10.GL_LEQUAL);
        */
        mGameSquare = new Square();
        mNextSquare = new Square();
        
        // Really nice perspective calculations. 对透视进行修正
        //gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); // android.opengl.GLException: invalid enum
        // don't need this one any more
        //gl.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_FASTEST);   
    }  
   
    @Override
    // 要应用投影和视口矩阵，需将矩阵们相乘然后把它们设置给顶点着色器
    public void onDrawFrame(GL10 gl) {
        // Draw background color
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix) 创建一个视口矩阵
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        // 合并投影和视口矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        
        // draw square
        mGameSquare.draw(mMVPMatrix);
    }  

    @Override  
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // adjust the viewport based on geometry changes, such as screen rotation
        gl.glViewport(0, 0, width, height); // 设置画面的大小
        float ratio = (float) width / height;
        /*
        gl.glMatrixMode(GL10.GL_PROJECTION); // 设置投影矩阵 set matrix to projection mode 
        gl.glLoadIdentity();                 // 重置投影矩阵 reset the matrix to its default state 
        gl.glFrustumf(-aspect, aspect, -1.0f, 1.0f, 1.0f, 10.0f); // apply the projection matrix
        */
        // the proj matrix is applied to object coordinates in the onDrawFrame() method
        // 根据设备屏幕的几何特征创建投影矩阵 
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        /*        
        // selection the projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);
        // reset the projection matrix
        gl.glLoadIdentity();
        // 设置画面比例 calculate the aspect ratio of the window 
        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
        // 选择模型观察矩阵 select the modelview matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        // 重置模型观察矩阵 reset the modelview matrix
        gl.glLoadIdentity();  */
    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    *
    * <pre>
    * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
    *
    * If the operation is not successful, the check throws an error.
    *
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }
    
    /*   
    private class Square {
        FloatBuffer vertexbuffer;
        ByteBuffer indicesBuffer;
        float vertices[] = {
            -1.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f,
            -1.0f, -1.0f
        };
        byte indices[] = {0, 1, 2, 0, 2, 3};

        public Square(){
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            vertexbuffer = byteBuffer.asFloatBuffer();
            vertexbuffer.put(vertices);
            vertexbuffer.position(0);

            indicesBuffer = ByteBuffer.allocateDirect(indices.length);
            indicesBuffer.put(indices);
            indicesBuffer.position(0); 
        }

        public void setVerticesAndDraw(float value, GL10 gl, byte color) {
            ByteBuffer indicesBuffer;
            ByteBuffer mColorBuffer;
            FloatBuffer verticesBuffer;
            //FloatBuffer textureBuffer;

            byte indices[] = {0, 1, 2, 0, 2, 3};
            float vertices[] = {
                -value, value, 0.0f,
                value, value, 0.0f,
                value, -value, 0.0f,
                -value, -value, 0.0f
            }; 
            
            byte colors[] = {
                color, color, 0, color,    
                0, color, color, color,    
                0, 0, 0, color,    
                color, 0, color, color
            };
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            verticesBuffer = byteBuffer.asFloatBuffer();
            verticesBuffer.put(vertices);
            verticesBuffer.position(0);

            indicesBuffer = ByteBuffer.allocateDirect(indices.length);
            indicesBuffer.put(indices);
            indicesBuffer.position(0);
            
            mColorBuffer = ByteBuffer.allocateDirect(colors.length);
            mColorBuffer.put(colors);
            mColorBuffer.position(0);
            
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
            gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, mColorBuffer);
            gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indicesBuffer);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        }
    }

    private double degressToRadian(float deg){  
        double rad = 0;  
        rad = (deg * Math.PI)/180.0f;  
        return rad;  
    }  
   
    private FloatBuffer getFloatBuffer(float[] table ){  
        ByteBuffer bb = ByteBuffer.allocateDirect(table.length * 4);  
        bb.order(ByteOrder.nativeOrder());  
        FloatBuffer fb = bb.asFloatBuffer();  
        fb.put(table);  
        fb.position(0);  
        return fb;  
    }
    */    
}
