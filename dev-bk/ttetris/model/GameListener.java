package dev.ttetris.model;

public abstract interface GameListener {
    public abstract void modelChangeDenied();
    public abstract void modelChanged(Model paramModel);
    public abstract void modelCreated(Model paramModel);
    public abstract void blockFalled();
    public abstract void blockFreezed();
    public abstract void blockRotateDenied();
    public abstract void gameOver();
    public abstract void levelChanged(int paramInt);
    public abstract void lineDisappearing();
    public abstract void nextBlockGenerated(Block paramBlock);
    public abstract void pauseChanged(boolean paramBoolean);
    public abstract void pointsChanged(int paramInt);
    public abstract void stateChanged(GameState paramGameState);
}

