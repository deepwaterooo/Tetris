package dev.ttetris;

import dev.ttetris.model.Cube;
import dev.ttetris.model.CubeColor;
import dev.ttetris.model.Constant;
import dev.ttetris.model.Block;
import dev.ttetris.model.BlockMeta;
import dev.ttetris.model.BlockType;
import dev.ttetris.model.Model;
import dev.ttetris.model.Frame;
import dev.ttetris.model.Grid;
import dev.ttetris.util.AppConfig;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.content.Context;
import android.view.MotionEvent;
import android.graphics.PixelFormat;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import javax.vecmath.Matrix4f;

public class StarGLSurfaceView extends GLSurfaceView {
    private StarRenderer mStarRenderer; 

    public StarGLSurfaceView(Context context, OnSurfacePickedListener onSurfacePickedListener) {
        super(context);
        setEGLContextClientVersion(2);
        setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);

        mStarRenderer = new StarRenderer(context);
        setRenderer(mStarRenderer);                      
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        mStarRenderer.setOnSurfacePickedListener(onSurfacePickedListener);

        //setEGLConfigChooser(8, 8, 8, 8, 16, 0); 
        getHolder().setFormat(PixelFormat.TRANSLUCENT);  
        //setZOrderOnTop(true);
        setFocusableInTouchMode(true);
    } 

    public void onPause() {  super.onPause(); }
    public void onResume() { super.onResume(); }
}
