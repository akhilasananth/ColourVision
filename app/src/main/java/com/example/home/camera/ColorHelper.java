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

    private static double[] colorToHSL(int color){
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
        }

        else{
            if(L<0.5){
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
            }
            else if(g1 == max){
                H = (1/3.0)+(delta_r1 - delta_b1);
            }
            else if(b1 == max){
                H = (2/3.0)+(delta_g1 - delta_r1);
            }

            if(H < 0){
                H += 1;
            }

            if(H > 1){
                H -= 1;
            }
        }
        return (new double[]{H,S,L});
    }

    private static double[] HSLtoRGB(double[] hslColor){
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



    private static double Hue_2_RGB( double p, double q, double t )             //Function Hue_2_RGB
    {
        if(t < 0) t += 1;
        if(t > 1) t -= 1;
        if(t < 1/6.0) return p + (q - p) * 6 * t;
        if(t < 1/2.0) return q;
        if(t < 2/3.0) return p + (q - p) * (2/3.0 - t) * 6.0;
        return p;
    }

    public static int calculateComplementaryColor(int c){
        double[] hsl = colorToHSL(c);
        double currHue = hsl[0];
        double oppHue = currHue + 0.5;

        if(oppHue > 1){
            oppHue -=1;
        }

        double[] rgb = HSLtoRGB(new double[] {oppHue,hsl[1],hsl[2]});

        return(Color.rgb((int)rgb[0],(int)rgb[1],(int)rgb[2]));

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

}
