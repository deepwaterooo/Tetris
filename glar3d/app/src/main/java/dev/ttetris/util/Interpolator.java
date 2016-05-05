package dev.ttetris.util;

import java.io.Serializable;

public class Interpolator implements Serializable {
    private static final long serialVersionUID = 2511739140303109476L;
    private final float endValue;
    private final float startValue;
    private final float time;
    private float timer;

    public Interpolator(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.startValue = paramFloat1;
        this.endValue = paramFloat2;
        this.time = paramFloat3;
    }

    public float getEndValue() {
        return this.endValue;
    }

    public float getStartValue() {
        return this.startValue;
    }

    public float getValue() {
        if (isFinished())
            return this.endValue;
        return this.startValue + (this.endValue - this.startValue) * this.timer / this.time;
    }

    public boolean isFinished() {
        return this.timer >= this.time;
    }

    public void reset() {
        this.timer = 0.0F;
    }

    public void update(float paramFloat) {
        if (!isFinished())
            this.timer = (paramFloat + this.timer);
    }
}
