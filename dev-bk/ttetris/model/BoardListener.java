package dev.ttetris.model;

public abstract interface BoardListener {
    public abstract void brickCreated(Brick paramBrick);
    public abstract void brickDestroyed(Brick paramBrick);
    public abstract void brickMoved(Brick paramBrick);
    public abstract void lineDisappearing(int paramInt);
}
