package dev.ttetris.common;

import java.io.Serializable;

public class TopScore implements Serializable {
    private static final long serialVersionUID = 1L;
    private int highScore;
    private String playerName;

    public int getHighScore() {
        return this.highScore;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setHighScore(int paramInt) {
        this.highScore = paramInt;
    }

    public void setPlayerName(String paramString) {
        this.playerName = paramString;
    }
}
