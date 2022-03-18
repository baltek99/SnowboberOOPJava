package com.snowbober.Util;

public class Util {

    /**
     * Linearly interpolates a value between two floats
     *
     * @param start_value Start value
     * @param end_value   End value
     * @param pct         Our progress or percentage. [0,1]
     * @return Interpolated value between two floats
     */
    public static float lerp(float start_value, float end_value, float pct) {
        return (start_value + (end_value - start_value) * pct);
    }

    public static float easeOut(float x) {
        return 1 - (float) Math.pow(1 - x, 3);
    }

    public static float flip(float x) {
        return 1 - x;
    }

    public static float spike(float x) {
        if (x <= 0.5) {
            return easeOut(x / 0.5f);
        }
        return easeOut(flip(x) / 0.5f);
    }

    public static float easeInOutCubic(float x) {
        return x < 0.5 ? 4 * x * x * x : 1 - (float) Math.pow(-2 * x + 2, 3) / 2;
    }

    public static float easeInQuint(float x) {
        return (float) Math.pow(x, 5);
    }
}
