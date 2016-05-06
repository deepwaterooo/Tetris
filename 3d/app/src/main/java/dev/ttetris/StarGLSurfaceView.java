package dev.ttetris;

import dev.ttetris.model.Cube;
import dev.ttetris.model.Block;
import dev.ttetris.model.Model;
import dev.ttetris.util.AppConfig;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.AttributeSet;
import android.os.Bundle;
import javax.microedition.khronos.opengles.GL10;

import dev.ttetris.model.BlockType;
import dev.ttetris.model.CubeColor;
import dev.ttetris.model.Cube;
import dev.ttetris.model.Block;
import dev.ttetris.model.Model;
import dev.ttetris.util.MatrixState;

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

public class StarGLSurfaceView extends GLSurfaceView {
    //final float ANGLE_SPAN = 0.375f;
    private StarRenderer mStarRenderer; 
    // 记录上次触屏位置的坐标 
    private float mPreviousX, mPreviousY; 

	private ActivityGame activity;
    private Model model;
    public int DELAY = 100;
	private long lastMove = 0;
    Block activeBlock;
	Block nextBlock;  

    // set in Block
    public enum BlockColor {  
        RED(0xffff0000, (byte) 1),
        GREEN(0xff00ff00, (byte) 2),
        BLUE(0xff0000ff, (byte) 3),
        YELLOW(0xffffff00, (byte) 4),
        CYAN(0xff00ffff, (byte) 5),
        WHITE(0xffffffff, (byte) 6),
        MAGENTA(0xffff00ff, (byte) 7),
        TRANSPARENT(0x20320617, (byte) 8);
        private final int color;
        private final byte value;
        private BlockColor(int color, byte value) {
            this.color = color;
            this.value = value;
        }
    }

    public StarGLSurfaceView(Context context, OnSurfacePickedListener onSurfacePickedListener) {
        super(context);
        setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
        mStarRenderer = new StarRenderer(); //创建渲染器
        setZOrderOnTop(true);                      // 透视上一个View 
        setEGLConfigChooser(8, 8, 8, 8, 16, 0); 
        getHolder().setFormat(PixelFormat.TRANSLUCENT); // 透视上一个Activity 
        setRenderer(mStarRenderer);                     //设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        //setFocusableInTouchMode(true);
        //model = new Model();
        //setEGLContextClientVersion(2);
        mStarRenderer.setOnSurfacePickedListener(onSurfacePickedListener);
    } 
    /*public StarGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS); 
        setEGLContextClientVersion(2);
        initView();
        }*/

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
            AppConfig.gbNeedPick = false; 
            break; 
        case MotionEvent.ACTION_DOWN: 
            AppConfig.gbNeedPick = false; 
            break; 
        case MotionEvent.ACTION_UP: 
            AppConfig.gbNeedPick = true; 
            break; 
        case MotionEvent.ACTION_CANCEL: 
            AppConfig.gbNeedPick = false; 
            break; 
        } 
        mPreviousX = x; 
        mPreviousY = y; 
        return true; 
    }
    
    public void setModel(Model model) { this.model = model; }
	public void setActivity(ActivityGame activity) { this.activity = activity; }
    public void onPause() { super.onPause();  } 
    public void onResume() { super.onResume();  } 
	//public void endGame() { }
	//public void pauseGame() { //model.setGamePaused(); }
}
