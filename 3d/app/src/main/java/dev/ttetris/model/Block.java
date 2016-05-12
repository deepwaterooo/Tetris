package dev.ttetris.model;

import dev.ttetris.StarGLSurfaceView;
import dev.ttetris.model.Constant;
import dev.ttetris.util.Shader;
import dev.ttetris.util.Vector3f;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Block implements Cloneable, Serializable {
	public static float[] mVMatrix = new float[16];
	public static float[] mProjMatrix = new float[16];
	public static float[] mMVPMatrix = new float[16];
    public static float[] mMMatrix = new float[16]; // 具体物体的移动旋转矩阵，旋转、平移
    private static final Random RANDOM = new Random();
    private static StarGLSurfaceView mStarGLSurfaceView;
    private final int cubeCounts = 4;
    private final float [] activeBlockCenter = {2.5f, 2.5f, 0f};   // z 9.0f
    private static HashMap<String, BlockMeta> blocks = new HashMap();
    static {
        createMetaBlock("Square", CubeColor.Anchient, BlockType.squareType, .5f, .5f, 0f); // 田
        createMetaBlock("Line", CubeColor.Amethyst, BlockType.lineType, .0f, .5f, 0f);     // (0, .5, 0)
        createMetaBlock("LeftLightning", CubeColor.Oak, BlockType.leftLightningType, .5f, 1.5f, 0f); // (.5, 1, 0)
        createMetaBlock("RightLightning", CubeColor.MarbleRough, BlockType.rightLightningType, .5f, 1.5f, 0f); // (.5, 1, 0)
        createMetaBlock("Roof", CubeColor.LapisLazuli, BlockType.roofType, 0.5f, .5f, 0f);         // (0, .5, 0)
        createMetaBlock("LeftBoot", CubeColor.WhiteMarble, BlockType.leftBootType, .5f, 1.5f, 0f); // (.5, 1, 0)
        createMetaBlock("RightBoot", CubeColor.Marble, BlockType.rightBootType, .5f, 1.5f, 0f);    // (.5, 1, 0)
    }
    private Cube[] cubes;
    private CubeColor color;
    public float centerX;
    public float centerY;
    public float centerZ;
    public float xAngle = 0f;  // direction x y z
    private String curr;
    public static String[] getBlockNames() { return (String[])blocks.keySet().toArray(new String[0]); }
    private static void createMetaBlock(String paramString, CubeColor paramCubeColor, CubeShift[] paramArrayOfCubeShift,
                                        float paramFloat1, float paramFloat2, float paramFloat3) {
        blocks.put(paramString, new BlockMeta(paramCubeColor, paramArrayOfCubeShift, paramFloat1, paramFloat2, paramFloat3));
    }
    
    private Block() { this.cubes = null; }
    public Block(StarGLSurfaceView mv, BlockMeta paramBlockMeta) {
        this.mStarGLSurfaceView = mv;
        currBlock = false;
        for (String key : blocks.keySet()) {
            if (paramBlockMeta.getShifts() == blocks.get(key).getShifts())
                curr = key;
        }
        CubeShift[] arrayOfCubeShift = paramBlockMeta.getShifts();
        this.cubes = new Cube[arrayOfCubeShift.length]; 
        for (int m = 0; ; m++) {
            if (m >= arrayOfCubeShift.length) {
                this.centerX = paramBlockMeta.getCenterX();
                this.centerY = paramBlockMeta.getCenterY();
                this.centerZ = paramBlockMeta.getCenterZ();
                return;
            }
            this.cubes[m] = new Cube(paramBlockMeta.getColor(), arrayOfCubeShift[m].getDx(), arrayOfCubeShift[m].getDy(), arrayOfCubeShift[m].getDz());
        }
    }

    public Cube[] getCubes() { return this.cubes; }
    public CubeColor getColor() {
        CubeColor color = cubes[0].getColor();
        return color;
    }

    public Block clone() {
        Block localBlock = new Block();
        localBlock.cubes = new Cube[this.cubes.length];
        for (int i = 0; i < this.cubes.length; i++)
            localBlock.cubes[i] = this.cubes[i].clone();
        return localBlock;
    }
    /*
		Matrix.translateM(mMMatrix, 0, -0.5f, -0.5f, -0.5f); // 设置平移 (-Cx, -Cy, -Cz) to cube coordinate center (0, 0, 0)
        Matrix.rotateM(mMMatrix, 0, xAngle, 0, 0, 1);        // rotate around the center
		Matrix.translateM(mMMatrix, 0, 0.5f, 0.5f, 0.5f);    // 设置平移 (Cx, Cy, Cz) back to cube center before rotate
    */
    private boolean currBlock;
    public void setCurrBlock(boolean v) {
        this.currBlock = v;
    }
    public boolean getCurrBlock() {
        return this.currBlock;
    }
        
    public void drawSelf(int i){
		GLES20.glUseProgram(mProgram);
        initVertexData(i);
		Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);          // 初始化变换矩阵
		Matrix.translateM(mMMatrix, 0, 0.5f, 0.5f, 0.5f);    // 设置平移 （.5, .5, .5） y opposite direction
        Matrix.translateM(mMMatrix, 0, -1f, -1.5f, -0.5f); // 设置平移 (-Cx, -Cy, -Cz) to cube coordinate center (0, 0, 0)
        Matrix.rotateM(mMMatrix, 0, xAngle, 0, 0, 1);        // rotate around block center, static
        Matrix.translateM(mMMatrix, 0, 1f, 1.5f, 0.5f);    // 设置平移 (Cx, Cy, Cz) back to cube center before rotate
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, Block.getFinalMatrix(mMMatrix), 0);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, mVertexBuffer);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 4, mColorBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glLineWidth(3.0f);
        GLES20.glDrawElements(GLES20.GL_LINE_LOOP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
	}
    public void drawSelf() {
        if (Cube.mProjMatrix == null || Cube.mVMatrix == null) {
            float ratio = Constant.ratio;
            Matrix.frustumM(Cube.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
            Matrix.setLookAtM(Cube.mVMatrix, 0, -1.5f, -4.5f, 3.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f); // should be passed in somehow
        }
        //Cube[] cubes = getCubes();
        for (int i = 0; i < cubeCounts; i++) {
            Cube[] cubes = getCubes();
            cubes[i].initShader(mStarGLSurfaceView);
            if (curr != null && curr.equals("Square")) { // (.5, 1, 0)
                //Constant.currBlock = true;
                cubes[i].setCurrBlock(true);
                //shiftBlock(activeBlockCenter[0] - this.centerX, activeBlockCenter[1] - this.centerY, activeBlockCenter[2] - this.centerZ); // to the correct center
                //shiftBlock(1f, 1.5f, 0.5f); // 设置平移 (Cx, Cy, Cz) to cube coordinate center (0, 0, 0)
                shiftBlock(-this.centerX, -this.centerY, -this.centerZ);
                cubes[i].setCoordinates();
                cubes[i].xAngle = this.xAngle;// * 2;
                cubes[i].drawSelf();
                shiftBlock(this.centerX, this.centerY, this.centerZ);
                //shiftBlock(-1f, -1.5f, -0.5f); // 设置平移 (-Cx, -Cy, -Cz) to cube coordinate center (0, 0, 0)
                cubes[i].setCoordinates();
                /*
                initVertexData(i);
                GLES20.glUseProgram(mProgram);
                Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);          // 初始化变换矩阵
                Matrix.translateM(mMMatrix, 0, 0.5f, 0.5f, 0.5f);    // 设置平移 （.5, .5, .5） y opposite direction
                Matrix.translateM(mMMatrix, 0, -1f, -1.5f, -0.5f); // 设置平移 (-Cx, -Cy, -Cz) to cube coordinate center (0, 0, 0)
                Matrix.rotateM(mMMatrix, 0, xAngle, 0, 0, 1);        // rotate around block center, static
                Matrix.translateM(mMMatrix, 0, 1f, 1.5f, 0.5f);    // 设置平移 (Cx, Cy, Cz) back to cube center before rotate
                */
                //cubes[i].setCoordinates();
                //cubes[i].initVertexData();
                /*
                Matrix.setRotateM(Cube.mMMatrix, 0, 0, 0, 1, 0);          // 初始化变换矩阵
                Matrix.translateM(Cube.mMMatrix, 0, 0.5f, 0.5f, 0.5f);    // 设置平移 （.5, .5, .5） y opposite direction
                Matrix.translateM(Cube.mMMatrix, 0, -this.centerX, -this.centerY, -this.centerZ);    
                //Matrix.translateM(Cube.mMMatrix, 0, -1f, -1.5f, -0.5f); // 设置平移 (-Cx, -Cy, -Cz) to cube coordinate center (0, 0, 0)
                Matrix.rotateM(Cube.mMMatrix, 0, xAngle, 0, 0, 1);        // rotate around block center, static
                Matrix.translateM(Cube.mMMatrix, 0, this.centerX, this.centerY, this.centerZ);
                //Matrix.translateM(Cube.mMMatrix, 0, 1f, 1.5f, 0.5f);    // 设置平移 (Cx, Cy, Cz) back to cube center before rotate
                //drawSelf(i);  
                GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, Block.getFinalMatrix(mMMatrix), 0);
                //GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, Cube.getFinalMatrix(mMMatrix), 0);
                GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, mVertexBuffer);
                GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 4, mColorBuffer);
                GLES20.glEnableVertexAttribArray(mPositionHandle);
                GLES20.glEnableVertexAttribArray(mColorHandle);
                GLES20.glLineWidth(3.0f);
                GLES20.glDrawElements(GLES20.GL_LINE_LOOP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
                              */
                //shiftBlock(this.centerX - activeBlockCenter[0], this.centerY - activeBlockCenter[1], this.centerZ - activeBlockCenter[2]); // to the correct center
                //cubes[i].setCoordinates();
            } else {
                shiftBlock(activeBlockCenter[0] - this.centerX, activeBlockCenter[1] - this.centerY, activeBlockCenter[2] - this.centerZ);
                cubes[i].setCoordinates();
                cubes[i].xAngle = this.xAngle;
                cubes[i].drawSelf();
                shiftBlock(this.centerX - activeBlockCenter[0], this.centerY - activeBlockCenter[1], this.centerZ - activeBlockCenter[2]);
                cubes[i].setCoordinates();
            }
            /*
            if (curr != null && curr.equals("Line")) {
                shiftBlock(-this.centerX, -this.centerY, -this.centerZ);
                cubes[i].setCoordinates();
                cubes[i].xAngle = this.xAngle * 2;
                cubes[i].drawSelf();
                shiftBlock(this.centerX, this.centerY, this.centerZ);
                cubes[i].setCoordinates();
            } else {
                shiftBlock(activeBlockCenter[0] - this.centerX, activeBlockCenter[1] - this.centerY, activeBlockCenter[2] - this.centerZ);
                cubes[i].setCoordinates();
                cubes[i].xAngle = this.xAngle;
                cubes[i].drawSelf();
                shiftBlock(this.centerX - activeBlockCenter[0], this.centerY - activeBlockCenter[1], this.centerZ - activeBlockCenter[2]);
                } */
        }
    }

    public void initVertexData(int i) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(cubes[i].coords.length*4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(cubes[i].coords);
		mVertexBuffer.position(0);
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
		ByteBuffer cbb = ByteBuffer.allocateDirect(cubeColor.length*4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put(cubeColor);
		mColorBuffer.position(0);
    }

    private static final int COORDS_PER_VERTEX = 3;
    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private ShortBuffer drawListBuffer;
	int mProgram;
	int mMVPMatrixHandle;
	int mPositionHandle;
	int mColorHandle;
	String mVertexShader;
	String mFragmentShader;
	FloatBuffer mVertexBuffer;
	FloatBuffer mColorBuffer;
    private static final float cubeColor[] = {0.0f, 0.0f, 0.0f, 1.0f}; // supposed to change
    private static final short drawOrder[] = {0, 1, 2, 3, 0, 4, 5, 1,  1, 2, 6, 5, 5, 6, 7, 4,  7, 6, 2, 3, 3, 7, 4, 0};

    public void initShader(StarGLSurfaceView mv) {                                // should I set it to be static ?
		mVertexShader = Shader.loadFromAssetsFile("vertex.sh", mv.getResources());
		mFragmentShader = Shader.loadFromAssetsFile("frag.sh", mv.getResources());		
		mProgram = Shader.createProgram(mVertexShader, mFragmentShader);
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	}
    
	public static float[] getFinalMatrix(float[] spec) {
		mMVPMatrix = new float[16];
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
		return mMVPMatrix;
	}

    public void shiftBlock(float paramFloat1, float paramFloat2, float paramFloat3) { // 平移
        /*int i = cubes.length;
        for (int j = 0; ; j++) {
            if (j >= i) {
                this.centerX += paramFloat1;
                this.centerY += paramFloat2;
                this.centerZ += paramFloat3;
                return;
            }
            //Cube localCube = cubes[j];
            cubes[j].setX(paramFloat1 + cubes[j].getX());
            cubes[j].setY(paramFloat2 + cubes[j].getY());
            cubes[j].setZ(paramFloat3 + cubes[j].getZ());
            }*/
        Cube[] arrayOfCube = getCubes();
        float i = arrayOfCube.length;
        for (int j = 0; ; j++) {
            if (j >= i) {
                this.centerX += paramFloat1;
                this.centerY += paramFloat2;
                this.centerZ += paramFloat3;
                return;
            }
            Cube localCube = arrayOfCube[j];
            localCube.setX(paramFloat1 + localCube.getX());
            localCube.setY(paramFloat2 + localCube.getY());
            localCube.setZ(paramFloat3 + localCube.getZ());
        }
    }

    /*
    public boolean rotateBlockLeft(Cube[] paramArrayOfCube) { // around z, anti-clock wise
        int[] arrayOfInt1 = new int[this.cubes.length];
        int[] arrayOfInt2 = new int[this.cubes.length];
        int[] arrayOfInt3 = new int[this.cubes.length];
        int i = 0;
        if (i >= this.cubes.length);
        for (int i1 = 0; ; i1++) {
            if (i1 >= this.cubes.length) { // 4
                boolean bool = true;
                //label45: return bool;
                arrayOfInt1[i] = Math.round(this.centerX - (this.cubes[i].getY() - this.centerY));
                arrayOfInt2[i] = Math.round(this.centerY + (this.cubes[i].getX() - this.centerX));
                //arrayOfInt3[i] = Math.round(this.centerZ + (this.cubes[i].getX() - this.centerX)); // ?
                int j = paramArrayOfCube.length;
                for (int k = 0; ; k++) {
                    if (k >= j) {                        
                            i++;
                            break;
                        }
                    Cube localCube = paramArrayOfCube[k];
                    if (localCube.getX() == arrayOfInt1[i]) {                        
                            int m = localCube.getY();
                            int n = arrayOfInt2[i];
                            bool = false;
                            if (m == n)
                                //break label45;
                                break;
                        }
                }
            }
            this.cubes[i1].setX(arrayOfInt1[i1]);
            this.cubes[i1].setY(arrayOfInt2[i1]);
            this.cubes[i1].setZ(arrayOfInt3[i1]);
        }
    }
*/
    /*    
    public void invertX() { //沿Y轴旋转
        Cube[] arrayOfCube = this.cubes;
        int i = arrayOfCube.length;
        for (int j = 0; ; j++) {
            if (j >= i) {
                this.centerX = (-this.centerX);
                return;
            }
            Cube localCube = arrayOfCube[j];
            localCube.setX(-localCube.getX());
        }
    }

    public boolean moveBlock(Cube[] paramArrayOfCube, int paramInt1, int paramInt2, int paramInt3) {
        Cube[] arrayOfCube = getCubes();
        int i = arrayOfCube.length; // 4
        int j = 0;
        boolean bool;
        if (j >= i) {
            shiftBlock(paramInt1, paramInt2, paramInt3);
            bool = true;
            label30: { return bool; }
        }
        Cube localCube1 = arrayOfCube[j];
        int k = paramArrayOfCube.length; // 4
        for (int m = 0; ; m++) {
            if (m >= k) {
                j++;
                break;
            }
            Cube localCube2 = paramArrayOfCube[m];
            if (localCube2.getX() == paramInt1 + localCube1.getX()) {
                int n = localCube2.getY();
                int i1 = paramInt2 + localCube1.getY();
                bool = false;
                if (n == i1)
                    if (j >= i) {
                        shiftBlock(paramInt1, paramInt2, paramInt3);
                        bool = true;
                        return bool;
                    }
            }
        }
        return false; // added for debugging, never reach here
    }

    // these two functions have problems, supposed to change dramatically 
    public boolean rotateBlockRight(Cube[] paramArrayOfCube) {
        int[] arrayOfInt1 = new int[this.cubes.length];
        int[] arrayOfInt2 = new int[this.cubes.length];
        int[] arrayOfInt3 = new int[this.cubes.length];
        int i = 0;
        if (i >= this.cubes.length);
        for (int i1 = 0; ; i1++) {
            if (i1 >= this.cubes.length) {
                boolean bool = true;
                //label45: return bool;
                arrayOfInt1[i] = Math.round(this.centerX + (this.cubes[i].getY() - this.centerY));
                arrayOfInt2[i] = Math.round(this.centerY - (this.cubes[i].getX() - this.centerX));
                int j = paramArrayOfCube.length;
                for (int k = 0; ; k++) {
                    if (k >= j) {                        
                            i++;
                            break;
                        }
                    Cube localCube = paramArrayOfCube[k];
                    if (localCube.getX() == arrayOfInt1[i]) {                        
                            int m = localCube.getY();
                            int n = arrayOfInt2[i];
                            bool = false;
                            if (m == n)
                                //break label45;
                                break;
                        }
                }
            }
            this.cubes[i1].setX(arrayOfInt1[i1]);
            this.cubes[i1].setY(arrayOfInt2[i1]);
            this.cubes[i1].setZ(arrayOfInt3[i1]);
        }
    }
    */
}
