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
import dev.ttetris.shader.TextureShaderProgram;
import dev.ttetris.shader.ColorShaderProgram;
import dev.ttetris.util.MatrixHelper;
import dev.ttetris.util.ShaderHelper;
import dev.ttetris.util.TextResourceReader;
import dev.ttetris.util.TextureHelper;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.content.Context;
import android.view.MotionEvent;
import android.graphics.PixelFormat;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.Matrix;

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
    //private final float[] projectionMatrix = new float[16];
    //private final float[] modelMatrix = new float[16];
    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private int texture;
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1f, 1f, 1f, .5f);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        this.textureProgram = new TextureShaderProgram(context);
        this.colorProgram = new ColorShaderProgram(context);
        frame = new Frame(5, 10);
        //grid = new Grid(5);
        cube = new Cube(CubeColor.Amethyst, 0, 0, 0);   // E i J
        currBlock = new Block(new BlockMeta(CubeColor.Oak, BlockType.squareType, 1f, 1f, 0f));
        nextBlock = new Block(new BlockMeta(CubeColor.Oak, BlockType.lineType, 3f, 1.0f, 0f));
        rthread = new RotateThread();
        rthread.start();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        this.colorProgram.useProgram();
        this.colorProgram.setUniforms(frame.getFinalMatrix());
        frame.bindData(this.colorProgram);
        frame.draw(); 
        //grid.drawSelf();

        this.texture = TextureHelper.loadTexture(context, R.drawable.cubeamethyst);
        this.textureProgram.useProgram();
        this.textureProgram.setUniforms(cube.getFinalMatrix(), this.texture);
        this.cube.bindData(this.textureProgram);
        cube.draw();
        /*
        this.texture = TextureHelper.loadTexture(context, R.drawable.cubeoak);
        this.textureProgram = new TextureShaderProgram(context);
        this.textureProgram.useProgram();
        this.textureProgram.setUniforms(projectionMatrix, this.texture);
        currBlock.drawSelf();
        */
        //nextBlock.drawSelf();
        renderBoard();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        GLES20.glViewport(0, 0, w, h);
        float ratio = (float) w / h;
        Constant.ratio = ratio;
        Matrix.perspectiveM(Frame.mProjMatrix, 0, 45f, ratio, 1f, 10f); // Õ∂”∞æ‡’Û
        Matrix.perspectiveM(Grid.mProjMatrix, 0, 45f, ratio, 1f, 10f); 
        Matrix.perspectiveM(Cube.mProjMatrix, 0, 45f, ratio, 1f, 10f); 
        Matrix.perspectiveM(Block.mProjMatrix, 0, 45f, ratio, 1f, 10f);

        Matrix.setLookAtM(Frame.mVMatrix, 0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.setLookAtM(Grid.mVMatrix,  0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.setLookAtM(Cube.mVMatrix,  0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.setLookAtM(Block.mVMatrix,  0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        //MatrixHelper.perspectiveM(projectionMatrix, 45, (float) w / (float) h, 1f, 10f);
        /*Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);
        final float[] temp=new float[16];
        Matrix.multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
*/
    }

    public class RotateThread extends Thread {
        public boolean flag = true;
        @Override
        public void run() {
            while (flag) {
                frame.xAngle = frame.xAngle + ANGLE_SPAN;
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
                return CubeColor.Amethyst; 
        return CubeColor.Hidden;
    } 
        
    public void renderBoard() {
        for (int k = 0; k < Model.HGT; k++) {
            for (int j = 0; j < Model.COL; j++) {
                for (int i = 0; i < Model.ROW; i++) {
                    if (Model.board[i][j][k] != 0) {
                        Cube cube = new Cube(CubeColor.Amethyst, i, j, k); // getCubeColor(Model.board[i][j][k])
                        cube.xAngle = Model.getBoardRotatingAngle();
                        this.textureProgram = new TextureShaderProgram(context);
                        this.textureProgram.useProgram();  
                        this.textureProgram.setUniforms(cube.getFinalMatrix(), this.texture); // supposed to change
                        this.cube.bindData(this.textureProgram);
                        cube.draw();
                    }
                }
            }
        }
    }
}
