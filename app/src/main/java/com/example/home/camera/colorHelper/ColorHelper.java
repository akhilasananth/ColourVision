package com.example.home.camera.colorHelper;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;

import com.example.home.camera.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.home.camera.colorHelper.ColorSpaceConversion.*;
/**
 * Created by robertfernandes on 11/4/2016.
 */

public class ColorHelper {

    private static final String TAG = "ColorHelper";

    public static final double JUST_NOTICEABLE_DIFFERENCE = 2.3; // in CIEL*a*b* space

    public static final double HUE_DIFFERENCE = 1.0 / 12.0;

    public static final double COMPLIMENT_SHIFT = HUE_DIFFERENCE * 6;

    public static final double TRIAD_SHIFT = HUE_DIFFERENCE * 4;

    public static final int WHITE = 0xFF;

    private Context context;

    private static final HashMap<String, Integer> colorMap = new HashMap<>();
    private static final HashMap<Integer, String> emotionMap = new HashMap<>();


    public ColorHelper(Context c) {
        context = c;
        String[] colorName = context.getResources().getStringArray(R.array.items);
        String[] colorHex = context.getResources().getStringArray(R.array.values);
        String[] colorEmotions = context.getResources().getStringArray(R.array.emotions);

        for (int i =0; i<colorName.length; i++) {
           // Log.e(TAG, colorName[i]);
            colorMap.put(colorName[i], Color.parseColor(colorHex[i]));
        }

        for (int i =0; i<colorName.length; i++) {
            // Log.e(TAG, colorName[i]);
            emotionMap.put(Color.parseColor(colorHex[i]),colorEmotions[i]);
        }
    }

    //takes in CIELab colors as inputs
    public static double getDeltaE(double[] color1, double[] color2) {
        return Math.sqrt(Math.pow(color2[0] - color1[0], 2) +
                Math.pow(color2[1] - color1[1], 2) +
                Math.pow(color2[2] - color1[2], 2));
    }

    public static double colorDistance(int c1, int c2)
    {
        int red1 = Color.red(c1);
        int red2 = Color.red(c2);
        int rmean = (red1 + red2) >> 1;
        int r = red1 - red2;
        int g = Color.green(c1) - Color.green(c2);
        int b = Color.blue(c1) - Color.blue(c2);
        return Math.sqrt((((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)>>8));
    }

    //Returns the name of the colour detected
    public static int getClosestColor(int color) {
        int closestColor = 0;
        double currentDistance = Double.MAX_VALUE;
            for (int currentColor : colorMap.values()) {
                double tempDistance = getDeltaE(XYZtoCIELab(RGBtoXYZ(currentColor)), XYZtoCIELab(RGBtoXYZ(color)));
                if(tempDistance < currentDistance){
                    currentDistance = tempDistance;
                    closestColor = currentColor;
                }
                if (currentDistance <= 2.3*2) {
                    return closestColor;
                }
            }
        return closestColor;
    }

    public static boolean isGreyscaleColor(int color){
        return(Color.red(color)== Color.green(color)
                && Color.red(color) == Color.blue(color)
                && Color.green(color) == Color.blue(color));
    }

    public static int getColor(String colorName) {
        return colorMap.get(colorName);
    }

    public static String getColorName(int color) {

        String colorName = "";


            for (String s : colorMap.keySet()) {
                if (colorMap.get(s) == color) {
                    colorName = s;
                }
            }

        return colorName;
    }

    public static double[] calculateCorrection(int color){
        double[] rgbCorrectionValues = new double[3];
        rgbCorrectionValues[0] = 1+((Color.red(color)*1.0)/(WHITE-Color.red(color)));
        rgbCorrectionValues[1] = 1+((Color.green(color)*1.0)/(WHITE-Color.green(color)));
        rgbCorrectionValues[2] = 1+((Color.blue(color)*1.0)/(WHITE-Color.blue(color)));

        return rgbCorrectionValues;
    }

    public static int colorCorrector(int color){
        int WHITE = 0xFF;

        double[] correctionValues = calculateCorrection(color);
        int correctedColor = Color.rgb(Math.min(WHITE, (int)(Color.red(color) * correctionValues[0])), Math.min(WHITE, (int)(Color.green(color) * correctionValues[1])), Math.min(WHITE,(int)(Color.blue(color) * correctionValues[2])));
        //int correctedColor = Color.rgb((int)(Color.red(color) * correctionValues[0]), (int)(Color.green(color) * correctionValues[1]), (int)(Color.blue(color) * correctionValues[2]));
        return correctedColor;
    }

    public static int getAverageColor(int[] colors) {

        int r = 0;
        int g = 0;
        int b = 0;

        for (int i : colors) {
            r += Color.red(i);
            g += Color.green(i);
            b += Color.blue(i);
        }

        r /= colors.length;
        g /= colors.length;
        b /= colors.length;

        return Color.rgb(r, g, b);
    }

    public static String getColorEmotion(int color){
        return emotionMap.get(color);
    }
}
