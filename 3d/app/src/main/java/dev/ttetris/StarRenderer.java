package dev.ttetris;
/*
 */
import dev.ttetris.model.Cube;
import dev.ttetris.model.CubeColor;
import dev.ttetris.model.Block;
import dev.ttetris.model.BlockMeta;
import dev.ttetris.model.BlockType;
import dev.ttetris.model.Model;
import dev.ttetris.model.Frame;
import dev.ttetris.model.Grid;
import dev.ttetris.model.Constant;
import dev.ttetris.shader.ColorShaderProgram;
import dev.ttetris.util.TextureHelper;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
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
    private ColorShaderProgram colorProgram;
    private int texture;
    public StarRenderer(Context context) { this.context = context; }
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1f, 1f, 1f, 1.0f); // 设置屏幕背景色 RGBA
        GLES20.glEnable(GLES20.GL_DEPTH_TEST); // 打开深度检测
        GLES20.glDisable(GLES20.GL_CULL_FACE); // 关闭背面剪裁

        this.colorProgram = new ColorShaderProgram(context);
        frame = new Frame(5, 10);
        //grid = new Grid(5);
        cube = new Cube(this.context, CubeColor.Amethyst, 0, 0, 0);   // E i J 
        currBlock = new Block(this.context, new BlockMeta(CubeColor.Amethyst, BlockType.squareType, 1f, 1f, 0f));
        nextBlock = new Block(this.context, new BlockMeta(CubeColor.Oak, BlockType.lineType, 3f, 1.0f, 0f));

        rthread = new RotateThread();
        rthread.start();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT); // 清除深度缓冲与颜色缓冲

        this.colorProgram.useProgram();
        this.colorProgram.setUniforms(frame.getFinalMatrix());
        frame.bindData(this.colorProgram);
        frame.draw(); 
        //grid.drawSelf();

        this.texture = TextureHelper.loadTexture(context, R.drawable.cubeoak);
        renderBoard();

        this.texture = -1;
        this.texture = TextureHelper.loadTexture(context, R.drawable.cubeamethyst);
        currBlock.draw(this.texture);
        //nextBlock.draw(this.texture);

        this.texture = -1;
        this.texture = TextureHelper.loadTexture(context, R.drawable.cubelapislazuli);
        this.cube.draw(texture); 
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        GLES20.glViewport(0, 0, w, h);
        float ratio = (float) w / h;
        Constant.ratio = ratio;
        Matrix.perspectiveM(Frame.mProjMatrix, 0, 45f, ratio, 1f, 10f); // 投影距阵
        Matrix.perspectiveM(Grid.mProjMatrix, 0, 45f, ratio, 1f, 10f); 
        Matrix.perspectiveM(Cube.mProjMatrix, 0, 45f, ratio, 1f, 10f); 
        Matrix.perspectiveM(Block.mProjMatrix, 0, 45f, ratio, 1f, 10f);

        Matrix.setLookAtM(Frame.mVMatrix, 0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.setLookAtM(Grid.mVMatrix,  0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.setLookAtM(Cube.mVMatrix,  0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.setLookAtM(Block.mVMatrix, 0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
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
                        Cube cube = new Cube(context, CubeColor.Oak, i, j, k); // too many repeats
                        cube.xAngle = Model.getBoardRotatingAngle();
                        //this.texture = ; // set color
                        cube.draw(this.texture);
                    }
                }
            }
        }
    }
}
