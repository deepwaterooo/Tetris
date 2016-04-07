package dev.ttetris;

import dev.ttetris.model.*;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.os.Handler;
import android.util.AttributeSet;
import android.os.Bundle;
import javax.microedition.khronos.opengles.GL10;

class StarGLSurfaceView extends GLSurfaceView {
    // view Constants
    public static final float AMBIENT_VALUE = 0.35F;
    public static final int BACKGROUND_RENDER_QUEUE = -1;
    public static final int BRICK_BATCH_MAX_SIZE = 20;
    public static final float BRICK_REFLECTION_STRENGTH = 0.3F;
    public static final int BRICK_RENDER_QUEUE = 0;
    public static final float BRICK_SPECULAR_POWER = 12.5F;
    public static final float[] GLASS_COLOR = { 0.0F, 1.0F, 0.3F, 1.0F };
    public static final int GLASS_DIFFUSE_QUEUE = 1;
    public static final float GLASS_SPECULAR_POWER = 15.0F;
    public static final int GLASS_SPECULAR_QUEUE = 2;
    public static final float[] LIGHT_DIRECTION = { 0.78F, -0.45F, -0.5F };
    public static final int PARTICLES_QUEUE = 3;
    public static final float[] SPECULAR_COLOR = { 1.0F, 1.0F, 0.7F, 1.0F };
    // model constants
    public static final float BOARDS_OBSERVE_DETECTION_EPS = 1.0E-06F;
    public static final float BOARDS_OBSERVE_ROTATION_SPEED = 15.0F;
    public static final int BOARD_HEIGHT = 13;
    public static final float BOARD_ROTATION_ANGLE = 180.0F;
    public static final float BOARD_ROTATION_TIME = 0.7F;
    public static final int BOARD_WIDTH = 11;
    public static final float BRICK_DISAPPEARING_INTERVAL = 0.03F;
    public static final float BRICK_FALL_TIME_MAX = 1.1F;
    public static final float BRICK_FALL_TIME_MIN = 0.35F;
    public static final float FIRE_LIFE_TIME = 0.35F;
    public static final float FIRE_LIFE_TIME_RANGE = 0.05F;
    public static final int MAX_ACC_LEVEL = 10;
    public static final int POINTS_PER_LEVEL = 200;
    public static final int STARS_POOL_SIZE = 2;
    
    private static StarGLSurfaceView mStarGLSurfaceView = null;
    //private StarRenderer mStarRenderer; // final?
    private StarRenderer mStarRenderer; 

    private static Handler sHandler; // do I need a handler?

	private ActivityGame activity;
    private Model model;
    public int DELAY = 100;
	private long lastMove = 0;

    // 记录上次触屏位置的坐标 
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
        // 透视上一个View 
        setZOrderOnTop(true); 
        setEGLConfigChooser(8, 8, 8, 8, 16, 0); 
        // 透视上一个Activity 
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mStarRenderer.setOnSurfacePickedListener(onSurfacePickedListener);
        } */
    
    private void initView() {
        //mStarRenderer = new StarRenderer(); //创建渲染器
        mStarRenderer = new StarRenderer(); //创建渲染器
        setRenderer(mStarRenderer);  //设置渲染器
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
            // 经过中心点的手势方向逆时针旋转90°后的坐标 
            float dx = y - mPreviousY; 
            float dy = x - mPreviousX; 
            // 手势距离 
            float d = (float) (Math.sqrt(dx * dx + dy * dy)); 
            // 旋转轴单位向量的x,y值（z=0） 
            mStarRenderer.mfAngleX = dx; 
            mStarRenderer.mfAngleY = dy; 
            // 手势距离 
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
