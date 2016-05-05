package dev.ttetris.model;

import dev.ttetris.model.Cube;
import dev.ttetris.model.Block;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.os.Bundle;

public class Board implements Serializable {
    private static final long serialVersionUID = -2318261556694003675L;
    private ArrayList<Cube> staticCubes = new ArrayList();
    private ArrayList<Cube> blockCubes = new ArrayList(); 
    private ArrayList<ArrayList<Cube>> disappearingCubes;
    private int height; // y 
    private int width;  // x 
    private int depth;  // z
    private int xbgn;
    private int xend;
    private int ybgn;
    private int yend;
    private int zbgn;
    private int zend;
    private Block block;
    private float disappearingTime;
    private transient BoardListener listener;
    private BoardListener2 listener2;
    
    public int getDepth() { return this.depth; }
    public int getHeight() { return this.height; }
    public int getWidth() { return this.width; }
    public int getXBGN() { return this.xbgn; }
    public int getXEND() { return this.xend; }
    public boolean isDisappearing() { return this.disappearingCubes != null; }

    public Board(int paramInt1, int paramInt2, int paramInt3) { // height y, width(col) x, depth(row) z
        this.height = paramInt1; // y 10
        this.width = paramInt2;  // x 6
        this.depth = paramInt3;  // z 6
        
        this.xbgn = (-paramInt2 / 2); // -3
        this.xend = (paramInt2 / 2);  // 3
        if ((paramInt2 & 0x1) == 0) // potentially a bug here
            this.xbgn = (1 + this.xbgn); // -2 
        this.zbgn = this.xbgn;  // -2
        this.zend = this.zend;  // 3
        this.ybgn = 0;
        this.yend = (paramInt3 - 1); // 9

        block = new Block((BlockMeta)new BlockMeta(CubeColor.Anchient, BlockType.squareType, 0.5F, 0.5F, 0.0F));
        
        /*int i = this.ybgn; // i:y 0
          int j = 0;         // j:x 0
          //label105: {}
          int k = 0;         // k:z 0
          if (i >= this.yend) { // 9
          j = this.xbgn; // j:x -2
          //if (j <= this.xend);
          //break label180;
          k = this.ybgn; // k:y 0
          //label120: if (k < this.yend);
          //break label221;
          }
          for (int m = 0; ; m++) {
          if (m >= 4) {
          //return;
          Cube localCube1 = new Cube(CubeColor.Brass, this.xbgn, -i, 0);
          this.staticCubes.add(localCube1);
          notifyCubeCreated(localCube1);
          i++;
          //break;
          //label180: {}
          Cube localCube2 = new Cube(CubeColor.Brass, j, -this.yend, 0); 
          this.staticCubes.add(localCube2);
          notifyCubeCreated(localCube2);
          j++;
          //break label105;
          //label221: {}
          Cube localCube3 = new Cube(CubeColor.Brass, this.xend, -k, 0); 
          this.staticCubes.add(localCube3);
          notifyCubeCreated(localCube3);
          k++;
          //break label120;
          }
          this.staticCubes.add(new Cube(CubeColor.Hidden, this.xbgn, -(this.ybgn - m), 0));
          this.staticCubes.add(new Cube(CubeColor.Hidden, this.xend, -(this.ybgn - m), 0));
        */
        Cube [] localCubeArr = block.getCubes();
        int n = localCubeArr.length;
        for (int i = 0; i < n; i++)
            staticCubes.add(localCubeArr[i]);
    }

    private void notifyCubeCreated(Cube paramCube) {
        if ((this.listener != null) && (paramCube.getColor() != CubeColor.Hidden))
            this.listener.cubeCreated(paramCube);
    }

    private void notifyCubeDestroyed(Cube paramCube) {
        if ((this.listener != null) && (paramCube.getColor() != CubeColor.Hidden))
            this.listener.cubeDestroyed(paramCube);
    }

    private void notifyCubeDisappeared() {
        if (this.listener2 != null)
            this.listener2.cubeDisappeared();
    }

    private void notifyCubeMoved(Cube paramCube) {
        if ((this.listener != null) && (paramCube.getColor() != CubeColor.Hidden))
            this.listener.cubeMoved(paramCube);
    }

    private void notifyLineDisappearing(int paramInt) {
        if (this.listener != null)
            this.listener.lineDisappearing(paramInt);
    }

    public boolean addBlock(Block paramBlock) {
        int i = 0;
        if (this.block != null)
            throw new RuntimeException("Block already was added");
        Cube[] arrayOfCube1 = paramBlock.getCubes();
        int j = arrayOfCube1.length;
        int k = 0;
        Cube[] arrayOfCube2 = null;
        int m = 0;
        if (k >= j) {
            arrayOfCube2 = paramBlock.getCubes();
            m = arrayOfCube2.length;
        }
        while (true) {
            if (i >= m) {
                this.block = paramBlock;
                //return true;
                Cube localCube1 = arrayOfCube1[k];
                Iterator localIterator = this.staticCubes.iterator();
                Cube localCube2 = null;
                do {
                    if (!localIterator.hasNext()) {                        
                            k++;
                            break;
                        }
                    localCube2 = (Cube)localIterator.next();
                }
                while ((localCube2.getX() != localCube1.getX()) || (localCube2.getY() != localCube1.getY()));
                return false;
            }
            notifyCubeCreated(arrayOfCube2[i]);
            i++;
        }
    }

    private ArrayList<ArrayList<Cube>> findDisappearingCubes() {
        HashMap localHashMap = new HashMap();
        Iterator localIterator1 = this.blockCubes.iterator();
        Object localObject = null;
        Iterator localIterator2 = null;
        if (!localIterator1.hasNext()) {
            localObject = null;
            localIterator2 = localHashMap.values().iterator();
        }
        while (true) { // Y indicating height
            if (!localIterator2.hasNext()) {
                //return (ArrayList<ArrayList<Cube>>)localObject;
                Cube localCube = (Cube)localIterator1.next();
                ArrayList localArrayList1 = (ArrayList)localHashMap.get(Integer.valueOf(localCube.getY()));
                if (localArrayList1 == null) {
                    localArrayList1 = new ArrayList();
                    localHashMap.put(Integer.valueOf(localCube.getY()), localArrayList1);
                }
                localArrayList1.add(localCube);
                return (ArrayList<ArrayList<Cube>>)localObject;
                //break;
            }
            ArrayList localArrayList2 = (ArrayList)localIterator2.next();
            if (localArrayList2.size() == -2 + this.width) {
                if (localObject == null)
                    localObject = new ArrayList();
                Collections.sort(localArrayList2);
                ((ArrayList)localObject).add(localArrayList2);
            }
        }
    }

    private boolean isCellFree(int paramInt1, int paramInt2, int paramInt3) {
        Iterator localIterator1 = this.staticCubes.iterator();
        Iterator localIterator2 = null;
        if (!localIterator1.hasNext())
            localIterator2 = this.blockCubes.iterator();
        Cube localCube2 = null;
        do {
            if (!localIterator2.hasNext()) //{
                return true;
            //} else {
            Cube localCube1 = (Cube)localIterator1.next();
            if ((localCube1.getX() != paramInt1) || (localCube1.getY() != paramInt2)
                || localCube1.getZ() != paramInt3)
                break;
            //return false;
            //}
            localCube2 = (Cube)localIterator2.next();
        } while ((localCube2.getX() != paramInt1) || (localCube2.getY() != paramInt2) || localCube2.getY() != paramInt3);
        return false;
    }

    public boolean freezeBlock(Block paramBlock) {
        if (this.block != paramBlock)
            throw new RuntimeException("Block must be the same");
        if (isDisappearing())
            return false;
        Cube[] arrayOfCube = paramBlock.getCubes();
        int i = arrayOfCube.length;
        int j = 0;
        Iterator localIterator = null;
        if (j >= i) {
            this.block = null;
            this.disappearingCubes = findDisappearingCubes();
            if (this.disappearingCubes != null)
                localIterator = this.disappearingCubes.iterator();
        }
        while (true) {
            if (!localIterator.hasNext()) {
                this.disappearingTime = 0.0F;
                //return true;
                Cube localCube = arrayOfCube[j];
                this.staticCubes.add(localCube);
                this.blockCubes.add(localCube);
                j++;
                break;
            }
            notifyLineDisappearing(((Cube)((ArrayList)localIterator.next()).get(0)).getY());
        }
        return true; // added for debugging
    }

    public boolean moveBlock(Block paramBlock, int paramInt1, int paramInt2, int paramInt3) {
        int i = 0;
        if (this.block != paramBlock)
            throw new RuntimeException("Block must be the same");
        if (paramBlock.moveBlock((Cube[])this.staticCubes.toArray(new Cube[0]), paramInt1, paramInt2, paramInt3)) {
            Cube[] arrayOfCube = paramBlock.getCubes();
            int j = arrayOfCube.length;
            while (true) {
                if (i >= j)
                    return true;
                notifyCubeMoved(arrayOfCube[i]);
                i++;
            }
        }
        return false;
    }

    public void removeBlock(Block paramBlock) {
        if (this.block != paramBlock)
            throw new RuntimeException("Block must be the same");
        Cube[] arrayOfCube = paramBlock.getCubes();
        int i = arrayOfCube.length;
        for (int j = 0; ; j++) {
            if (j >= i) {
                this.block = null;
                return;
            }
            notifyCubeDestroyed(arrayOfCube[j]);
        }
    }

    public boolean rotateBlockLeft(Block paramBlock) {
        int i = 0;
        if (this.block != paramBlock)
            throw new RuntimeException("Block must be the same");
        if (paramBlock.rotateBlockLeft((Cube[])this.staticCubes.toArray(new Cube[0]))) {
            Cube[] arrayOfCube = paramBlock.getCubes();
            int j = arrayOfCube.length;
            while (true) {
                if (i >= j)
                    return true;
                notifyCubeMoved(arrayOfCube[i]);
                i++;
            }
        }
        return false;
    }

    public boolean rotateBlockRight(Block paramBlock) {
        int i = 0;
        if (this.block != paramBlock)
            throw new RuntimeException("Block must be the same");
        if (paramBlock.rotateBlockRight((Cube[])this.staticCubes.toArray(new Cube[0]))) {
            Cube[] arrayOfCube = paramBlock.getCubes();
            int j = arrayOfCube.length;
            while (true) {
                if (i >= j)
                    return true;
                notifyCubeMoved(arrayOfCube[i]);
                i++;
            }
        }
        return false;
    }

    public void setListener(BoardListener paramBoardListener) {
        this.listener = paramBoardListener;
        Iterator localIterator = this.staticCubes.iterator();
        Cube[] arrayOfCube = null;
        int i = 0;
        if (!localIterator.hasNext())
            if (this.block != null) {
                arrayOfCube = this.block.getCubes();
                i = arrayOfCube.length;
            }
        for (int j = 0; ; j++) {
            if (j >= i) {
                //return;
                notifyCubeCreated((Cube)localIterator.next());
                break;
            }
            notifyCubeCreated(arrayOfCube[j]);
        }
    }

    public void setListener2(BoardListener2 paramBoardListener2) {
        this.listener2 = paramBoardListener2;
    }

    public void testFill() {
        Random localRandom = new Random();
        CubeColor[] arrayOfCubeColor = CubeColor.values();
        int j = 0;
        int i = 0;
        for ( i = this.ybgn; ; i++) {
            if (i >= this.yend)
                return;
            j = this.xbgn;
            if (j <= this.xend)
                break;
        }
        CubeColor localCubeColor = null;
        if (isCellFree(j, -i, 0)) { // for Z
            localCubeColor = arrayOfCubeColor[localRandom.nextInt(arrayOfCubeColor.length)];
            if (localCubeColor != CubeColor.Brass);
                //break label82;
        }
        while (true) {
            j++;
            //break;
            //label82: {}
            Cube localCube = new Cube(localCubeColor, j, -i, 0);  // for z
            this.staticCubes.add(localCube);
            notifyCubeCreated(localCube);
        }
    }

    public void update(float paramFloat) {
        if (this.disappearingCubes != null) {
            this.disappearingTime = (paramFloat + this.disappearingTime);
            if (this.disappearingTime >= 0.03F);
        }
        else {
            return;
        }
        this.disappearingTime -= 0.03F;
        Object localObject = null;
        Iterator localIterator1 = this.disappearingCubes.iterator();
        int i = 0;
        while (true) {
            if (!localIterator1.hasNext()) {
                if (localObject != null) {
                    Collections.sort((List)localObject);
                    i = -1 + ((ArrayList)localObject).size();
                    if (i >= 0);
                        //break label196;
                }
                if (!this.disappearingCubes.isEmpty())
                    break;
                this.disappearingCubes = null;
                return;
            }
            ArrayList localArrayList = (ArrayList)localIterator1.next();
            Cube localCube1 = (Cube)localArrayList.get(0);
            this.staticCubes.remove(localCube1);
            this.blockCubes.remove(localCube1);
            notifyCubeDestroyed(localCube1);
            notifyCubeDisappeared();
            localArrayList.remove(0);
            if (localArrayList.isEmpty()) {
                if (localObject == null)
                    localObject = new ArrayList();
                ((ArrayList)localObject).add(Integer.valueOf(localCube1.getY()));
                localIterator1.remove();
            }
        }
        label196: {} Integer localInteger = (Integer)((ArrayList)localObject).get(i);
        Iterator localIterator2 = this.blockCubes.iterator();
        while (true) {
            if (!localIterator2.hasNext()) {
                i--;
                break;
            }
            Cube localCube2 = (Cube)localIterator2.next();
            if (localCube2.getY() > localInteger.intValue()) {
                localCube2.setY(-1 + localCube2.getY());
                notifyCubeMoved(localCube2);
            }
        }
    }
}
