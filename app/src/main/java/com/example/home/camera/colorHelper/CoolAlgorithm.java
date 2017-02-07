package com.example.home.camera.colorHelper;

import android.graphics.Color;
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

    private static boolean inCoolRange(int color){
        float[] hsvColor = new float[3];
        Color.colorToHSV(color,hsvColor);
        if(hsvColor[0]>90 && hsvColor[0]<=270 ){
            return true;
        }
        return false;
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
