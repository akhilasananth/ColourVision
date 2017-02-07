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
public class TriadAlgorithm implements MatchingAlgorithm {

    @Override
    public List<Integer> getMatchingColors(int color) {
        ArrayList<Integer> matchingColors = new ArrayList<>();

        double[] hslColor = RGBtoHSL(color);
        if (hslColor[0] > 1) hslColor[0] -= 1.0;

        hslColor[0] += TRIAD_SHIFT;
        double[] temp = HSLtoRGB(hslColor);
        matchingColors.add(Color.rgb((int) temp[0], (int) temp[1], (int) temp[2]));

        hslColor[0] -= TRIAD_SHIFT;
        temp = HSLtoRGB(hslColor);
        matchingColors.add(Color.rgb((int) temp[0], (int) temp[1], (int) temp[2]));

        Log.d("TRIAD_COLORS", "getMatchingColors: Number of colors in the list" + matchingColors.size());
        return matchingColors;
    }

    @Override
    public boolean isMatch(int color1, int color2) {
        double[] c1 = RGBtoHSL(color1);
        double[] c2 = RGBtoHSL(color2);
        double hueDifference = Math.abs(c1[0]-c2[0]);
        return ((hueDifference >= (TRIAD_SHIFT - HUE_DIFFERENCE)) && (hueDifference <= (TRIAD_SHIFT + HUE_DIFFERENCE))) ;

    }
}
