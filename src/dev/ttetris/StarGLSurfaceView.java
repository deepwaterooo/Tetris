package dev.ttetris;

import dev.ttetris.model.Block;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.os.Handler;
import android.util.AttributeSet;
import android.os.Bundle;
import javax.microedition.khronos.opengles.GL10;

class StarGLSurfaceView extends GLSurfaceView {
    final float ANGLE_SPAN = 0.375f;
    
    private static StarGLSurfaceView mStarGLSurfaceView = null;
    //private StarRenderer mStarRenderer; // final?
    private StarRenderer mStarRenderer; 

    private static Handler sHandler; // do I need a handler?

	private ActivityGame activity;
    private Model model;
    public int DELAY = 100;
	private long lastMove = 0;
    Block activeBlock;
	Block nextBlock;  

    // ��¼�ϴδ���λ�õ����� 
    private float mPreviousX, mPreviousY; 
    
    public StarGLSurfaceView(Context context) {
        super(context);
        setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS); // Turn on error-checking and logging
        setEGLContextClientVersion(2);
        initView();
    }

    public StarGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS); 
        setEGLContextClientVersion(2);
        initView();
    }
    /*
    public StarGLSurfaceView(Context context, OnSurfacePickedListener onSurfacePickedListener) {
        super(context, onSurfacePickedListener);
        setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
        setEGLContextClientVersion(2);
        initView();
        // ͸����һ��View 
        setZOrderOnTop(true); 
        setEGLConfigChooser(8, 8, 8, 8, 16, 0); 
        // ͸����һ��Activity 
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mStarRenderer.setOnSurfacePickedListener(onSurfacePickedListener);
        } */
    
    private void initView() {
        //mStarRenderer = new StarRenderer(); //������Ⱦ��
        mStarRenderer = new StarRenderer(); //������Ⱦ��
        setRenderer(mStarRenderer);  //������Ⱦ��
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mStarGLSurfaceView = this; // do I really need this one?
        setFocusableInTouchMode(true);
        //model = new Model(7, 20, 7);
        model = new Model();
    }
    
    public boolean onTouchEvent(final MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        //AppConfig.setTouchPosition(x, y);  // sth I need to code ///////////////////////////////
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            // �������ĵ�����Ʒ�����ʱ����ת90�������� 
            float dx = y - mPreviousY; 
            float dy = x - mPreviousX; 
            // ���ƾ��� 
            float d = (float) (Math.sqrt(dx * dx + dy * dy)); 
            // ��ת�ᵥλ������x,yֵ��z=0�� 
            mStarRenderer.mfAngleX = dx; 
            mStarRenderer.mfAngleY = dy; 
            // ���ƾ��� 
            mStarRenderer.gesDistance = d; 
 
            // float dx = x - mPreviousX; 
            // float dy = y - mPreviousY; 
            // mStarRenderer.mfAngleY += dx * TOUCH_SCALE_FACTOR; 
            // mStarRenderer.mfAngleX += dy * TOUCH_SCALE_FACTOR; 
 
            // PickFactory.update(x, y); 
            //AppConfig.gbNeedPick = false; 
            break; 
        case MotionEvent.ACTION_DOWN: 
            //AppConfig.gbNeedPick = false; 
            break; 
        case MotionEvent.ACTION_UP: 
            //AppConfig.gbNeedPick = true; 
            break; 
        case MotionEvent.ACTION_CANCEL: 
            //AppConfig.gbNeedPick = false; 
            break; 
        } 
        mPreviousX = x; 
        mPreviousY = y; 
        return true; 
    }
    
    public void setModel(Model model) {
		this.model = model;
	}

	public void setActivity(ActivityGame activity) {
		this.activity = activity;
	}

	public void endGame() {
	}

	public void pauseGame() {
		//model.setGamePaused();
	}
}
