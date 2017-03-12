package com.example.home.camera.colorHelper;

import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.home.camera.ColorMatch.IndexedColor;

import java.util.*;

/**
 * Created by robertfernandes on 1/20/2017.
 */

public class Matcher {

    private MatchingAlgorithm[] matchingAlgorithms = {
            new ComplimentaryAlgorithm(),
            new CoolAlgorithm(),
            new AnalogousAlgorithm(),
            new TriadAlgorithm(),
            new GreyscaleAlgorithm(),
            new WarmAlgorithm()
    };

    private EmotionsAlgorithm emotionsAlgorithm = new EmotionsAlgorithm();

    public enum emotions{
        weight, warmth
    }

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

    public List<IndexedColor> isMatch(int color, List<IndexedColor> colors) {
        List<IndexedColor> matchingColors = new ArrayList<>();
        for (IndexedColor i : colors) {
            if (isMatch(color, i.getColor())){
                matchingColors.add(i);
            }
        }
        return matchingColors;
    }

    public boolean isMatch(String emotion, int color){
        return (emotionsAlgorithm.isMatch(emotion,color));
    }

    public List<IndexedColor> isMatch(String emotion, List<IndexedColor> colors){
        List<IndexedColor> matchingColors = new ArrayList<>();
        for (IndexedColor i : colors) {
            if (isMatch(emotion, i.getColor())){
                matchingColors.add(i);
            }
        }
        return matchingColors;
    }

    public boolean isMatch(int c1, int c2){
        for (MatchingAlgorithm m : matchingAlgorithms) {
            if (m.isMatch(c1, c2))
                return true;
        }
        return false;
    }

}
