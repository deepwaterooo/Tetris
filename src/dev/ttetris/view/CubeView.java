package dev.ttetris.view;

//import dev.ttetris.util.AssetManager;
import dev.ttetris.util.Geometry;
import dev.ttetris.util.Material;
import dev.ttetris.util.Node;
import dev.ttetris.model.Cube;
import dev.ttetris.model.CubeColor;
//import dev.ttetris.view.material.CubeMaterial;

public final class CubeView extends Node {
    private Cube cube;

    //public CubeView(AssetManager paramAssetManager, Cube paramCube) {
    public CubeView(Cube paramCube) {
        this.cube = paramCube;
        //Geometry localGeometry = new Geometry(paramAssetManager.getMesh("Meshes/Cube.ply"), getMaterial(paramAssetManager, paramCube.getType()));
        //localGeometry.setRenderQueueId(0);
        //attachGeometry(localGeometry);
        setRotation(90 * paramCube.getRx(), 90 * paramCube.getRy(), 0.0F);
        syncPosition();
    }

    public Cube getCube() {
        return this.cube;
    }

    public void syncPosition() {
        setPosition(this.cube.getX(), this.cube.getY(), 0.0F);
    }

    //public static Material getMaterial(AssetManager paramAssetManager, CubeColor paramCubeColor) {
    public static Material getMaterial(CubeColor paramCubeColor) {
        String str = "Cube" + paramCubeColor;
        //Object localObject = paramAssetManager.getMaterial(str);
        /*
        if (localObject == null) {
            localObject = new CubeMaterial(paramAssetManager, "Textures/" + str + ".png");
            paramAssetManager.putMaterial(str, (Material)localObject);
            } */
        return (Material)(null);// localObject;
    }

}
