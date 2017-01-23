package com.example.home.camera.colorHelper;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.util.*;

/**
 * Created by robertfernandes on 1/20/2017.
 */

public class Matcher {

    private TextToSpeech speech;

    enum MatchType {
        Complimentary, Analogous, Warm, Cool, Greyscale, Triad
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

    public boolean checkMatch(int c1, int c2){
        boolean isMatch = (
                //new AnalogousAlgorithm().isMatch(c1,c2) ||
                //new ComplimentaryAlgorithm().isMatch(c1, c2)||
                new TriadAlgoritm().isMatch(c1, c2)
                //new WarmAlgorithm().isMatch(c1, c2) ||
                //new CoolAlgorithm().isMatch(c1, c2) ||
                //new GreyscaleAlgorithm().isMatch(c1, c2)
                  );

        return(isMatch);
    }

    public List<Integer> getMatchingColors(int color) {
        MatchingAlgorithm ma = new ComplimentaryAlgorithm();

        return ma.getMatchingColors(color);
    }

}
