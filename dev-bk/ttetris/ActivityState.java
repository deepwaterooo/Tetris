package dev.ttetris;

public enum ActivityState {
    static {
        GAME = new ActivityState("GAME", 2);
        TOP_SCORES = new ActivityState("TOP_SCORES", 3);
        ActivityState[] arrayOfActivityState = new ActivityState[4];
        arrayOfActivityState[0] = LOADING;
        arrayOfActivityState[1] = MENU;
        arrayOfActivityState[2] = GAME;
        arrayOfActivityState[3] = TOP_SCORES;
    }
}
