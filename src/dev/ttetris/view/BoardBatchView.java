package dev.ttetris.view;

//import dev.ttetris.util.AssetManager;
import dev.ttetris.util.Geometry;  
import dev.ttetris.model.Board;
import dev.ttetris.model.Cube;
import dev.ttetris.view.BoardView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BoardBatchView extends BoardView {
    private Map<Cube, CubeBatchGeometry> cubeMap = new HashMap();

    //public BoardBatchView(AssetManager paramAssetManager, Board paramBoard) {
    public BoardBatchView(Board paramBoard) {
        super(paramBoard);
    }

    public void cubeCreated(Cube paramCube) {
        Iterator localIterator = getGeometries().iterator();
        boolean bool = localIterator.hasNext();
        Object localObject = null;
        if (!bool);
        while (true) {
            if (localObject == null) {
                //localObject = new CubeBatchGeometry(this.assetManager, paramCube.getColor());
                localObject = new CubeBatchGeometry(paramCube.getColor());
                attachGeometry((Geometry)localObject);
            }
            ((CubeBatchGeometry)localObject).addCube(paramCube);
            this.cubeMap.put(paramCube, (CubeBatchGeometry)localObject);
            //return;
            Geometry localGeometry = (Geometry)localIterator.next();
            if (!(localGeometry instanceof CubeBatchGeometry))
                break;
            CubeBatchGeometry localCubeBatchGeometry = (CubeBatchGeometry)localGeometry;
            if ((localCubeBatchGeometry.getCubeColor() != paramCube.getColor()) || (localCubeBatchGeometry.isFull()))
                break;
            localObject = localCubeBatchGeometry;
        }
    }

    public void lineDisappearing(int paramInt) {
    }
    
    public void cubeDestroyed(Cube paramCube) {
        CubeBatchGeometry localCubeBatchGeometry = (CubeBatchGeometry)this.cubeMap.get(paramCube);
        localCubeBatchGeometry.removeCube(paramCube);
        if (localCubeBatchGeometry.isEmpty())
            detachGeometry(localCubeBatchGeometry);
    }

    public void cubeMoved(Cube paramCube) {
        ((CubeBatchGeometry)this.cubeMap.get(paramCube)).updateCube(paramCube);
    }
}
