package dev.ttetris;

import dev.ttetris.util.AssetManager;
import dev.ttetris.util.Node;
import dev.ttetris.util.RenderParams;
import dev.ttetris.util.Scene;
import dev.ttetris.common.GameResult;
import dev.ttetris.model.Game;
import dev.ttetris.view.BackgroundGeometry;
import dev.ttetris.view.GameView;

public class TetrisScene extends Scene {
    private Game game;
    private boolean stopScoreSubmitting;

    public TetrisScene(Game paramGame) { this.game = paramGame; }
    public Game getGame() { return this.game; }
    public int getGameScore() { return this.game.getPoints(); }
    public boolean isPaused() { return this.game.isPaused(); }
    public boolean isStopScoreSubmitting() { return this.stopScoreSubmitting; }

    public GameResult getGameResult() {
        GameResult localGameResult = new GameResult();
        localGameResult.setPlayTime(this.game.getPlayTime());
        localGameResult.setScore(this.game.getPoints());
        return localGameResult;
    }

    public void moveBlockLeft() {
        queueEvent(new Runnable() {
                public void run() {
                    TetrisScene.this.game.moveBlockLeft();
                }
            });
    }
    public void moveBlockRight() {
        queueEvent(new Runnable() {
                public void run() {
                    TetrisScene.this.game.moveBlockRight();
                }
            });
    }

    protected void onCreate(AssetManager paramAssetManager) {
        setFrustrum(45.0F, 1.0F, 50.0F);
        this.renderParams.setLookAt(4.8f, 2.2f, 4.5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f); 
        this.rootNode.attachGeometry(new BackgroundGeometry(paramAssetManager));    // ?????????????????????????????????
        GameView localGameView = new GameView(paramAssetManager, this.game);
        this.game.addListener(localGameView);
        this.rootNode.addChild(localGameView);
    }

    protected void onUpdate(float paramFloat) {
        this.game.update(paramFloat);
    }

    public void rotateBlockLeft() {
        queueEvent(new Runnable() {
                public void run() {
                    TetrisScene.this.game.rotateBlockLeft();
                }
            });
    }

    public void rotateBlockRight() {
        queueEvent(new Runnable() {
                public void run() {
                    TetrisScene.this.game.rotateBlockRight();
                }
            });
    }

    public void setPaused(final boolean paramBoolean) {
        queueEvent(new Runnable() {
                public void run() {
                    TetrisScene.this.game.setPaused(paramBoolean);
                }
            });
    }

    public void stopScoreSubmitting() {
        this.stopScoreSubmitting = true;
        queueEvent(new Runnable() {
                public void run() {
                    TetrisScene.this.game.stopScoreSubmitting();
                }
            });
    }

    public void throwBlock() {
        queueEvent(new Runnable() {
                public void run() {
                    TetrisScene.this.game.throwBlock();
                }
            });
    }
}
