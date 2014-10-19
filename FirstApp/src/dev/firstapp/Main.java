package dev.firstapp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Main extends Activity implements IOpenGLDemo {

    private GLSurfaceView mGLSurfaceView;

    // 
    float[] vertexArray = new float[] { 
            -0.8f, -0.4f * 1.732f, 0.0f, 
            0.8f,-0.4f * 1.732f, 0.0f,
            0.0f, 0.4f * 1.732f, 0.0f, };

    // 
    float vertexArray2[] = { 
            -0.8f, -0.4f * 1.732f, 0.0f,
            -0.4f, 0.4f * 1.732f,0.0f, 
            0.0f, -0.4f * 1.732f, 0.0f, 
            0.4f, 0.4f * 1.732f, 0.0f, };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setRenderer(new OpenGLRenderer(this));
        setContentView(mGLSurfaceView);
    }

    //
    //
    // public void DrawScene(GL10 gl) {
    // gl.glClearColor(0f, 0f, 1.0f, 0.0f);
    // // Clears the screen and depth buffer.
    // gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    // }

    // 
//     public void DrawScene(GL10 gl) {
//     gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
//     //System.out.println("we executed here already");
//     ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
//     vbb.order(ByteOrder.nativeOrder());
//     FloatBuffer vertex = vbb.asFloatBuffer();
//     vertex.put(vertexArray);
//     vertex.position(0);
//    
//     gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
//     gl.glPointSize(18f);
//     gl.glLoadIdentity();
//     gl.glTranslatef(0, 0, -4);
//    
//     gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//    
//     gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertex);
//     gl.glDrawArrays(GL10.GL_POINTS, 0, 3);
//    
//     gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//    
//     }

    // 
    public void DrawScene(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray2.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer vertex = vbb.asFloatBuffer();
        vertex.put(vertexArray);
        vertex.position(0);

        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -4);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertex);

        int index = new Random().nextInt(4);
        switch (index) {

        case 1:
            gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
            gl.glDrawArrays(GL10.GL_LINES, 0, 4);
            break;
        case 2:
            gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
            gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 4);
            break;
        case 3:
            gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
            gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);
            break;
        }

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

    }

    @Override
    protected void onResume() {
        // Ideally a game should implement
        // onResume() and onPause()
        // to take appropriate action when the
        // activity looses focus
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume()
        // and onPause()
        // to take appropriate action when the
        // activity looses focus
        super.onPause();
        mGLSurfaceView.onPause();
    }

}
