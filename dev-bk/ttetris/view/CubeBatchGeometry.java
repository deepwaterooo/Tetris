package dev.ttetris.view;

import dev.ttetris.model.Cube;
import dev.ttetris.model.CubeType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dev.anogl.Geometry;
import dev.anogl.Mesh;

public final class CubeBatchGeometry extends Geometry {
    private List<Cube> cubes = new ArrayList();
    private float[] orientations = new float[80];
    private float[] translations = new float[60];
    private CubeType cubeType;
    private boolean batchChanged;

    //public CubeBatchGeometry(AssetManager paramAssetManager, CubeType paramCubeType) {
    public CubeBatchGeometry(CubeType paramCubeType) {
        //this.cubeType = paramCubeType;
        /*
        Mesh localMesh = paramAssetManager.getMesh("CubeBatch");
        if (localMesh == null) {
            localMesh = paramAssetManager.getMesh("Meshes/Cube.ply").createBatchMesh(20);
            paramAssetManager.putMesh("CubeBatch", localMesh);
        }
        this.mesh = localMesh;
        */
        String str = "Textures/Cube" + paramCubeType + ".png"; /*
        if (paramCubeType == CubeType.Brass) {
            //this.material = new CubeEnvBatchMaterial(paramAssetManager, str);
            return;
            } */
        //this.material = new CubeBatchMaterial(paramAssetManager, str);
    }

    public void addCube(Cube paramCube) {
        if (isFull())
            throw new RuntimeException("Cube batch is full");
        this.cubes.add(paramCube);
        this.batchChanged = true;
    }

    public boolean isFull() { return this.cubes.size() >= 20; } // 10, 7, 49?
    public boolean isEmpty() { return this.cubes.isEmpty(); }
    public CubeType getCubeType() { return this.cubeType; }
    public void updateCube(Cube paramCube) { this.batchChanged = true; }

    public void removeCube(Cube paramCube) {
        this.cubes.remove(paramCube);
        this.batchChanged = true;
    }

    public void update(float paramFloat) {
        int i = 0;
        Iterator localIterator = null;
        if (this.batchChanged) {
            i = 0;
            localIterator = this.cubes.iterator();
        }
        while (true) {
            if (!localIterator.hasNext()) {
                //((CubeBatch)this.material).updateBatch(this.cubes.size(), this.translations, this.orientations);
                this.batchChanged = false;
                return;
            }
            Cube localCube = (Cube)localIterator.next();
            int j = i * 3;
            this.translations[(j + 0)] = localCube.getX();
            this.translations[(j + 1)] = localCube.getY();
            this.translations[(j + 2)] = localCube.getZ();
            i++;
        }
    }
}
