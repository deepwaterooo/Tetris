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
Activityʹ�����Զ����GLSurfaceView������, ���������ǿ��Կ��������û�������Ӧ�ã�������Ϸ�ȡ� 
��Ҫע����ǣ�������Ⱦ������������һ����������Ⱦ�߳��У�������Ҫ���ÿ��̵߳Ļ����������¼��Ĵ���
����Android�ṩ��һ�����ķ���, ����ֻ��Ҫ���¼�������ʹ��queueEvent(Runnable)�Ϳ�����.
*/  
public class StarGLSurfaceView extends GLSurfaceView {
    private StarRenderer mStarRenderer; 
    private float mPreviousX, mPreviousY; 
	private ActivityGame activity;
    public int DELAY = 100;
	private long lastMove = 0;
	public static final float ANGLE_SPAN = 0.375f;
	RotateThread rthread;

    public enum BlockColor { // set in Block, may need it before set textures
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
        setEGLContextClientVersion(2);
        // Ϊ�˿��Լ���log�ʹ����飬��������3DӦ�ã���Ҫ����setDebugFlags()��  
        setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
        mStarRenderer = new StarRenderer(); 
        setEGLConfigChooser(8, 8, 8, 8, 16, 0); 
        setRenderer(mStarRenderer);                      
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // requestRender(); doesn't make much difference
        getHolder().setFormat(PixelFormat.TRANSLUCENT);  
        setFocusableInTouchMode(true);
        mStarRenderer.setOnSurfacePickedListener(onSurfacePickedListener);
    } 
    /*
    public boolean onTouchEvent(final MotionEvent event) {  
        // ���� mStarRenderer ������������һ���߳��У�������ÿ��̵߳Ļ��ƽ��д���ʹ��queueEvent����  
        //��ȻҲ����ʹ��������Synchronized������UI�̺߳���Ⱦ�߳̽���ͨ�š�  
        queueEvent(new Runnable() {  
                @Override  
                public void run() {  
                    //mRenderer.setColor(event.getX()/getWidth(), event.getY()/getHeight(), 1.0f);
                    //mRenderer.setAngle(mRenderer.getAngle() + ((dx + dy) * TOUCH_SCALE_FACTOR));
                    // mStarRenderer.doSomething()
                    requestRender();
                }  
            });  
        return true;  
        } */ 
    /*
    public boolean onTouchEvent(final MotionEvent e) { 
        float x = e.getX();
        float y = e.getY();
        AppConfig.setTouchPosition(x, y);  
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
        }*/

    //public void setModel(Model model) { this.model = model; }
	public void setActivity(ActivityGame activity) { this.activity = activity; }
    public void onPause() { // suppose to add view methods for pause/resume as well
        super.onPause();
    } 
    public void onResume() {
        super.onResume();
    }

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
                mStarRenderer.setBoardCubeRotating(ANGLE_SPAN);

                try {
                    Thread.sleep(20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class StarRenderer implements GLSurfaceView.Renderer {
        private Frame frame;
        private Grid grid;
        private Cube cube;
        private Block currBlock;
        private Block nextBlock;
        //private Model model = new Model();

        private final int unitSize = 1;
        private OnSurfacePickedListener onSurfacePickedListener; 
        public float mfAngleX = 0.0f; 
        public float mfAngleY = 0.0f; 
        public float gesDistance = 0.0f; 
        private float one = 1.0f; 

        private float mAngle;
        //public volatile float mAngle; // volatile
        //public float getAngle() { return mAngle; }
        //public void setAngle(float angle) { mAngle = angle; }

        //public void setModel(Model model) { this.model = model; }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // grey
            GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.5f);

            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            frame = new Frame(StarGLSurfaceView.this, 5, 10);
            grid = new Grid(StarGLSurfaceView.this, 5);
            cube = new Cube(StarGLSurfaceView.this, CubeColor.Anchient, 0, 0, 0); // E i J
            currBlock = new Block(StarGLSurfaceView.this, new BlockMeta(CubeColor.Anchient, BlockType.squareType, 1f, 1f, 0f));
            nextBlock = new Block(StarGLSurfaceView.this, new BlockMeta(CubeColor.Amethyst, BlockType.lineType, 3f, 1.0f, 0f));
            rthread = new RotateThread();
			rthread.start();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int w, int h) {
            GLES20.glViewport(0, 0, w, h);    
            float ratio = (float) w / h;
            Constant.ratio = ratio;
            Matrix.frustumM(Frame.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10); // ͶӰ����
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
            frame.drawSelf();
            grid.drawSelf();
            cube.drawSelf();

            currBlock.drawSelf();
            nextBlock.drawSelf();
            renderBoard();
        }

        public void setOnSurfacePickedListener(OnSurfacePickedListener onSurfacePickedListener) { 
            this.onSurfacePickedListener = onSurfacePickedListener; 
        }

        public void setBoardCubeRotating(float angle) {
            Model.setBoardRotatingAngle(angle);
        }

        public void renderBoard() {
            for (int k = 0; k < Model.HGT; k++) {
                for (int j = 0; j < Model.COL; j++) {
                    for (int i = 0; i < Model.ROW; i++) {
                        if (Model.board[i][j][k] != 0) {
                            Cube cube = new Cube(StarGLSurfaceView.this, CubeColor.Anchient, i, j, k);
                            cube.xAngle = Model.getBoardRotatingAngle();
                            cube.drawSelf();
                        }
                    }
                }
            }
        }
        
    }
}
