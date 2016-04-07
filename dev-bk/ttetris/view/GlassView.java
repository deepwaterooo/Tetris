package dev.ttetris.view;

import dev.anogl.AssetManager;
import dev.anogl.Geometry;
import dev.anogl.Mesh;
import dev.anogl.Node;
import dev.ttetris.view.material.GlassDiffuseMaterial;
import dev.ttetris.view.material.GlassSpecularMaterial;

public class GlassView extends Node {
    public GlassView(AssetManager paramAssetManager, float paramFloat1, float paramFloat2) {
        Mesh localMesh = paramAssetManager.getMesh("Glass");
        if (localMesh == null) {
            localMesh = paramAssetManager.getMesh("Meshes/Glass.ply").createMeshWithTangents();
            paramAssetManager.putMesh("Glass", localMesh);
        }
        Geometry localGeometry1 = new Geometry(localMesh, new GlassDiffuseMaterial(paramAssetManager));
        localGeometry1.setRenderQueueId(1);
        attachGeometry(localGeometry1);
        Geometry localGeometry2 = new Geometry(localMesh, new GlassSpecularMaterial(paramAssetManager));
        localGeometry2.setRenderQueueId(2);
        attachGeometry(localGeometry2);
        setScale(paramFloat1, paramFloat2, 1.0F);
    }
}
