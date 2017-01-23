package com.example.home.camera.colorHelper;

import android.util.Log;


import java.util.List;

import static com.example.home.camera.colorHelper.ColorHelper.*;

/**
 * Created by robertfernandes on 1/20/2017.
 */
public class CoolAlgorithm implements MatchingAlgorithm {
    @Override
    public List<Integer> getMatchingColors(int color) {
        return null;
    }

    @Override
    public boolean isMatch(int color1, int color2) {
        boolean misMatch = inCoolRange(color1)&& inCoolRange(color2);
        if(misMatch) {
            Log.d("COOL", "isMatch: Cool Match");
        }
        return (misMatch);
    }
}
