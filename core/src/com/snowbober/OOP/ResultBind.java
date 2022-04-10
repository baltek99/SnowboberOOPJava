package com.snowbober.OOP;

import java.io.Serializable;

public class ResultBind implements Serializable, Comparable<ResultBind> {
    private final String name;
    private final int score;

    public ResultBind(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(ResultBind result) {
        return Integer.compare(this.score, result.getScore());
    }

    @Override
    public String toString() {
        return name + " : " + score;
    }
}
