package dev.ttetris;

import android.os.Bundle;
import android.os.Handler;
import android.content.Context;
import android.graphics.PixelFormat;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

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

        getHolder().setFormat(PixelFormat.TRANSLUCENT);  
        setFocusableInTouchMode(true);
    } 

    public void onPause() {  super.onPause(); }
    public void onResume() { super.onResume(); }
}
