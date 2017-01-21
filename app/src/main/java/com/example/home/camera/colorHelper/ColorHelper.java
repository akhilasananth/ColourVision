package com.example.home.camera.colorHelper;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;

import com.example.home.camera.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by robertfernandes on 11/4/2016.
 */

public class ColorHelper {

    private static int WHITE = 0xFF;

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
        double x = XYZ[0] / 95.047;
        double y = XYZ[1] / 100.000;
        double z = XYZ[2] / 108.883;

        x = (x > 0.008856) ? Math.pow(x, 1.0/3) : (7.787 * x) + (16/116);
        y = (y > 0.008856) ? Math.pow(y, 1.0/3) : (7.787 * y) + (16/116);
        z = (z > 0.008856) ? Math.pow(z, 1.0/3) : (7.787 * z) + (16/116);

        double L = (116 * y ) - 16;
        double a = 500 * (x - y);
        double b = 200 * (y - z);

        double[] CIELab = {L, a, b};

        return CIELab;
    }

    public static double[] CIELabToXYZ(double[] CIELab){
        double Y = ( CIELab[0] + 16 ) / 116;
        double X = CIELab[1] / 500 + Y;
        double Z = Y - CIELab[2] / 200;

        double Y3 = Math.pow(Y, 3);
        double X3 = Math.pow(X, 3);
        double Z3 = Math.pow(Z, 3);
        Y = (Y3 > 0.008856)? Y3 : (Y - 16 / 116) / 7.787;
        X = (X3 > 0.008856)? X3 : (X - 16 / 116) / 7.787;
        Z = (Z3 > 0.008856)? Z3 : (Z - 16 / 116) / 7.787;

        X *= 95.047;
        Y *= 100.000;
        Z *= 108.883;

        return (new double[]{X,Y,Z});
    }

    public static double[] XYZtoRGB(double[] XYZ){
        double X = 0.0, Y = 0.0, Z = 0.0;
        
        if(XYZ[0] >= 0 && XYZ[0] <= 95.047){
            X = XYZ[0] / 100;
        } else if(XYZ[1] >= 0 && XYZ[1] <= 100.000){
            Y = XYZ[1] / 100;
        } else if(XYZ[2] >= 0 && XYZ[2] <= 108.883){
            Z = XYZ[2] / 100;
        }

        double R = X *  3.2406 + Y * -1.5372 + Z * -0.4986;
        double G = X * -0.9689 + Y *  1.8758 + Z *  0.0415;
        double B = X *  0.0557 + Y * -0.2040 + Z *  1.0570;

        R = (R > 0.0031308)? 1.055 * (Math.pow(R, (1 / 2.4))) - 0.055 : 12.92 * R;
        G = (G > 0.0031308)? 1.055 * (Math.pow(G, (1 / 2.4))) - 0.055 : 12.92 * G;
        B = (B > 0.0031308)? 1.055 * (Math.pow(B, (1 / 2.4))) - 0.055 : 12.92 * B;

        R *= 255;
        G *= 255;
        B *= 255;

        return (new double[]{R,G,B});
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

    protected static double[] RGBtoHSL(int color){
        double H = 0;
        double S = 0;
        double L = 0;

        double r1 = Color.red(color)/255.0;
        double g1 = Color.green(color)/255.0;
        double b1 = Color.blue(color)/255.0;

        double min = Math.min(r1, Math.min(g1, b1));
        double max = Math.max(r1, Math.max(g1, b1));

        double delta_r1 = 0;
        double delta_g1 = 0;
        double delta_b1 = 0;
        double delta_max = max - min;

        L = (max + min)/2.0;

        if(delta_max == 0){
            H = 0;
            S = 0;
        } else {
            if(L < 0.5){
                S = delta_max/(max + min);
            }
            else{
                S = delta_max/(2-max - min);
            }
            delta_r1 = (((max-r1)/6.0) + (delta_max/2.0))/delta_max;
            delta_g1 = (((max-r1)/6.0) + (delta_max/2.0))/delta_max;
            delta_b1 = (((max-r1)/6.0) + (delta_max/2.0))/delta_max;

            if(r1 == max){
                H = delta_b1 - delta_g1;
            } else if(g1 == max){
                H = (1/3.0)+(delta_r1 - delta_b1);
            } else if(b1 == max){
                H = (2/3.0)+(delta_g1 - delta_r1);
            }

            if(H < 0){
                H += 1;
            } else if (H > 1){
                H -= 1;
            }
        }
        return (new double[]{H,S,L});
    }

    protected static double[] HSLtoRGB(double[] hslColor){
        double h = hslColor[0];
        double s = hslColor[1];
        double l = hslColor[2];

        double r = 0;
        double g = 0;
        double b = 0;

        double v1 = 0;
        double v2 = 0;

        if(s == 0){
            r =g=b=l;
        }
        else{
            if(l < 0.5){
                v2 = l * ( 1 + s );
            }
            else{
                v2 =  (l + s ) - ( s * l) ;
                v1 = 2 * l - v2;
            }

            r = 255 * Hue_2_RGB( v1, v2, h + ( 1 / 3.0 ) );
            g = 255 * Hue_2_RGB( v1, v2, h );
            b = 255 * Hue_2_RGB( v1, v2, h - ( 1 / 3.0 ) );

        }

        return(new double[]{r,g,b});
    }

    protected static double Hue_2_RGB( double p, double q, double t ) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1 / 6.0) return p + (q - p) * 6 * t;
        if (t < 1 / 2.0) return q;
        if (t < 2 / 3.0) return p + (q - p) * (2 / 3.0 - t) * 6.0;
        return p;
    }

    public static double[] calculateCorrection(int color){
        double[] rgbCorrectionValues = new double[3];
        rgbCorrectionValues[0] = 1+((Color.red(color)*1.0)/(WHITE-Color.red(color)));
        rgbCorrectionValues[1] = 1+((Color.green(color)*1.0)/(WHITE-Color.green(color)));
        rgbCorrectionValues[2] = 1+((Color.blue(color)*1.0)/(WHITE-Color.blue(color)));

        return rgbCorrectionValues;
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
