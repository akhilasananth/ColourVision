package com.example.home.camera.colorHelper;

import java.util.List;

import static com.example.home.camera.colorHelper.ColorHelper.*;

/**
 * Created by robertfernandes on 1/20/2017.
 */
public class WarmAlgorithm implements MatchingAlgorithm {
    @Override
    public List<Integer> getMatchingColors(int color) {
        return null;
    }

    @Override
    public boolean isMatch(int color1, int color2) {
        return (inWarmRange(color1) && inWarmRange(color2));
    }
}
