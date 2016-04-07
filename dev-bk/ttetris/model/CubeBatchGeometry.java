package dev.ttetris.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dev.anogl.AssetManager;
import dev.anogl.Geometry;
import dev.anogl.Mesh;
import dev.ttetris.model.Cube;
import dev.ttetris.model.CubeType;
import dev.ttetris.view.material.CubeBatchMaterial;
import dev.ttetris.view.material.CubeEnvBatchMaterial;

public final class CubeBatchGeometry extends Geometry {
    private boolean batchChanged;
    private CubeType cubeType;
    private List<Cube> cubes = new ArrayList();
    private float[] orientations = new float[80];
    private float[] translations = new float[60];

    public CubeBatchGeometry(AssetManager paramAssetManager, CubeType paramCubeType) {
        this.cubeType = paramCubeType;
        Mesh localMesh = paramAssetManager.getMesh("CubeBatch");
        if (localMesh == null) {
            localMesh = paramAssetManager.getMesh("Meshes/Cube.ply").createBatchMesh(20);
            paramAssetManager.putMesh("CubeBatch", localMesh);
        }
        this.mesh = localMesh;
        String str = "Textures/Cube" + paramCubeType + ".png";
        if (paramCubeType == CubeType.Brass) {
            this.material = new CubeEnvBatchMaterial(paramAssetManager, str);
            return;
        }
        this.material = new CubeBatchMaterial(paramAssetManager, str);
    }

    public void addCube(Cube paramCube) {
        if (isFull())
            throw new RuntimeException("Cube batch is full");
        this.cubes.add(paramCube);
        this.batchChanged = true;
    }

    public CubeType getCubeType() {
        return this.cubeType;
    }

    public boolean isEmpty() {
        return this.cubes.isEmpty();
    }

    public boolean isFull() {
        return this.cubes.size() >= 20;
    }

    public void removeCube(Cube paramCube) {
        this.cubes.remove(paramCube);
        this.batchChanged = true;
    }

    public void update(float paramFloat) {
        int i;
        Iterator localIterator;
        if (this.batchChanged) {
            i = 0;
            localIterator = this.cubes.iterator();
        }
        while (true) {
            if (!localIterator.hasNext()) {
                ((CubeBatch)this.material).updateBatch(this.cubes.size(), this.translations, this.orientations);
                this.batchChanged = false;
                return;
            }
            Cube localCube = (Cube)localIterator.next();
            int j = i * 3;
            this.translations[(j + 0)] = localCube.getX();
            this.translations[(j + 1)] = localCube.getY();
            this.translations[(j + 2)] = 0.0F;
            i++;
        }
    }

    public void updateCube(Cube paramCube) {
        this.batchChanged = true;
    }
}
