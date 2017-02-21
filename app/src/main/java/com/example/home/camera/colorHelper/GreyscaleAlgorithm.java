package com.example.home.camera.colorHelper;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.home.camera.colorHelper.ColorHelper.*;
import static com.example.home.camera.colorHelper.ColorSpaceConversion.*;

/**
 * Created by robertfernandes on 1/20/2017.
 */
public class GreyscaleAlgorithm implements MatchingAlgorithm {
    @Override
    public List<Integer> getMatchingColors(int color) {
        ArrayList<Integer> matchingColors = new ArrayList<>();
        Random r = new Random();

        for(int i =0; i<5;i++) {
            int rn = r.nextInt(256); // Generates random numbers from 0 to 255

            int newColor = Color.rgb(rn, rn, rn);

            if (newColor != color) {
                matchingColors.add(newColor);
            }
        }

        return matchingColors;
  }

    @Override
    public boolean isMatch(int color1, int color2){
        if(color1==color2){
            return false;
        }
        else if(isGreyscaleColor(color1) || isGreyscaleColor(color2)){
            return true;
        }

        return false;
    }

    private boolean isGreyscaleColor(int color){
        return(Color.red(color)== Color.green(color)
                && Color.red(color) == Color.blue(color)
                && Color.green(color) == Color.blue(color) );
    }


}


