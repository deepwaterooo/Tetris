package dev.ttetris;

import dev.anogl.AssetManager;
import dev.anogl.Node;
import dev.anogl.RenderParams;
import dev.anogl.Scene;
import dev.ttetris.common.GameResult;
import dev.ttetris.model.Game;
import dev.ttetris.view.BackgroundGeometry;
import dev.ttetris.view.GameView;

public class TetrisScene extends Scene {
    private Game game;
    private boolean stopScoreSubmitting;

    public TetrisScene(Game paramGame) {
        this.game = paramGame;
    }

    public void changeBoard() {
        queueEvent(new Runnable() {
                public void run() {
                    TetrisScene.this.game.changeBoard();
                }
            });
    }

    public Game getGame() {
        return this.game;
    }

    public GameResult getGameResult() {
        GameResult localGameResult = new GameResult();
        localGameResult.setPlayTime(this.game.getPlayTime());
        localGameResult.setScore(this.game.getPoints());
        return localGameResult;
    }

    public int getGameScore() {
        return this.game.getPoints();
    }

    public boolean isPaused() {
        return this.game.isPaused();
    }

    public boolean isStopScoreSubmitting() {
        return this.stopScoreSubmitting;
    }

    public void moveFigureLeft() {
        queueEvent(new Runnable() {
                public void run() {
                    TetrisScene.this.game.moveFigureLeft();
                }
            });
    }

    public void moveFigureRight() {
        queueEvent(new Runnable() {
                public void run() {
                    TetrisScene.this.game.moveFigureRight();
                }
            });
    }

    protected void onCreate(AssetManager paramAssetManager) {
        setFrustrum(45.0F, 1.0F, 50.0F);
        this.renderParams.setLookAt(0.0F, 0.0F, 18.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F);
        this.rootNode.attachGeometry(new BackgroundGeometry(paramAssetManager));
        GameView localGameView = new GameView(paramAssetManager, this.game);
        this.game.addListener(localGameView);
        this.rootNode.addChild(localGameView);
    }

    protected void onUpdate(float paramFloat) {
        this.game.update(paramFloat);
    }

    public void rotateFigureLeft() {
        queueEvent(new Runnable() {
                public void run() {
                    TetrisScene.this.game.rotateFigureLeft();
                }
            });
    }

    public void rotateFigureRight() {
        queueEvent(new Runnable() {
                public void run() {
                    TetrisScene.this.game.rotateFigureRight();
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

    public void throwFigure() {
        queueEvent(new Runnable() {
                public void run() {
                    TetrisScene.this.game.throwFigure();
                }
            });
    }
}
