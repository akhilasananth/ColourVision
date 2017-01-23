package com.example.home.camera.colorHelper;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.home.camera.colorHelper.ColorHelper.*;
import static com.example.home.camera.colorHelper.ColorSpaceConversion.*;

/**
 * Created by robertfernandes on 1/20/2017.
 */
public class TriadAlgoritm implements MatchingAlgorithm {

    @Override
    public List<Integer> getMatchingColors(int color) {
        ArrayList<Integer> matchingColors = new ArrayList<>();

        double[] hslColor = RGBtoHSL(color);
        double[] rgbColor1 = HSLtoRGB(new double[]{hslColor[0] + 120, hslColor[1], hslColor[2]});
        double[] rgbColor2 = HSLtoRGB(new double[]{hslColor[0] - 120, hslColor[1], hslColor[2]});

        int color1 = Color.rgb((int)rgbColor1[0], (int)rgbColor1[1], (int)rgbColor1[2]);
        int color2 = Color.rgb((int)rgbColor2[0], (int)rgbColor2[1], (int)rgbColor2[2]);

        matchingColors.add(color1);
        matchingColors.add(color2);

        return matchingColors;
    }

    @Override
    public boolean isMatch(int color1, int color2) {
        double[] c1 = RGBtoHSL(color1);
        double[] c2 = RGBtoHSL(color2);
        double hueDifference = Math.abs(c1[0]-c2[0]);
        Log.d("HUE_DIFERENCE", "isMatch: Hue difference: " + hueDifference);
        if((hueDifference>0.20 && hueDifference<0.4)||(hueDifference>0.45 && hueDifference<0.85)) {
            Log.d("TRIAD", "isMatch: Triad Match");
            return true;
        }
        return false;

    }
}
