package dev.ttetris.view;

import dev.anogl.AssetManager;
import dev.anogl.Geometry;
import dev.ttetris.view.material.BackgroundMaterial;

public class BackgroundGeometry extends Geometry {
    public BackgroundGeometry(AssetManager paramAssetManager) {  
        super(paramAssetManager.getMesh("Meshes/Background.ply"), new BackgroundMaterial(paramAssetManager, "Textures/Background.png"));
        this.renderQueueId = -1;
    }
}
