package dev.ttetris.view;

import dev.ttetris.model.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

//import dev.anogl.AssetManager;
import dev.ttetris.util.Node;
//import dev.anogl.ParticleGenerator;
//import dev.anogl.ParticleParams;
//import dev.anogl.ParticleSystem;
//import dev.anogl.ParticleSystemParams;
import dev.ttetris.model.Board;
import dev.ttetris.model.BoardListener;

public abstract class BoardView extends Node implements BoardListener {
    //protected AssetManager assetManager;
    protected Board model;
    private Node[] disappearingTrailNodes;
    //private ParticleSystem[] disappearingTrails;

    private Map<Cube, CubeBatchGeometry> cubeMap = new HashMap();

    //public BoardView(AssetManager paramAssetManager, Board paramBoard) {
    public BoardView(Board paramBoard) {
        //this.assetManager = paramAssetManager;
        this.model = paramBoard;
        int i = -2 + paramBoard.getHeight();
        this.disappearingTrailNodes = new Node[i];
        //this.disappearingTrails = new ParticleSystem[i];
        //ParticleSystemParams localParticleSystemParams = new ParticleSystemParams();
        //localParticleSystemParams.numParticles = (-2 + paramBoard.getWidth());
        //localParticleSystemParams.colorTexture = "Textures/Fire.png";
        //localParticleSystemParams.rampTexture = "Ramp/FireRamp.png";
        //ParticleGenerator local1 = new ParticleGenerator() {
        /*
                private float dx;

                public void generateParticle(int paramAnonymousInt, ParticleParams paramAnonymousParticleParams) {
                    paramAnonymousParticleParams.lifeTime = 0.35F;
                    paramAnonymousParticleParams.lifeTimeRange = 0.05F;
                    paramAnonymousParticleParams.startTime = (0.03F * paramAnonymousInt);
                    paramAnonymousParticleParams.endSize = 2.3F;
                    paramAnonymousParticleParams.startSize = 2.3F;
                    paramAnonymousParticleParams.position[0] = (paramAnonymousInt * this.dx);
                    float[] arrayOfFloat = paramAnonymousParticleParams.velocityRange;
                    paramAnonymousParticleParams.velocityRange[2] = 0.2F;
                    arrayOfFloat[1] = 0.2F;
                    paramAnonymousParticleParams.spinStartRange = 180.0F;
                    paramAnonymousParticleParams.spinSpeedRange = 90.0F;
                }
            }; */
        for (int j = 0; ; j++) {
            if (j >= this.disappearingTrailNodes.length)
                return;
            this.disappearingTrailNodes[j] = new Node();
            addChild(this.disappearingTrailNodes[j]);
            this.disappearingTrailNodes[j].setPosition(1 + paramBoard.getXBGN(), j - (-2 + paramBoard.getHeight()), 0.6F);
            //this.disappearingTrails[j] = new ParticleSystem(paramAssetManager, localParticleSystemParams, local1);
            //this.disappearingTrails[j].setRenderQueueId(3);
            //this.disappearingTrailNodes[j].attachGeometry(this.disappearingTrails[j]);
        }
    }

    public float getHeight() { return this.model.getHeight(); }
    public float getWidth() { return this.model.getWidth(); }

    /*
    public void lineDisappearing(int paramInt) {
        this.disappearingTrails[(-2 + (paramInt + this.model.getHeight()))].restart();
    }
    */

    public void cubeCreated(Cube paramCube) {
        Iterator localIterator = getGeometries().iterator();
        boolean bool = localIterator.hasNext();
        Object localObject = null;
        if (!bool);
        while (true) {
            if (localObject == null) {
                //localObject = new CubeBatchGeometry(this.assetManager, paramCube.getType());
                //attachGeometry((Geometry)localObject);
            }
            ((CubeBatchGeometry)localObject).addCube(paramCube);
            this.cubeMap.put(paramCube, (CubeBatchGeometry)localObject);
            return;
            //Geometry localGeometry = (Geometry)localIterator.next();
            /*
            if (!(localGeometry instanceof CubeBatchGeometry))
                break;
                CubeBatchGeometry localCubeBatchGeometry = (CubeBatchGeometry)localGeometry; 
            if ((localCubeBatchGeometry.getCubeType() != paramCube.getType()) || (localCubeBatchGeometry.isFull()))
                break;
                localObject = localCubeBatchGeometry; */
        }
    }

    public void cubeDestroyed(Cube paramCube) {
        CubeBatchGeometry localCubeBatchGeometry = (CubeBatchGeometry)this.cubeMap.get(paramCube);
        localCubeBatchGeometry.removeCube(paramCube);
        if (localCubeBatchGeometry.isEmpty())
            detachGeometry(localCubeBatchGeometry);
    }

    public void cubeMoved(Cube paramCube) {
        ((CubeBatchGeometry)this.cubeMap.get(paramCube)).updateCube(paramCube);
    }
}
