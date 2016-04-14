package dev.ttetris.view;

import dev.ttetris.util.AssetManager;
import dev.ttetris.util.Geometry;
import dev.ttetris.view.material.BackgroundMaterial;

public class BackgroundGeometry extends Geometry {
    public BackgroundGeometry(AssetManager paramAssetManager) {
        //public BackgroundGeometry() {  
        super(paramAssetManager.getMesh("Meshes/Background.ply"), new BackgroundMaterial(paramAssetManager, "Textures/Background.png")); // 
        this.renderQueueId = -1;
    }
}
