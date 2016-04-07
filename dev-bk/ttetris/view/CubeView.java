package dev.ttetris.view;

import dev.anogl.AssetManager;
import dev.anogl.Geometry;
import dev.anogl.Material;
import dev.anogl.Node;
import dev.ttetris.model.Brick;
import dev.ttetris.model.BrickType;
import dev.ttetris.view.material.BrickMaterial;

public final class CubeView extends Node {
    private Cube cube;

    public CubeView(AssetManager paramAssetManager, Cube paramCube) {
        this.cube = paramCube;
        Geometry localGeometry = new Geometry(paramAssetManager.getMesh("Meshes/Cube.ply"), getMaterial(paramAssetManager, paramCube.getType()));
        localGeometry.setRenderQueueId(0);
        attachGeometry(localGeometry);
        setRotation(90 * paramCube.getRx(), 90 * paramCube.getRy(), 0.0F);
        syncPosition();
    }

    public static Material getMaterial(AssetManager paramAssetManager, CubeType paramCubeType) {
        String str = "Cube" + paramCubeType;
        Object localObject = paramAssetManager.getMaterial(str);
        if (localObject == null) {
            localObject = new CubeMaterial(paramAssetManager, "Textures/" + str + ".png");
            paramAssetManager.putMaterial(str, (Material)localObject);
        }
        return localObject;
    }

    public Cube getCube() {
        return this.cube;
    }

    public void syncPosition() {
        setPosition(this.cube.getX(), this.cube.getY(), 0.0F);
    }
}
