package com.example.home.camera;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;

/**
 * Created by robertfernandes on 11/4/2016.
 */

public class ColorHelper {

    private Context context;

    private static final SparseArray<String> colorMap = new SparseArray<>();

    public ColorHelper(Context c) {
        context = c;
        String[] colorName = context.getResources().getStringArray(R.array.items);
        String[] colorHex = context.getResources().getStringArray(R.array.values);
        int index = 0;

        for (String s : colorHex) {
            colorMap.put(Color.parseColor(s), colorName[index]);
            index++;
        }
    }

    public static double[] RGBtoXYZ(int color) {

        double r = Color.red(color) / 255.0;
        double g = Color.green(color) / 255.0;
        double b = Color.blue(color) / 255.0;

        r = 100 * ((r > 0.04045) ? Math.pow(((r + 0.055) / 1.055), 2.4) : r / 12.92);
        g = 100 * ((g > 0.04045) ? Math.pow(((g + 0.055) / 1.055), 2.4) : g / 12.92);
        b = 100 * ((b > 0.04045) ? Math.pow(((b + 0.055) / 1.055), 2.4) : b / 12.92);

        double x = r * 0.4124 + g * 0.3576 + b * 0.1805;
        double y = r * 0.2126 + g * 0.7152 + b * 0.0722;
        double z = r * 0.0193 + g * 0.1192 + b * 0.9505;

        double[] XYZ = {x, y, z};

        return XYZ;
    }

    public static double[] XYZtoCIELab(double[] XYZ) {
        double x = XYZ[0];
        double y = XYZ[1];
        double z = XYZ[2];

        x = (x > 0.008856) ? Math.pow(x, 1.0/3) : (7.787 * x) + (16/116);
        y = (y > 0.008856) ? Math.pow(y, 1.0/3) : (7.787 * y) + (16/116);
        z = (z > 0.008856) ? Math.pow(z, 1.0/3) : (7.787 * z) + (16/116);

        double L = (116 * y ) - 16;
        double a = 500 * (x - y);
        double b = 200 * (y - z);

        double[] CIELab = {L, a, b};

        return CIELab;
    }

    public static double getDeltaE(double[] color1, double[] color2) {
        return Math.sqrt(Math.pow(color2[0] - color1[0], 2) +
                Math.pow(color2[1] - color1[1], 2) +
                Math.pow(color2[2] - color1[2], 2));
    }

    //Returns the name of the colour detected
    public static int getClosestColor(int color) {
        int closestColor = 0;
        double currentDistance = Double.MAX_VALUE;

        int[] keys = new int[colorMap.size()];

        for (int i = 0; i < keys.length; i++) {
            keys[i] = colorMap.keyAt(i);
        }

        for(int currentColor : keys){

            double tempDistance = getDeltaE(XYZtoCIELab(RGBtoXYZ(currentColor)), XYZtoCIELab(RGBtoXYZ(color)));

            if(tempDistance < currentDistance){
                currentDistance = tempDistance;
                closestColor = currentColor;
            }
        }
        return closestColor;
    }

    //Returns true if two colours are a match
    public static boolean isMatch(int color1, int color2){
        double[] c1xyz = XYZtoCIELab(RGBtoXYZ(color1));
        double[] c2xyz = XYZtoCIELab(RGBtoXYZ(color2));

        double[] white = {100.0, 0.0, 0.0};

        double minDiff = 40.0;

        double d1 = getDeltaE(c1xyz, white);
        double d2 = getDeltaE(c2xyz, white);

        double average = (d1+d2)/2;

        if (average-d1 <= minDiff && average-d1 > 0) {
            return true;
        }

        return false;

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

    public static String getColorName(int color) {
        return colorMap.get(color);
    }

}
