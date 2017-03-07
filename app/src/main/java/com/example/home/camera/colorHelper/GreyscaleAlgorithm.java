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
        Log.e("Color1", "" + color1);
        Log.e("Color2", "" + color2);
        return (color1!=color2) && ((isGreyscaleColor(color1) || isGreyscaleColor(color2)));
    }
}


