package dev.ttetris.model;

import dev.ttetris.model.Cube;
import dev.ttetris.model.CubeType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Model implements Serializable {
    private static final long serialVersionUID = -2318261556694003675L;
    private ArrayList<ArrayList<Cube>> disappearingCubes;
    private float disappearingTime;
    private Block block;
    private ArrayList<Cube> blockCubes = new ArrayList();
    private int height;
    private int width;
    private int depth;
    private transient ModelListener listener;
    private ModelListener2 listener2;
    private ArrayList<Cube> staticCubes = new ArrayList();
    private int xBgn;
    private int xEnd;
    private int yBgn;
    private int yEnd;
    private int zBgn;
    private int zEnd;

    public Model(int paramInt1, int paramInt2, int paramInt3) {
        this.width = paramInt1;
        this.height = paramInt2;
        this.depth = paramInt3;
        this.xBgn = (-paramInt1 / 2); 
        this.xEnd = (paramInt1 / 2);
        if ((paramInt2 & 0x1) == 0)
            this.xBgn = (1 + this.xBgn);
        this.yBgn = 0;
        this.yEnd = (paramInt3 - 1);
        this.zBgn = 0;
        this.zEnd = paramInt3 - 1;

        int i = this.yBgn;
        int j = 0;
        int k = 0;
        label105: {
            //int k;
            if (i >= this.yEnd) {
                j = this.xBgn;
                if (j <= this.xEnd)
                    //break label180;
                    break label105;
                    k = this.yBgn;
                label120: {
                    if (k < this.yEnd)
                        //break label221;
                        break label120;
                        }
            }
        }
        for (int m = 0; ; m++) {
            Cube localCube2 = null;
            Cube localCube3 = null;
            if (m >= 4) {
                //return;
                Cube localCube1 = new Cube(CubeType.Brass, this.xBgn, -i, 0);  // added 0 for debug
                this.staticCubes.add(localCube1);
                notifyCubeCreated(localCube1);
                i++;
                //break;
                label180: {
                    localCube2 = new Cube(CubeType.Brass, j, -this.yEnd, 0);
                }
                this.staticCubes.add(localCube2);
                notifyCubeCreated(localCube2);
                j++;
                //break label105;
                label221: {
                    localCube3 = new Cube(CubeType.Brass, this.xEnd, -k, 0); 
                }
                this.staticCubes.add(localCube3);
                notifyCubeCreated(localCube3);
                k++;
                //break label120;
            }
            this.staticCubes.add(new Cube(CubeType.Hidden, this.xBgn, -(this.yBgn - m), 0));
            this.staticCubes.add(new Cube(CubeType.Hidden, this.xEnd, -(this.yBgn - m), 0));
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
        while (true) {
            if (!localIterator2.hasNext()) {
                //return (new ArrayList<ArrayList<Cube>>());//localObject;
                Cube localCube = (Cube)localIterator1.next();
                ArrayList localArrayList1 = (ArrayList)localHashMap.get(Integer.valueOf(localCube.getY()));
                if (localArrayList1 == null) {
                    localArrayList1 = new ArrayList();
                    localHashMap.put(Integer.valueOf(localCube.getY()), localArrayList1);
                }
                localArrayList1.add(localCube);
                return (new ArrayList<ArrayList<Cube>>());//localObject;
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

    private boolean isCellFree(int paramInt1, int paramInt2) {
        Iterator localIterator1 = this.staticCubes.iterator();
        Iterator localIterator2 = null;
        if (!localIterator1.hasNext())
            localIterator2 = this.blockCubes.iterator();
        Cube localCube2 = null;
        do {
            if (!localIterator2.hasNext()) {
                //return true;
                Cube localCube1 = (Cube)localIterator1.next();
                if ((localCube1.getX() != paramInt1) || (localCube1.getY() != paramInt2))
                    break;
                return false;
            }
            localCube2 = (Cube)localIterator2.next();
        }
        while ((localCube2.getX() != paramInt1) || (localCube2.getY() != paramInt2));
        return false;
    }

    private void notifyCubeCreated(Cube paramCube) {
        if ((this.listener != null) && (paramCube.getType() != CubeType.Hidden))
            this.listener.cubeCreated(paramCube);
    }

    private void notifyCubeDestroyed(Cube paramCube) {
        if ((this.listener != null) && (paramCube.getType() != CubeType.Hidden))
            this.listener.cubeDestroyed(paramCube);
    }

    private void notifyCubeDisappeared() {
        if (this.listener2 != null)
            this.listener2.cubeDisappeared();
    }

    private void notifyCubeMoved(Cube paramCube) {
        if ((this.listener != null) && (paramCube.getType() != CubeType.Hidden))
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
        return true; // added for debug
    }

    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    public int getDepth() { return this.depth; }
    public int getXBGN() { return this.xBgn; }
    public int getXEND() { return this.xEnd; }
    public boolean isDisappearing() { return this.disappearingCubes != null; }

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

    public void setListener(ModelListener paramModelListener) {
        this.listener = paramModelListener;
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

    public void setListener2(ModelListener2 paramModelListener2) {
        this.listener2 = paramModelListener2;
    }

    public void testFill() {
        Random localRandom = new Random();
        //CubeType[] arrayOfCubeType = CubeType.values();
        CubeType[] arrayOfCubeType = null;
        int j = 0;
        int i = 0;
        //for (int i = this.yBgn; ; i++) {
        for ( i = this.yBgn; ; i++) {
            if (i >= this.yEnd)
                return;
            j = this.xBgn;
            if (j <= this.xEnd)
                break;
        }
        CubeType localCubeType = null;
        if (isCellFree(j, -i)) {
            localCubeType = arrayOfCubeType[localRandom.nextInt(arrayOfCubeType.length)];
            if (localCubeType != CubeType.Brass);
                //break label82;
        }
        Cube localCube = null;
        while (true) {
            j++;
            //break;
            label82: {
                localCube = new Cube(localCubeType, j, -i, 0);
            }
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
        Integer localInteger = 0;
        label196: {
            localInteger = (Integer)((ArrayList)localObject).get(i);
        }
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
