package dev.ttetris;

import dev.ttetris.model.Table;
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

import dev.ttetris.TextureShaderProgram;
import dev.ttetris.MatrixHelper;
import dev.ttetris.ShaderHelper;
import dev.ttetris.TextResourceReader;
import dev.ttetris.TextureHelper;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class StarRenderer implements GLSurfaceView.Renderer {
    public static final float ANGLE_SPAN = 0.375f;
    private OnSurfacePickedListener onSurfacePickedListener; 
    private final Context context;
    private RotateThread rthread;
    private Frame frame;
    private Grid grid;
    private Cube cube;
    private Block currBlock;
    private Block nextBlock;
    private float mAngle;
    public StarRenderer(Context context) { this.context = context; }

    private final float[] projectionMatrix=new float[16];
    private final float[] modelMatrix=new float[16];
    private Table table;
    private TextureShaderProgram textureProgram;
    //private ColorShaderProgram colorProgram;
    private int texture;
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(.5f, .5f, .5f, .5f);

        this.table = new Table();
        this.textureProgram = new TextureShaderProgram(context);
        this.texture = TextureHelper.loadTexture(context, R.drawable.cubeoak);
        
        //GLES20.glEnable(GLES20.GL_TEXTURE_2D);  // for textures
        //GLES20.glEnable(GLES20.GL_BLEND);
        //GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        //GLES20.glEnable(GLES20.GL_DITHER);
        //GLES20.glClearColor(0.5f, 0.5f, 0.5f, .5f);

        //GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
            
        //frame = new Frame(context, 5, 10);
        //grid = new Grid(context, 5);
        //cube = new Cube(context, CubeColor.Anchient, 0, 0, 0); // E i J
        cube = new Cube(context, CubeColor.Amethyst, 0, 0, 0); // E i J
        //currBlock = new Block(context, new BlockMeta(CubeColor.Anchient, BlockType.squareType, 1f, 1f, 0f));
        currBlock = new Block(context, new BlockMeta(CubeColor.Amethyst, BlockType.squareType, 1f, 1f, 0f));
        nextBlock = new Block(context, new BlockMeta(CubeColor.Amethyst, BlockType.lineType, 3f, 1.0f, 0f));
        rthread = new RotateThread();
        rthread.start();
        
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //GLES20.glClearColor(0f, 0f, 0f, 1f);
        //GLES20.glColorMask(true, true, true, true);
        //GLES20.glClear(16384);

        //frame.drawSelf();
        //grid.drawSelf();
        cube.drawSelf();

        //currBlock.drawSelf();
        //nextBlock.drawSelf();
        //renderBoard();

        this.textureProgram.useProgram();
        this.textureProgram.setUniforms(projectionMatrix,this.texture);
        //this.table.bindData(this.textureProgram);
        //this.table.draw(); 
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        GLES20.glViewport(0, 0, w, h);
        float ratio = (float) w / h;
        Constant.ratio = ratio;
        /*Matrix.frustumM(Frame.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10); // 投影距阵
        Matrix.frustumM(Grid.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10); 
        Matrix.frustumM(Cube.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10); 
        Matrix.frustumM(Block.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10); */
        Matrix.perspectiveM(Frame.mProjMatrix, 0, 45f, ratio, 1f, 10f); // 投影距阵
        Matrix.perspectiveM(Grid.mProjMatrix, 0, 45f, ratio, 1f, 10f); 
        Matrix.perspectiveM(Cube.mProjMatrix, 0, 45f, ratio, 1f, 10f); 
        Matrix.perspectiveM(Block.mProjMatrix, 0, 45f, ratio, 1f, 10f);

        Matrix.setLookAtM(Frame.mVMatrix, 0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.setLookAtM(Grid.mVMatrix,  0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.setLookAtM(Cube.mVMatrix,  0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.setLookAtM(Block.mVMatrix,  0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        /*
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) w / (float) h, 1f, 10f);
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix,0,0f,0f,-2.5f);
        Matrix.rotateM(modelMatrix,0,-60f,1f,0f,0f);
        final float[] temp=new float[16];
        Matrix.multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);   */
    }

    public class RotateThread extends Thread {
        public boolean flag = true;
        @Override
        public void run() {
            while (flag) {
                //frame.xAngle = frame.xAngle + ANGLE_SPAN;
                //grid.xAngle = grid.xAngle + ANGLE_SPAN;
                cube.xAngle = cube.xAngle + ANGLE_SPAN;

                currBlock.xAngle = currBlock.xAngle + ANGLE_SPAN;
                nextBlock.xAngle = nextBlock.xAngle + ANGLE_SPAN;
                nextBlock.setActiveFlag(true); 
                setBoardRotatingAngle(ANGLE_SPAN);

                try {
                    Thread.sleep(20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setOnSurfacePickedListener(OnSurfacePickedListener onSurfacePickedListener) { 
        this.onSurfacePickedListener = onSurfacePickedListener; 
    }

    public void setBoardRotatingAngle(float angle) {
        Model.setBoardRotatingAngle(angle);
    }

    public CubeColor getCubeColor(int val) {
        for (CubeColor item : CubeColor.values()) 
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
                        Cube cube = new Cube(context, CubeColor.Amethyst, i, j, k);
                        cube.xAngle = Model.getBoardRotatingAngle();
                        cube.drawSelf();
                    }
                }
            }
        }
    }
}
