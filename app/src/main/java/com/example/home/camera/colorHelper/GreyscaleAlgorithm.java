package com.example.home.camera.colorHelper;

import java.util.List;

import static com.example.home.camera.colorHelper.ColorHelper.*;
import static com.example.home.camera.colorHelper.ColorSpaceConversion.*;

/**
 * Created by robertfernandes on 1/20/2017.
 */
public class GreyscaleAlgorithm implements MatchingAlgorithm {
    @Override
    public List<Integer> getMatchingColors(int color) {
        return null;
    }

    @Override
    public boolean isMatch(int color1, int color2){
        double[] c1 = RGBtoHSL(color1);
        double[] c2 = RGBtoHSL(color2);
        boolean c1IslowSaturation = c1[2] <=10;
        boolean c2IslowSaturation = c2[2] <=10;

        if((c1[1] <= 0.1 && c1IslowSaturation) && c2[1] >= 0.1 ){
            return true;
        } else if(c1[1] >= 0.1 && (c2[1] <= 0.1 && c2IslowSaturation)){
            return true;
        } else if((c1[1] <= 0.1 && c2[1] <= 0.1 )&& (c1IslowSaturation && c2IslowSaturation) && color1 != color2){
            return true;
        }
        return false;
    }
}


