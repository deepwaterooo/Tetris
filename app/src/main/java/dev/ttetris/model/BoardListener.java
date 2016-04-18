package dev.ttetris.model;

public abstract interface BoardListener {
    public abstract void cubeCreated(Cube paramCube);
    public abstract void cubeDestroyed(Cube paramCube);
    public abstract void cubeMoved(Cube paramCube);
    public abstract void lineDisappearing(int paramInt);
}
