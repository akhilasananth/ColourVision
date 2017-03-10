package com.example.home.camera.ColorMatch;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.home.camera.colorHelper.ColorHelper;

/**
 * Created by robertfernandes on 2/21/2017.
 */

public class ColorFinder {

    private static final String TAG = "ColorFinder";

    private View currentColor;
    private TextView colorName;

    public ColorFinder(View currentColor, TextView colorName) {
        this.currentColor = currentColor;
        this.colorName = colorName;
    }

    public void updateColor(int color) {
        currentColor.setBackgroundColor(color);
        colorName.setText(ColorHelper.getColorName(color));
    }
}
