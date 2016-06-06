package dev.ttetris.model;
/*
*/
import dev.ttetris.model.Constant;
import android.opengl.Matrix;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import android.content.Context;

public class Block implements Cloneable, Serializable {
	public static float[] mVMatrix = new float[16];
	public static float[] mProjMatrix = new float[16];
	public static float[] mMVPMatrix = new float[16];
    public static float[] mMMatrix = new float[16];              // ����������ƶ���ת������ת��ƽ��
    //private static final Random RANDOM = new Random();
    private final int cubeCounts = 4;
    private final float [] activeBlockCenter = {2.5f, 2.5f, 1f}; // z 9.0f
    private static HashMap<String, BlockMeta> blocks = new HashMap<String, BlockMeta>();
    Context context;
    static {
        createMetaBlock("Square", CubeColor.Anchient, BlockType.squareType, .5f, .5f, 0f); // ��
        createMetaBlock("Line", CubeColor.Amethyst, BlockType.lineType, .0f, .5f, 0f);     // (0, .5, 0)
        createMetaBlock("LeftLightning", CubeColor.Oak, BlockType.leftLightningType, .5f, 1.5f, 0f);           // (.5, 1, 0)
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
    public Block(Context context, BlockMeta paramBlockMeta) {
        this.color = paramBlockMeta.getColor();
        this.context = context;
        isActiveFlag = false;
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
    public CubeColor getColor() { return this.color; }
    public Block clone() {
        Block localBlock = new Block();
        localBlock.cubes = new Cube[this.cubes.length];
        for (int i = 0; i < this.cubes.length; i++)
            localBlock.cubes[i] = this.cubes[i].clone();
        return localBlock;
    }

    private boolean isActiveFlag;
    public void setActiveFlag(boolean flag) { this.isActiveFlag = flag; }
    public boolean getActiveFlag() { return this.isActiveFlag; }

    public void draw(int texId) {
        if (Cube.mProjMatrix == null || Cube.mVMatrix == null) {
            float ratio = Constant.ratio;
            Matrix.perspectiveM(Cube.mProjMatrix, 0, 90f, ratio, 1f, 15f); // ͶӰ����
            Matrix.setLookAtM(Cube.mVMatrix, 0, 1.5f, -1.5f, 3.0f, 0f, 0f, 0f, 0f, 1.0f, 7.0f); // should be passed in somehow
        }
        Cube[] cubes = getCubes();
        for (int i = 0; i < cubeCounts; i++) { // can NOT think here for today
            if (curr != null && curr.equals("Square")) { // (.5, 1, 0)
                cubes[i].setActiveFlag(true);            // think here
                shiftBlock(-this.centerX, -this.centerY, -this.centerZ);
                cubes[i].setCoordinates();
                cubes[i].xAngle = this.xAngle;
                cubes[i].initShader(context);
                cubes[i].draw(texId);
                shiftBlock(this.centerX - activeBlockCenter[0], this.centerY - activeBlockCenter[1], this.centerZ - activeBlockCenter[2]);
                cubes[i].setCoordinates();
            } else {
                shiftBlock(activeBlockCenter[0] - this.centerX, activeBlockCenter[1] - this.centerY, activeBlockCenter[2] - this.centerZ);
                cubes[i].setCoordinates();
                cubes[i].xAngle = this.xAngle;
                cubes[i].initShader(context);
                cubes[i].draw(texId);
                shiftBlock(this.centerX - activeBlockCenter[0], this.centerY - activeBlockCenter[1], this.centerZ - activeBlockCenter[2]);
                cubes[i].setCoordinates();
            }
        }
    }

    public void shiftBlock(float paramFloat1, float paramFloat2, float paramFloat3) { // ƽ��
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
    public void invertX() { //��Y����ת
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
