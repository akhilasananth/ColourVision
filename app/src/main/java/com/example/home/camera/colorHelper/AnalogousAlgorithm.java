package com.example.home.camera.colorHelper;

import android.graphics.Color;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import static com.example.home.camera.colorHelper.ColorSpaceConversion.*;
import static com.example.home.camera.colorHelper.ColorHelper.*;


/**
 * Created by robertfernandes on 1/20/2017.
 */
public class AnalogousAlgorithm implements MatchingAlgorithm {
    private double visibleDifference = 2.3;
    @Override
    public List<Integer> getMatchingColors(int color) {
        int n = 2;
        ArrayList<Integer> analogousColors = new ArrayList<Integer>();
        double[] CIELABcolor = XYZtoCIELab(RGBtoXYZ(color));
        while(n > 0){
            if(n%2 == 0) {
                double[] analogousColor = XYZtoRGB(CIELabToXYZ(new double[] {(CIELABcolor[0] + 2.3 * n) , (CIELABcolor[1] +2.3 * n), (CIELABcolor[2] + 2.3 * n)}));
                analogousColors.add(Color.rgb((int) analogousColor[0],(int) analogousColor[1],(int) analogousColor[2]));
            } else{
                double[] analogousColor = XYZtoRGB(CIELabToXYZ(new double[] {(CIELABcolor[0] - 2.3 * n) , (CIELABcolor[1] -2.3 * n), (CIELABcolor[2] -2.3 * n)}));
                analogousColors.add(Color.rgb((int) analogousColor[0],(int) analogousColor[1],(int) analogousColor[2]));
            }
            n--;
        }
        return analogousColors;
    }

    @Override
    public boolean isMatch(int color1, int color2) {
        double deltaE = getDeltaE(XYZtoCIELab(RGBtoXYZ(color1)),XYZtoCIELab(RGBtoXYZ(color2)));
        if(deltaE>=visibleDifference && deltaE<=visibleDifference*13.5) {
            Log.d("ANALOGOUS", "isMatch: Analogous Match");
            return true;
        }
        return false;
    }
}
