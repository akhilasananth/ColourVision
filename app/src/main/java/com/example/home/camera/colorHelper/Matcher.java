package com.example.home.camera.colorHelper;

import java.util.*;

/**
 * Created by robertfernandes on 1/20/2017.
 */

public class Matcher {

    enum MatchType {
        Complimentary, Analogous, Triad
    }

    public MatchingAlgorithm selectStrategy() {
        return null;
    }

    public MatchingAlgorithm selectRandomStrategy() {
        int pick = new Random().nextInt(MatchType.values().length);
        return createMatchingAlgorithm(MatchType.values()[pick]);
    }

    public MatchingAlgorithm createMatchingAlgorithm(MatchType matchType) {
        switch(matchType) {
            case Complimentary:
                return new ComplimentaryAlgorithm();
            case Analogous:
                return new AnalogousAlgorithm();
            case Triad:
                return new TriadAlgoritm();
            default:
                return null;
        }
    }

    public List<Integer> getMatchingColors(int color) {
        MatchingAlgorithm ma = selectRandomStrategy();
        return ma.getMatchingColors(color);
    }

}
