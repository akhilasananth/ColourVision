package com.example.home.camera.colorHelper;

import android.content.Context;
import android.graphics.Color;
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

    public static final double JUST_NOTICEABLE_DIFFERENCE = 2.3; // in CIEL*a*b* space

    public static final double HUE_DIFFERENCE = 1.0 / 12.0;

    public static final double COMPLIMENT_SHIFT = HUE_DIFFERENCE * 6;

    public static final int WHITE = 0xFF;

    private Context context;

    private static final HashMap<String, Integer> colorMap = new HashMap<>();

    public ColorHelper(Context c) {
        context = c;
        String[] colorName = context.getResources().getStringArray(R.array.items);
        String[] colorHex = context.getResources().getStringArray(R.array.values);
        int index = 0;

        for (String name : colorName) {
            colorMap.put(name, Color.parseColor(colorHex[index]));
            index++;
        }
    }

    //takes in CIELab colors as inputs
    public static double getDeltaE(double[] color1, double[] color2) {
        return Math.sqrt(Math.pow(color2[0] - color1[0], 2) +
                Math.pow(color2[1] - color1[1], 2) +
                Math.pow(color2[2] - color1[2], 2));
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
        }

        return closestColor;
    }



    public static int getColor(String colorName) {
        return colorMap.get(colorName);
    }

    public static String getColorName(int color) {

        String colorName = "";

        for (String s : colorMap.keySet()) {
            if (colorMap.get(s) == color) {
                return s;
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

    public static boolean inWarmRange(int color){
        float[] hsvColor = new float[3];
        Color.colorToHSV(color,hsvColor);
        if((hsvColor[0] <= 90 && hsvColor[0] >= 0) && (hsvColor[0]>270) ){
            return true;
        }
        return false;
    }

    public static boolean inCoolRange(int color){
        float[] hsvColor = new float[3];
        Color.colorToHSV(color,hsvColor);
        if(hsvColor[0] > 90 && hsvColor[0] <= 270 ){
            return true;
        }
        return false;
    }
}
