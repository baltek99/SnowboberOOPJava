package com.snowbober.OOP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScores implements Serializable {
    private final List<ResultBind> scores;
    private final int maxNumberOfResults;

    public HighScores() {
        this.scores = new ArrayList<>();
        this.maxNumberOfResults = 10;
    }

    public List<ResultBind> getScores() {
        return scores;
    }

    public void addResult(String name, int score) {
        scores.add(new ResultBind(name, score));
        Collections.sort(scores);

        if (scores.size() > maxNumberOfResults) {
            scores.remove(0);
        }
    }
}
