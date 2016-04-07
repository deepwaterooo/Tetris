package dev.ttetris.model;

public abstract interface Constants {
    public static final float BOARDS_OBSERVE_DETECTION_EPS = 1.0E-06F;
    public static final float BOARDS_OBSERVE_ROTATION_SPEED = 15.0F;
    public static final int BOARD_HEIGHT = 13;
    public static final float BOARD_ROTATION_ANGLE = 180.0F;
    public static final float BOARD_ROTATION_TIME = 0.7F;
    public static final int BOARD_WIDTH = 11;
    public static final float BRICK_DISAPPEARING_INTERVAL = 0.03F;
    public static final float BRICK_FALL_TIME_MAX = 1.1F;
    public static final float BRICK_FALL_TIME_MIN = 0.35F;
    public static final float FIRE_LIFE_TIME = 0.35F;
    public static final float FIRE_LIFE_TIME_RANGE = 0.05F;
    public static final int MAX_ACC_LEVEL = 10;
    public static final int POINTS_PER_LEVEL = 200;
    public static final int STARS_POOL_SIZE = 2;
}
