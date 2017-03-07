package com.example.home.camera.colorHelper;

import android.graphics.Color;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import static com.example.home.camera.colorHelper.ColorSpaceConversion.*;
import static com.example.home.camera.colorHelper.ColorHelper.*;


/**
 * Created by robertfernandes on 1/20/2017.
 */
public class AnalogousAlgorithm implements MatchingAlgorithm {

    @Override
    public List<Integer> getMatchingColors(int color) {
        ArrayList<Integer> analogousColors = new ArrayList<>();

        double[] leftAnalogous = RGBtoHSL(color);
        double[] rightAnalogous = RGBtoHSL(color);

        leftAnalogous[0] += HUE_DIFFERENCE;
        rightAnalogous[0] -= HUE_DIFFERENCE;

        if (leftAnalogous[0] > 1) leftAnalogous[0] -= 1.0;
        if (rightAnalogous[0] < 0) rightAnalogous[0] += 1.0;

        double[] temp = HSLtoRGB(leftAnalogous);
        analogousColors.add(Color.rgb((int) temp[0], (int) temp[1], (int)temp[2]));

        temp = HSLtoRGB(rightAnalogous);
        analogousColors.add(Color.rgb((int) temp[0], (int) temp[1], (int)temp[2]));

        return analogousColors;
    }

    @Override
    public boolean isMatch(int color1, int color2) {
        double[] c1 = RGBtoHSL(color1);
        double[] c2 = RGBtoHSL(color2);

        double hueDifference = Math.abs(c1[0] - c2[0]);

        return ( (hueDifference <= (HUE_DIFFERENCE+(HUE_DIFFERENCE/2))) && (hueDifference >= (HUE_DIFFERENCE/2)));
    }
}
