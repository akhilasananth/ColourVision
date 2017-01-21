package com.example.home.camera.colorHelper;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import static com.example.home.camera.colorHelper.ColorHelper.*;

/**
 * Created by robertfernandes on 1/20/2017.
 */
public class ComplimentaryAlgorithm implements MatchingAlgorithm {
    @Override
    public List<Integer> getMatchingColors(int color) {
        ArrayList<Integer> matchingColors = new ArrayList<>();

        double[] hslColor = RGBtoHSL(color);
        double hue = hslColor[0];

        hue += (hue <= 180)? 180 : -180;

        double[] rgbColor = HSLtoRGB(new double[]{hue, hslColor[1], hslColor[2]});
        int complimentaryColor = Color.rgb((int)rgbColor[0],(int)rgbColor[1],(int)rgbColor[2]);

        matchingColors.add(complimentaryColor);
        return matchingColors;
    }

    @Override
    public boolean isMatch(int color1, int color2) {
        double[] c1 = RGBtoHSL(color1);
        double[] c2 = RGBtoHSL(color2);
        return ( color2 == getMatchingColors(color1).get(0) && (c1[2] != c2[2]));
    }
}
