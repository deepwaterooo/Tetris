package dev.ttetris.view;

import java.util.HashMap;
import java.util.Map;
import dev.ttetris.util.AssetManager;
import dev.ttetris.model.Board;
import dev.ttetris.model.Cube;

public class BoardDiscreteView extends BoardView {
    private Map<Cube, CubeView> cubeViews = new HashMap();

    public BoardDiscreteView(AssetManager paramAssetManager, Board paramBoard) {
        super(paramAssetManager, paramBoard);
    }

    public void cubeCreated(Cube paramCube) {
        CubeView localCubeView = new CubeView(this.assetManager, paramCube);
        //CubeView localCubeView = new CubeView(paramCube);
        addChild(localCubeView);
        this.cubeViews.put(paramCube, localCubeView);
    }

    public void cubeDestroyed(Cube paramCube) {
        ((CubeView)this.cubeViews.remove(paramCube)).removeFromParent();
    }

    public void cubeMoved(Cube paramCube) {
        CubeView localCubeView = (CubeView)this.cubeViews.get(paramCube);
        if (localCubeView != null)
            localCubeView.syncPosition();
    }

    public void lineDisappearing(int paramInt) {
    }
}
