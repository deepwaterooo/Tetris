package dev.ttetris.model;

public abstract class GameAdapter implements GameListener {
    public void modelChangeDenied() {}
    public void modelChanged(Model paramModel) {}
    public void modelCreated(Model paramModel) {}
    public void blockFalled() {}
    public void blockFreezed() {}
    public void blockRotateDenied() {}
    public void gameOver() {}
    public void levelChanged(int paramInt) {}
    public void lineDisappearing() {}
    public void nextBlockGenerated(Block paramBlock) {}
    public void pauseChanged(boolean paramBoolean) {}
    public void pointsChanged(int paramInt) {}
    public void stateChanged(GameState paramGameState) {}
}

