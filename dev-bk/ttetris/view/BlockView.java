package dev.ttetris.view;

//import dev.anogl.AssetManager;
import dev.anogl.Node;
import dev.tetris.model.Cube;
import dev.tetris.model.Block;

public class BlockView extends Node {
    private Block block;
    /*
    public BlockView(AssetManager paramAssetManager, Block paramBlock) {
        this.block = paramBlock;
        CubeBatchGeometry localCubeBatchGeometry = null;
        Cube[] arrayOfCube = paramBlock.getCubes();
        int i = arrayOfCube.length;
        for (int j = 0; ; j++) {
            if (j >= i)
                return;
            Cube localCube = arrayOfCube[j];
            if ((localCubeBatchGeometry == null) || (localCubeBatchGeometry.isFull())) {
                localCubeBatchGeometry = new CubeBatchGeometry(paramAssetManager, localCube.getType());
                attachGeometry(localCubeBatchGeometry);
            }
            localCubeBatchGeometry.addCube(localCube);
        }
    }
    */
    public Block getBlock() {
        return this.block;
    }
}
