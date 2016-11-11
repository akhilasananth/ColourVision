package com.example.home.camera;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

/**
 * Created by StarGazer on 11/10/2016.
 */

public class colorMatchCriterias extends ColorHelper{
    private static Context context;

/*
    public static int COLOR_MAX = 255;
    public static int COLOR_MIN = 0;

    public class ColorWheelPoint
    {
        private  double radius;
        private double angle;

        public ColorWheelPoint(double radius, double angle)
        {
            this.radius = radius;
            this.angle = angle;
        }

        public Color toColor()
        {
            return new Color(0,0,0);
        }

    }

    public class Color
    {
        public int getBlue() {
            return blue;
        }

        public int getRed() {
            return red;
        }

        int red;

        public int getGreen() {
            return green;
        }

        int green;
        int blue;

        public Color(int red, int green, int blue)
        {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

    }

    public class ColorWheel
    {

        private int colorMax;

        public ColorWheel(int colorMax)
        {
            this.colorMax = colorMax;
        }

        public


    }*/



    public colorMatchCriterias(Context c) {
        super(c);
    }

    public static boolean isComplementaryMatch(int c1, int c2){
        int complementryColor = calculateComplementaryColor(c1);
        int closestComplementaryColor = getClosestColor(complementryColor);

        int closestC2 = getClosestColor(c2);

        double[] c1HSL = colorToHSL(c1);
        double[] c2HSL = colorToHSL(c2);

        if((closestComplementaryColor == closestC2)&&(c1HSL[2]!=c2HSL[2])){
            return true;
        }
        return false;
    }

    public static boolean isShadeOfBlackOrWhite(int color){
        String[] colourValue = context.getResources().getStringArray(R.array.BlackShadeValues);
        List<String> wordList = Arrays.asList(colourValue);
        String inputColor = "#" + Integer.toHexString(color).substring(2);

        if(wordList.contains(inputColor)){
            return true;
        }

        return false;
    }

    /*public boolean isAnalogousMatch(int c1, int c2){

    }*/
}
