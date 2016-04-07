package dev.ttetris.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Block implements Cloneable, Serializable {
    private static final Random RANDOM = new Random();
    private final float SIZE = 0.15F;   // supposed to set & get later

    private static HashMap<String, BlockMeta> blocks = new HashMap();
    private Cube[] cubes;
    private float centerX;
    private float centerY;
    private float centerZ;
    static {
        createMetaBlock("Square", CubeColor.Anchient, BlockType.squareType, 0.5F, 0.5F, 0.0F);
        createMetaBlock("Line", CubeColor.Amethyst, BlockType.lineType, 0.0F, 1.0F, 0.0F);
        createMetaBlock("LeftLightning", CubeColor.Oak, BlockType.leftLightningType, 1.0F, 1.0F, 0.0F);
        createMetaBlock("RightLightning", CubeColor.MarbleRough, BlockType.rightLightningType, 1.0F, 1.0F, 0.0F);
        createMetaBlock("Roof", CubeColor.LapisLazuli, BlockType.roofType, 0.0F, 0.0F, 0.0F);
        createMetaBlock("LeftBoot", CubeColor.WhiteMarble, BlockType.leftBootType, 1.0F, 1.0F, 0.0F);
        createMetaBlock("RightBoot", CubeColor.Marble, BlockType.rightBootType, 0.0F, 1.0F, 0.0F);
    }

    private Block() { this.cubes = null; }
    public static Block createBlock(String paramString) { return new Block((BlockMeta)blocks.get(paramString)); } // return BlockMeta
    public static String[] getBlockNames() { return (String[])blocks.keySet().toArray(new String[0]); }

    private static void createMetaBlock(String paramString, CubeColor paramCubeColor, CubeShift[] paramArrayOfCubeShift,
                                        float paramFloat1, float paramFloat2, float paramFloat3) {
        blocks.put(paramString, new BlockMeta(paramCubeColor, paramArrayOfCubeShift, paramFloat1, paramFloat2, paramFloat3));
    }

    public Cube[] getCubes() { return this.cubes; }
    
    public Block(BlockMeta paramBlockMeta) {
        CubeShift[] arrayOfCubeShift = paramBlockMeta.getShifts();
        /*
        int i = RANDOM.nextInt();
        int j = i & 0x3;        // x
        int k = (i & 0xC) >> 2; // y
        int x = (i & 0x30) >> 4;// z
        */
        this.cubes = new Cube[arrayOfCubeShift.length]; // constant 4
        for (int m = 0; ; m++) {
            if (m >= arrayOfCubeShift.length) {
                this.centerX = paramBlockMeta.getCenterX();
                this.centerY = paramBlockMeta.getCenterY();
                this.centerZ = paramBlockMeta.getCenterZ();
                return;
            }
            this.cubes[m] = new Cube(paramBlockMeta.getColor(), SIZE,
                                     arrayOfCubeShift[m].getDx(), arrayOfCubeShift[m].getDy(), arrayOfCubeShift[m].getDz());
        }
    }

    public Block clone() {
        Block localBlock = new Block();
        localBlock.cubes = new Cube[this.cubes.length];
        for (int i = 0; ; i++) {
            if (i >= this.cubes.length) {
                localBlock.centerX = this.centerX;
                localBlock.centerY = this.centerY;
                localBlock.centerZ = this.centerZ;
                return localBlock;
            }
            localBlock.cubes[i] = this.cubes[i].clone();
        }
    }

    /*    
    public void invertX() { //ÑØYÖáÐý×ª
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
    public boolean rotateBlockLeft(Cube[] paramArrayOfCube) {
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
    
    public void shiftBlock(int paramInt1, int paramInt2, int paramInt3) { // Æ½ÒÆ
        Cube[] arrayOfCube = getCubes();
        int i = arrayOfCube.length;
        for (int j = 0; ; j++) {
            if (j >= i) {
                this.centerX += paramInt1;
                this.centerY += paramInt2;
                this.centerZ += paramInt3;
                return;
            }
            Cube localCube = arrayOfCube[j];
            localCube.setX(paramInt1 + localCube.getX());
            localCube.setY(paramInt2 + localCube.getY());
            localCube.setZ(paramInt3 + localCube.getZ());
        }
    }
}
