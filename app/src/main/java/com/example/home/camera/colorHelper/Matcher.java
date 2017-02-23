package com.example.home.camera.colorHelper;

import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.*;

/**
 * Created by robertfernandes on 1/20/2017.
 */

public class Matcher {

    public enum MatchType {
        Complimentary, Analogous, Warm, Cool, Greyscale, Triad
    }

    private MatchingAlgorithm matchingAlgorithm;

    public Matcher() {
        matchingAlgorithm = selectRandomStrategy();
    }

    public Matcher(MatchType matchType) {
        matchingAlgorithm = createMatchingAlgorithm(matchType);
    }

    private MatchingAlgorithm selectStrategy() {

        return null;
    }

    private MatchingAlgorithm selectRandomStrategy() {
        int pick = new Random().nextInt(MatchType.values().length);
        return createMatchingAlgorithm(MatchType.values()[pick]);
    }

    private MatchingAlgorithm createMatchingAlgorithm(MatchType matchType) {
        switch(matchType) {
            case Complimentary:
                return new ComplimentaryAlgorithm();
            case Analogous:
                return new AnalogousAlgorithm();
            case Triad:
                return new TriadAlgorithm();
            case Warm:
                return new WarmAlgorithm();
            case Cool:
                return new CoolAlgorithm();
            case Greyscale:
                return new GreyscaleAlgorithm();
            default:
                return null;
        }
    }

    public List<Integer> isMatch(int color, List<Integer> colors) {
        List<Integer> matchingColors = new ArrayList<>();
        for (Integer i : colors) {
            Log.e("hello", "comparing");
            if (isMatch(color, i)){
                matchingColors.add(i);
            }
        }
        return matchingColors;
    }

    public boolean isMatch(int c1, int c2){
        return  matchingAlgorithm.isMatch(c1, c2);
    }

    public List<Integer> getMatchingColors(int color) {
        return matchingAlgorithm.getMatchingColors(color);
    }

}
