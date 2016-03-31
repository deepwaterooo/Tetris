package dev.ttetris;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

class StarGLSurfaceView extends GLSurfaceView {
    private static final StarGLSurfaceView mStarGLSurfaceView;
    private static Handler sHandler; // do I need a handler?
    private StarRenderer mMyRenderer;

    public StarGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        initView();
    }

    public StarGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setEGLContextClientVersion(2);
        initView();
    }

    private void initView() {
        setFocusableInTouchMode(true);
        mStarGLSurfaceView = this;
        myRenderer = new MyRenderer(); //¥¥Ω®‰÷»æ∆˜
        this.setRenderer(myRenderer);  //…Ë÷√‰÷»æ∆˜
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    
    public void start() {
        mMyRenderer =...;
        setRenderer(mMyRenderer);
    }
    
    public boolean onKeyDown(intkeyCode, KeyEventevent) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
            queueEvent(newRunnable(){
                    public void run(){
                        mMyRenderer.handleDpadCenter();
                    }});
            returntrue;
        }
        returnsuper.onKeyDown(keyCode,event);
    }

    
}
