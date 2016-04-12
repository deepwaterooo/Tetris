package dev.ttetris.view;

import dev.ttetris.util.AssetManager;
import dev.ttetris.util.Node;
import dev.ttetris.model.Cube;
import dev.ttetris.model.Block;

public class BlockView extends Node {
    private Block block;
    /*
    public BlockView(Block paramBlock) {
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
                localCubeBatchGeometry = new CubeBatchGeometry(localCube.getColor());
                attachGeometry(localCubeBatchGeometry);
            }
            localCubeBatchGeometry.addCube(localCube);
        }
    }
    */
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
                localCubeBatchGeometry = new CubeBatchGeometry(paramAssetManager, localCube.getColor());
                attachGeometry(localCubeBatchGeometry);
            }
            localCubeBatchGeometry.addCube(localCube);
        }
    }

    public Block getBlock() {
        return this.block;
    }
}
