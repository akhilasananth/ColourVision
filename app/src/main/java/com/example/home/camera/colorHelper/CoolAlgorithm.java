package com.example.home.camera.colorHelper;

import android.graphics.Color;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static com.example.home.camera.colorHelper.ColorSpaceConversion.*;
import static com.example.home.camera.colorHelper.ColorHelper.*;

/**
 * Created by robertfernandes on 1/20/2017.
 */
public class CoolAlgorithm implements MatchingAlgorithm {

    public List<Integer> getMatchingColors(int color) {
        ArrayList<Integer> matchingColors = new ArrayList<>();

        double[] hsl = RGBtoHSL(color);
        if(inCoolRange(color)){
            for(int i =0; i<5;i++){
                Random r = new Random();
                int hue = r.nextInt(271 - 61) + 61; // max: 271 exclusive and min: 61 inclusive
                if(hue != hsl[0]){
                    double[] rgb = HSLtoRGB(new double[]{(double) hue, hsl[1], hsl[2]});
                    matchingColors.add(Color.rgb((int) rgb[0], (int) rgb[1], (int) rgb[2]));
                }
            }
        }
        return matchingColors;
    }

    private static boolean inCoolRange(int color){
        float[] hsvColor = new float[3];
        Color.colorToHSV(color,hsvColor);
        if(hsvColor[0]>60 && hsvColor[0]<=270 ){
            return true;
        }
        return false;
    }

    @Override
    public boolean isMatch(int color1, int color2) {
        return((color1!=color2)&& inCoolRange(color1)&& inCoolRange(color2));
    }
}
