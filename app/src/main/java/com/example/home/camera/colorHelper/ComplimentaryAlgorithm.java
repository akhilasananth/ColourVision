package com.example.home.camera.colorHelper;

import android.graphics.Color;
import android.util.Log;

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

        hslColor[0] += COMPLIMENT_SHIFT;
        if (hslColor[0] > 1) hslColor[0] -= 1.0;

        double[] temp = HSLtoRGB(hslColor);
        matchingColors.add(Color.rgb((int) temp[0], (int) temp[1], (int) temp[2]));

        return matchingColors;
    }

    @Override
    public boolean isMatch(int color1, int color2) {
        double[] c1 = RGBtoHSL(color1);
        double[] c2 = RGBtoHSL(color2);
        double hueDifference = Math.abs(c1[0]-c2[0]);
        Log.d("HUE_DIFERENCE", "isMatch: Hue difference: " + hueDifference);

        if((hueDifference < (COMPLIMENT_SHIFT + HUE_DIFFERENCE/2)) && (hueDifference > (COMPLIMENT_SHIFT - HUE_DIFFERENCE/2)) ){
            Log.d("COMPLIMENTARY", "isMatch: Complimentary Match");
            return true;
        }
        return false;
    }
}
