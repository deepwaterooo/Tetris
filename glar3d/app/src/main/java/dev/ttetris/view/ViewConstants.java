package dev.ttetris.view;

public abstract interface ViewConstants {
  public static final float AMBIENT_VALUE = 0.35F;
  public static final int BACKGROUND_RENDER_QUEUE = -1;
  public static final int BRICK_BATCH_MAX_SIZE = 20;
  public static final float BRICK_REFLECTION_STRENGTH = 0.3F;
  public static final int BRICK_RENDER_QUEUE = 0;
  public static final float BRICK_SPECULAR_POWER = 12.5F;
  public static final float[] GLASS_COLOR = { 0.0F, 1.0F, 0.3F, 1.0F };
  public static final int GLASS_DIFFUSE_QUEUE = 1;
  public static final float GLASS_SPECULAR_POWER = 15.0F;
  public static final int GLASS_SPECULAR_QUEUE = 2;
  public static final float[] LIGHT_DIRECTION = { 0.78F, -0.45F, -0.5F };
  public static final int PARTICLES_QUEUE = 3;
  public static final float[] SPECULAR_COLOR = { 1.0F, 1.0F, 0.7F, 1.0F };
}
