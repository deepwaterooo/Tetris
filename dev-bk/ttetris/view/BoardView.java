package dev.ttetris.view;

import dev.anogl.AssetManager;
import dev.anogl.Node;
import dev.anogl.ParticleGenerator;
import dev.anogl.ParticleParams;
import dev.anogl.ParticleSystem;
import dev.anogl.ParticleSystemParams;
import dev.ttetris.model.Board;
import dev.ttetris.model.BoardListener;

public abstract class BoardView extends Node implements BoardListener {
    protected AssetManager assetManager;
    protected Board board;
    private Node[] disappearingTrailNodes;
    private ParticleSystem[] disappearingTrails;

    public BoardView(AssetManager paramAssetManager, Board paramBoard) {
        this.assetManager = paramAssetManager;
        this.board = paramBoard;
        int i = -2 + paramBoard.getHeight();
        this.disappearingTrailNodes = new Node[i];
        this.disappearingTrails = new ParticleSystem[i];
        ParticleSystemParams localParticleSystemParams = new ParticleSystemParams();
        localParticleSystemParams.numParticles = (-2 + paramBoard.getWidth());
        localParticleSystemParams.colorTexture = "Textures/Fire.png";
        localParticleSystemParams.rampTexture = "Ramp/FireRamp.png";
        ParticleGenerator local1 = new ParticleGenerator() {
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
            };
        for (int j = 0; ; j++) {
            if (j >= this.disappearingTrailNodes.length)
                return;
            this.disappearingTrailNodes[j] = new Node();
            addChild(this.disappearingTrailNodes[j]);
            this.disappearingTrailNodes[j].setPosition(1 + paramBoard.getX0(), j - (-2 + paramBoard.getHeight()), 0.6F);
            this.disappearingTrails[j] = new ParticleSystem(paramAssetManager, localParticleSystemParams, local1);
            this.disappearingTrails[j].setRenderQueueId(3);
            this.disappearingTrailNodes[j].attachGeometry(this.disappearingTrails[j]);
        }
    }

    public float getHeight() {
        return this.board.getHeight();
    }

    public float getWidth() {
        return this.board.getWidth();
    }

    public void lineDisappearing(int paramInt) {
        this.disappearingTrails[(-2 + (paramInt + this.board.getHeight()))].restart();
    }
}
