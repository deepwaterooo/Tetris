package dev.ttetris.common;

import java.io.Serializable;

public class GameResult
    implements Serializable {
    private static final long serialVersionUID = 1L;
    private float playTime;
    private int score;

    public float getPlayTime() {
        return this.playTime;
    }

    public int getScore() {
        return this.score;
    }

    public void setPlayTime(float paramFloat) {
        this.playTime = paramFloat;
    }

    public void setScore(int paramInt) {
        this.score = paramInt;
    }
}
