package dev.ttetris.model;

public enum GameState {
    PLAY("PLAY", 0),
    CHANGE_BOARD("CHANGE_BOARD", 1),
    SUBMIT_SCORE("SUBMIT_SCORE", 2),
    END("END", 3);

    private final String name;
    private final int val;
    private GameState(String str, int val) {
        this.name = str;
        this.val = val;
    }
    //GameState[] arrayOfGameState = new GameState[4];
}

