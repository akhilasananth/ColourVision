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


        //Generate complementary colors
        for(double i = 0.5; i<=0.65; i++) {
            if((hslColor[0] + i)<1) {
                double[] tempColor = HSLtoRGB(new double[]{hslColor[0] + i, hslColor[1], hslColor[2]});
                matchingColors.add(Color.rgb((int) tempColor[0], (int) tempColor[1], (int) tempColor[2]));
            }
            else{
                double[] tempColor = HSLtoRGB(new double[]{hslColor[0] - i, hslColor[1], hslColor[2]});
                matchingColors.add(Color.rgb((int) tempColor[0], (int) tempColor[1], (int) tempColor[2]));
            }
        }

        return matchingColors;
    }

    @Override
    public boolean isMatch(int color1, int color2) {
        double[] c1 = RGBtoHSL(color1);
        double[] c2 = RGBtoHSL(color2);
        double hueDifference = Math.abs(c1[0]-c2[0]);
        Log.d("HUE_DIFERENCE", "isMatch: Hue difference: " + hueDifference);
        if(hueDifference>0.5 && hueDifference<0.65) {
            Log.d("COMPLIMENTARY", "isMatch: Complimentary Match");
            return true;
        }
        return false;
    }
}
