package dev.ttetris.util;

public enum ServerTaskResult {
    SUCCESS("SUCCESS", 0),
    REPEAT("REPEAT", 1),
    CANCEL("CANCEL", 2),
    CONNECTION_ERROR("CONNECTION_ERROR", 3),
    SERVER_ERROR("SERVER_ERROR", 4);
    private String name;
    private int i;
    
    private ServerTaskResult(String name, int i) {
        this.name = name;
        this.i = i;
    }
}
