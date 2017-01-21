package com.example.home.camera.colorHelper;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import static com.example.home.camera.colorHelper.ColorHelper.*;
import static com.example.home.camera.colorHelper.ColorSpaceConversion.*;

/**
 * Created by robertfernandes on 1/20/2017.
 */
public class ComplimentaryAlgorithm implements MatchingAlgorithm {
    @Override
    public List<Integer> getMatchingColors(int color) {
        ArrayList<Integer> matchingColors = new ArrayList<>();

        // convert to HSL
        double[] hslColor = RGBtoHSL(color);

        // rotate across color wheel 180 degrees
        hslColor[0] += 180/360.0;

        if (hslColor[0] > 1.0)
            hslColor[0] -= 1.0;
        else if (hslColor[0] < 0)
            hslColor[0] += 1.0;

        double[] temp = HSLtoRGB(hslColor);

        int compliment = Color.rgb((int) temp[0], (int) temp[1], (int) temp[2]);

        matchingColors.add(compliment);

        return matchingColors;
    }

    @Override
    public boolean isMatch(int color1, int color2) {
        double[] c1 = RGBtoHSL(color1);
        double[] c2 = RGBtoHSL(color2);
        return ( color2 == getMatchingColors(color1).get(0) && (c1[2] != c2[2]));
    }
}
