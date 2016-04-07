package dev.ttetris.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import dev.anogl.AssetManager;
import dev.anogl.Geometry;  // 
import dev.tetris.model.Board;
import dev.tetris.model.Brick;

public class BoardBatchView extends BoardView {
    private Map<Cube, CubeBatchGeometry> cubeMap = new HashMap();

    public BoardBatchView(AssetManager paramAssetManager, Board paramBoard) {
        super(paramAssetManager, paramBoard);
    }

    public void cubeCreated(Cube paramCube) {
        Iterator localIterator = getGeometries().iterator();
        boolean bool = localIterator.hasNext();
        Object localObject = null;
        if (!bool);
        while (true) {
            if (localObject == null) {
                localObject = new CubeBatchGeometry(this.assetManager, paramCube.getType());
                attachGeometry((Geometry)localObject);
            }
            ((CubeBatchGeometry)localObject).addCube(paramCube);
            this.cubeMap.put(paramCube, localObject);
            return;
            Geometry localGeometry = (Geometry)localIterator.next();
            if (!(localGeometry instanceof CubeBatchGeometry))
                break;
            CubeBatchGeometry localCubeBatchGeometry = (CubeBatchGeometry)localGeometry;
            if ((localCubeBatchGeometry.getCubeType() != paramCube.getType()) || (localCubeBatchGeometry.isFull()))
                break;
            localObject = localCubeBatchGeometry;
        }
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
