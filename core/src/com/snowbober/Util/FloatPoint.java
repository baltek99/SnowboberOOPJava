package com.snowbober.Util;

public class FloatPoint {
    public float x;
    public float y;

    public FloatPoint() {
        this.x = 0;
        this.y = 0;
    }

    public FloatPoint(int x, int y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public FloatPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static FloatPoint add(FloatPoint p1, FloatPoint p2) {
        return new FloatPoint(p1.x + p2.x, p1.y + p2.y);
    }

    public static FloatPoint sub(FloatPoint p1, FloatPoint p2) {
        return new FloatPoint(p1.x - p2.x, p1.y - p2.y);
    }
}
