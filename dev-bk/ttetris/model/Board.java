package dev.ttetris.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Board implements Serializable {
    private static final long serialVersionUID = -2318261556694003675L;
    private ArrayList<ArrayList<Cube>> disappearingCubes;
    private float disappearingTime;
    private Block figure;
    private ArrayList<Cube> figureCubes = new ArrayList();
    private int height;
    private int index;
    private transient BoardListener listener;
    private BoardListener2 listener2;
    private ArrayList<Cube> staticCubes = new ArrayList();
    private int width;
    private int x0;
    private int x1;
    private int y0;
    private int y1;

    public Board(int paramInt1, int paramInt2, int paramInt3) {
        this.index = paramInt1;
        this.width = paramInt2;
        this.height = paramInt3;
        this.x0 = (-paramInt2 / 2);
        this.x1 = (paramInt2 / 2);
        if ((paramInt2 & 0x1) == 0)
            this.x0 = (1 + this.x0);
        this.y0 = 0;
        this.y1 = (paramInt3 - 1);
        int i = this.y0;
        int j;
        label105: int k;
        if (i >= this.y1) {
            j = this.x0;
            if (j <= this.x1)
                break label180;
            k = this.y0;
            label120: if (k < this.y1)
                break label221;
        }
        for (int m = 0; ; m++) {
            if (m >= 4) {
                return;
                Cube localCube1 = new Cube(CubeType.Brass, this.x0, -i);
                this.staticCubes.add(localCube1);
                notifyCubeCreated(localCube1);
                i++;
                break;
                label180: Cube localCube2 = new Cube(CubeType.Brass, j, -this.y1);
                this.staticCubes.add(localCube2);
                notifyCubeCreated(localCube2);
                j++;
                break label105;
                label221: Cube localCube3 = new Cube(CubeType.Brass, this.x1, -k);
                this.staticCubes.add(localCube3);
                notifyCubeCreated(localCube3);
                k++;
                break label120;
            }
            this.staticCubes.add(new Cube(CubeType.Hidden, this.x0, -(this.y0 - m)));
            this.staticCubes.add(new Cube(CubeType.Hidden, this.x1, -(this.y0 - m)));
        }
    }

    private ArrayList<ArrayList<Cube>> findDisappearingCubes() {
        HashMap localHashMap = new HashMap();
        Iterator localIterator1 = this.figureCubes.iterator();
        Object localObject;
        Iterator localIterator2;
        if (!localIterator1.hasNext()) {
            localObject = null;
            localIterator2 = localHashMap.values().iterator();
        }
        while (true) {
            if (!localIterator2.hasNext()) {
                return localObject;
                Cube localCube = (Cube)localIterator1.next();
                ArrayList localArrayList1 = (ArrayList)localHashMap.get(Integer.valueOf(localCube.getY()));
                if (localArrayList1 == null) {
                    localArrayList1 = new ArrayList();
                    localHashMap.put(Integer.valueOf(localCube.getY()), localArrayList1);
                }
                localArrayList1.add(localCube);
                break;
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
        Iterator localIterator2;
        if (!localIterator1.hasNext())
            localIterator2 = this.figureCubes.iterator();
        Cube localCube2;
        do {
            if (!localIterator2.hasNext()) {
                return true;
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
            this.listener.brickCreated(paramCube);
    }

    private void notifyCubeDestroyed(Cube paramCube) {
        if ((this.listener != null) && (paramCube.getType() != CubeType.Hidden))
            this.listener.brickDestroyed(paramCube);
    }

    private void notifyCubeDisappeared() {
        if (this.listener2 != null)
            this.listener2.brickDisappeared();
    }

    private void notifyCubeMoved(Cube paramCube) {
        if ((this.listener != null) && (paramCube.getType() != CubeType.Hidden))
            this.listener.brickMoved(paramCube);
    }

    private void notifyLineDisappearing(int paramInt) {
        if (this.listener != null)
            this.listener.lineDisappearing(paramInt);
    }

    public boolean addBlock(Block paramBlock) {
        int i = 0;
        if (this.figure != null)
            throw new RuntimeException("Block already was added");
        Cube[] arrayOfCube1 = paramBlock.getCubes();
        int j = arrayOfCube1.length;
        int k = 0;
        Cube[] arrayOfCube2;
        int m;
        if (k >= j) {
            arrayOfCube2 = paramBlock.getCubes();
            m = arrayOfCube2.length;
        }
        while (true) {
            if (i >= m) {
                this.figure = paramBlock;
                return true;
                Cube localCube1 = arrayOfCube1[k];
                Iterator localIterator = this.staticCubes.iterator();
                Cube localCube2;
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
        if (this.figure != paramBlock)
            throw new RuntimeException("Block must be the same");
        if (isDisappearing())
            return false;
        Cube[] arrayOfCube = paramBlock.getCubes();
        int i = arrayOfCube.length;
        int j = 0;
        Iterator localIterator;
        if (j >= i) {
            this.figure = null;
            this.disappearingCubes = findDisappearingCubes();
            if (this.disappearingCubes != null)
                localIterator = this.disappearingCubes.iterator();
        }
        while (true) {
            if (!localIterator.hasNext()) {
                this.disappearingTime = 0.0F;
                return true;
                Cube localCube = arrayOfCube[j];
                this.staticCubes.add(localCube);
                this.figureCubes.add(localCube);
                j++;
                break;
            }
            notifyLineDisappearing(((Cube)((ArrayList)localIterator.next()).get(0)).getY());
        }
    }

    public int getHeight() {
        return this.height;
    }

    public int getIndex() {
        return this.index;
    }

    public int getWidth() {
        return this.width;
    }

    public int getX0() {
        return this.x0;
    }

    public int getX1() {
        return this.x1;
    }

    public boolean isDisappearing() {
        return this.disappearingCubes != null;
    }

    public boolean moveBlock(Block paramBlock, int paramInt1, int paramInt2) {
        int i = 0;
        if (this.figure != paramBlock)
            throw new RuntimeException("Block must be the same");
        if (paramBlock.moveBlock((Cube[])this.staticCubes.toArray(new Cube[0]), paramInt1, paramInt2)) {
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
        if (this.figure != paramBlock)
            throw new RuntimeException("Block must be the same");
        Cube[] arrayOfCube = paramBlock.getCubes();
        int i = arrayOfCube.length;
        for (int j = 0; ; j++) {
            if (j >= i) {
                this.figure = null;
                return;
            }
            notifyCubeDestroyed(arrayOfCube[j]);
        }
    }

    public boolean rotateBlockLeft(Block paramBlock) {
        int i = 0;
        if (this.figure != paramBlock)
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
        if (this.figure != paramBlock)
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
        Cube[] arrayOfCube;
        int i;
        if (!localIterator.hasNext())
            if (this.figure != null) {
                arrayOfCube = this.figure.getCubes();
                i = arrayOfCube.length;
            }
        for (int j = 0; ; j++) {
            if (j >= i) {
                return;
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
        CubeType[] arrayOfCubeType = CubeType.values();
        int j;
        for (int i = this.y0; ; i++) {
            if (i >= this.y1)
                return;
            j = this.x0;
            if (j <= this.x1)
                break;
        }
        CubeType localCubeType;
        if (isCellFree(j, -i)) {
            localCubeType = arrayOfCubeType[localRandom.nextInt(arrayOfCubeType.length)];
            if (localCubeType != CubeType.Brass)
                break label82;
        }
        while (true) {
            j++;
            break;
            label82: Cube localCube = new Cube(localCubeType, j, -i);
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
        int i;
        while (true) {
            if (!localIterator1.hasNext()) {
                if (localObject != null) {
                    Collections.sort((List)localObject);
                    i = -1 + ((ArrayList)localObject).size();
                    if (i >= 0)
                        break label196;
                }
                if (!this.disappearingCubes.isEmpty())
                    break;
                this.disappearingCubes = null;
                return;
            }
            ArrayList localArrayList = (ArrayList)localIterator1.next();
            Cube localCube1 = (Cube)localArrayList.get(0);
            this.staticCubes.remove(localCube1);
            this.figureCubes.remove(localCube1);
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
        label196: Integer localInteger = (Integer)((ArrayList)localObject).get(i);
        Iterator localIterator2 = this.figureCubes.iterator();
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
