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
//import dev.ttetris.util.MatrixState;
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
/*
Activity使用了自定义的GLSurfaceView的子类, 这样，我们可以开发出和用户交互的应用，比如游戏等。 
需要注意的是：由于渲染对象是运行在一个独立的渲染线程中，所以需要采用跨线程的机制来进行事件的处理。
但是Android提供了一个简便的方法, 我们只需要在事件处理中使用queueEvent(Runnable)就可以了.
*/  
public class StarGLSurfaceView extends GLSurfaceView {
    private StarRenderer mStarRenderer; 
    private float mPreviousX, mPreviousY; 
	private ActivityGame activity;
    public int DELAY = 100;
	private long lastMove = 0;
	public static final float ANGLE_SPAN = 0.375f;
	RotateThread rthread;

    public enum Color { // set in Block, may need it before set textures
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
        private Color(int color, byte value) {
            this.color = color;
            this.value = value;
        }
    }
    
    public StarGLSurfaceView(Context context, OnSurfacePickedListener onSurfacePickedListener) {
        super(context);
        setEGLContextClientVersion(2);
        setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
        mStarRenderer = new StarRenderer(); 
        setEGLConfigChooser(8, 8, 8, 8, 16, 0); 
        setRenderer(mStarRenderer);                      
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);  
        setFocusableInTouchMode(true);
        mStarRenderer.setOnSurfacePickedListener(onSurfacePickedListener);
    } 

	public void setActivity(ActivityGame activity) { this.activity = activity; }
    public void onPause() {  super.onPause(); }
    public void onResume() { super.onResume(); }

    public class RotateThread extends Thread {
        public boolean flag = true;
        @Override
        public void run() {
            while (flag) {
                mStarRenderer.frame.xAngle = mStarRenderer.frame.xAngle + ANGLE_SPAN;
                mStarRenderer.grid.xAngle = mStarRenderer.grid.xAngle + ANGLE_SPAN;
                mStarRenderer.cube.xAngle = mStarRenderer.cube.xAngle + ANGLE_SPAN;

                mStarRenderer.currBlock.xAngle = mStarRenderer.currBlock.xAngle + ANGLE_SPAN;
                mStarRenderer.nextBlock.xAngle = mStarRenderer.nextBlock.xAngle + ANGLE_SPAN;
                mStarRenderer.nextBlock.setActiveFlag(true); 
                mStarRenderer.setBoardRotatingAngle(ANGLE_SPAN);

                try {
                    Thread.sleep(20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class StarRenderer implements GLSurfaceView.Renderer {
        private OnSurfacePickedListener onSurfacePickedListener; 
        private Frame frame;
        private Grid grid;
        private Cube cube;
        private Block currBlock;
        private Block nextBlock;
        private float mAngle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.5f);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            GLES20.glEnable(GLES20.GL_TEXTURE_2D); // for textures
            
            frame = new Frame(StarGLSurfaceView.this, 5, 10);
            grid = new Grid(StarGLSurfaceView.this, 5);
            //cube = new Cube(StarGLSurfaceView.this, CubeColor.Anchient, 0, 0, 0); // E i J
            cube = new Cube(StarGLSurfaceView.this, CubeColor.Amethyst, 0, 0, 0); // E i J
            //currBlock = new Block(StarGLSurfaceView.this, new BlockMeta(CubeColor.Anchient, BlockType.squareType, 1f, 1f, 0f));
            currBlock = new Block(StarGLSurfaceView.this, new BlockMeta(CubeColor.Amethyst, BlockType.squareType, 1f, 1f, 0f));
            nextBlock = new Block(StarGLSurfaceView.this, new BlockMeta(CubeColor.Amethyst, BlockType.lineType, 3f, 1.0f, 0f));
            rthread = new RotateThread();
			rthread.start();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int w, int h) {
            GLES20.glViewport(0, 0, w, h);    
            float ratio = (float) w / h;
            Constant.ratio = ratio;
            Matrix.frustumM(Frame.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10); // 投影距阵
            Matrix.frustumM(Grid.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10); 
            Matrix.frustumM(Cube.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10); 
            Matrix.frustumM(Block.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10);

            Matrix.setLookAtM(Frame.mVMatrix, 0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            Matrix.setLookAtM(Grid.mVMatrix,  0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            Matrix.setLookAtM(Cube.mVMatrix,  0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            Matrix.setLookAtM(Block.mVMatrix,  0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            //frame.drawSelf();
            //grid.drawSelf();
            cube.drawSelf();

            currBlock.drawSelf();
            nextBlock.drawSelf();
            renderBoard();
        }

        public void setOnSurfacePickedListener(OnSurfacePickedListener onSurfacePickedListener) { 
            this.onSurfacePickedListener = onSurfacePickedListener; 
        }

        public void setBoardRotatingAngle(float angle) {
            Model.setBoardRotatingAngle(angle);
        }

        public CubeColor getCubeColor(int val) {
            for (Color item : Color.values()) 
                if (val == item.value) 
                    return CubeColor.Amethyst; // item.color;
            return CubeColor.Hidden;
        } 
        
        public void renderBoard() {
            for (int k = 0; k < Model.HGT; k++) {
                for (int j = 0; j < Model.COL; j++) {
                    for (int i = 0; i < Model.ROW; i++) {
                        if (Model.board[i][j][k] != 0) {
                            //Cube cube = new Cube(StarGLSurfaceView.this, getCubeColor(Model.board[i][j][k]), i, j, k);
                            Cube cube = new Cube(StarGLSurfaceView.this, CubeColor.Amethyst, i, j, k);
                            cube.xAngle = Model.getBoardRotatingAngle();
                            cube.drawSelf();
                        }
                    }
                }
            }
        }
        
    }
}
