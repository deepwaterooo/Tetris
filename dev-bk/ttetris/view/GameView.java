package dev.ttetris.view;

import dev.anogl.AssetManager;
import dev.anogl.Node;
import dev.anogl.ParticleGenerator;
import dev.anogl.ParticleParams;
import dev.anogl.ParticleSystem;
import dev.anogl.ParticleSystemParams;
import dev.ttetris.model.Board;
import dev.ttetris.model.Block;
import dev.ttetris.model.Game;
import dev.ttetris.model.GameListener;
import dev.ttetris.model.GameState;

public class GameView extends Node implements GameListener {
    private AssetManager assetManager;
    private int boardsCount;
    private Node boardsNode;
    private int currentStars;
    private Game game;
    private Node nextBlock;
    private Node nextBlockParent;
    private ParticleSystem[] stars1;
    private ParticleSystem[] stars2;

    public GameView(AssetManager paramAssetManager, Game paramGame) {
        this.game = paramGame;
        this.assetManager = paramAssetManager;
        this.boardsNode = new Node();
        addChild(this.boardsNode);
        this.nextBlockParent = new Node();
        addChild(this.nextBlockParent);
    }

    private void updateNextBlockVisibility() {
        Node localNode = this.nextBlockParent;
        if ((this.game.getState() == GameState.PLAY) && (!this.game.isPaused()));
        for (boolean bool = true; ; bool = false) {
            localNode.setEnabled(bool);
            return;
        }
    }

    public void boardChangeDenied() {
    }

    public void boardChanged(Board paramBoard) {
        this.stars1[this.currentStars].restart();
        this.stars2[this.currentStars].restart();
        this.currentStars = ((1 + this.currentStars) % 2);
    }

    public void boardCreated(Board paramBoard) {
        if (this.boardsCount == 2)
            throw new RuntimeException("Maximum 2 boards can be created");
        this.boardsCount = (1 + this.boardsCount);
        BoardBatchView localBoardBatchView = new BoardBatchView(this.assetManager, paramBoard);
        paramBoard.setListener(localBoardBatchView);
        int i = 0x1 & paramBoard.getWidth();
        float f1 = 0.0F;
        if (i == 0) {
            f1 = -0.5F;
            if (this.boardsCount == 2)
                f1 *= -1.0F;
        }
        float f2 = 0.5F * (localBoardBatchView.getHeight() - 1.0F);
        int j;
        int k;
        label126: int m;
        label225: ParticleSystemParams localParticleSystemParams;
        ParticleGenerator local1;
        Node localNode1;
        Node localNode2;
        if (this.boardsCount == 1) {
            j = 1;
            localBoardBatchView.setPosition(f1, f2, 0.6F * j);
            if (this.boardsCount != 1)
                break label407;
            k = 0;
            localBoardBatchView.setRotation(0.0F, k, 0.0F);
            this.boardsNode.addChild(localBoardBatchView);
            if (this.boardsCount == 1)
                this.nextBlockParent.setPosition(2.0F + 0.5F * localBoardBatchView.getWidth(), 0.5F * localBoardBatchView.getHeight() - 3.0F, 0.0F);
            GlassView localGlassView = new GlassView(this.assetManager, localBoardBatchView.getWidth() - 1.0F, localBoardBatchView.getHeight() - 1.0F);
            this.boardsNode.addChild(localGlassView);
            if (this.boardsCount != 1)
                break label415;
            m = 0;
            localGlassView.setRotation(0.0F, m, 0.0F);
            if (this.boardsCount == 2) {
                localParticleSystemParams = new ParticleSystemParams();
                localParticleSystemParams.numParticles = 15;
                localParticleSystemParams.colorTexture = "Textures/Star.png";
                localParticleSystemParams.rampTexture = "Ramp/StarRamp.png";
                local1 = new ParticleGenerator() {
                        private float dt;
                        private float dy;

                        public void generateParticle(int paramAnonymousInt, ParticleParams paramAnonymousParticleParams)
                        {
                            paramAnonymousParticleParams.lifeTime = 1.2F;
                            paramAnonymousParticleParams.lifeTimeRange = 0.1F;
                            paramAnonymousParticleParams.endSize = 2.5F;
                            paramAnonymousParticleParams.startSize = 2.5F;
                            paramAnonymousParticleParams.position[1] = (paramAnonymousInt * this.dy);
                            paramAnonymousParticleParams.startTime = (paramAnonymousInt * this.dt);
                            float[] arrayOfFloat1 = paramAnonymousParticleParams.colorMult;
                            float[] arrayOfFloat2 = paramAnonymousParticleParams.colorMult;
                            paramAnonymousParticleParams.colorMult[2] = 0.65F;
                            arrayOfFloat2[1] = 0.65F;
                            arrayOfFloat1[0] = 0.65F;
                            float[] arrayOfFloat3 = paramAnonymousParticleParams.colorMultRange;
                            float[] arrayOfFloat4 = paramAnonymousParticleParams.colorMultRange;
                            paramAnonymousParticleParams.colorMultRange[2] = 0.35F;
                            arrayOfFloat4[1] = 0.35F;
                            arrayOfFloat3[0] = 0.35F;
                            paramAnonymousParticleParams.velocity[0] = 0.9F;
                            paramAnonymousParticleParams.velocityRange[0] = 0.2F;
                            float[] arrayOfFloat5 = paramAnonymousParticleParams.velocityRange;
                            paramAnonymousParticleParams.velocityRange[2] = 0.2F;
                            arrayOfFloat5[1] = 0.2F;
                        }
                    };
                localNode1 = new Node();
                addChild(localNode1);
                localNode1.setPosition(0.5F * localBoardBatchView.getWidth(), 0.5F + -0.5F * localBoardBatchView.getHeight(), 0.5F);
                localNode2 = new Node();
                addChild(localNode2);
                localNode2.setPosition(-0.5F * localBoardBatchView.getWidth(), 0.5F + -0.5F * localBoardBatchView.getHeight(), 0.5F);
                localNode2.setRotation(0.0F, 180.0F, 0.0F);
                this.stars1 = new ParticleSystem[2];
                this.stars2 = new ParticleSystem[2];
            }
        }
        for (int n = 0; ; n++) {
            if (n >= 2) {
                return;
                j = -1;
                break;
                label407: k = 180;
                break label126;
                label415: m = 180;
                break label225;
            }
            this.stars1[n] = new ParticleSystem(this.assetManager, localParticleSystemParams, local1);
            localNode1.attachGeometry(this.stars1[n]);
            this.stars1[n].setRenderQueueId(3);
            this.stars1[n].setAutoDisabled(true);
            this.stars2[n] = new ParticleSystem(this.assetManager, localParticleSystemParams, local1);
            localNode2.attachGeometry(this.stars2[n]);
            this.stars2[n].setRenderQueueId(3);
            this.stars2[n].setAutoDisabled(true);
        }
    }

    public void figureFalled() {
    }

    public void figureFreezed() {
    }

    public void figureRotateDenied() {
    }

    public void gameOver() {
        if (this.nextBlock != null) {
            this.nextBlock.removeFromParent();
            this.nextBlock = null;
        }
    }

    public void levelChanged(int paramInt) {
    }

    public void lineDisappearing() {
    }

    public void nextBlockGenerated(Block paramBlock) {
        if (this.nextBlock != null) {
            this.nextBlockParent.removeChild(this.nextBlock);
            this.nextBlock = null;
        }
        this.nextBlock = new BlockView(this.assetManager, paramBlock);
        this.nextBlockParent.addChild(this.nextBlock);
    }

    public void pauseChanged(boolean paramBoolean) {
        updateNextBlockVisibility();
    }

    public void pointsChanged(int paramInt) {
    }

    public void stateChanged(GameState paramGameState) {
        updateNextBlockVisibility();
    }

    public void update(float paramFloat) {
        super.update(paramFloat);
        this.boardsNode.setRotation(0.0F, this.game.getRotationAngle(), 0.0F, 1.0E-12F);
    }
}
