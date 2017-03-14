package com.example.home.camera.ColorMatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.example.home.camera.R;

/**
 * Created by robertfernandes on 3/3/2017.
 */

public class CurrentColorChoice {

    private int color;

    private View colorView;

    public CurrentColorChoice(View view) {
        color = Color.BLACK;
        colorView = view.findViewById(R.id.initialColor);
    }

    public void setColor(int color) {
        this.color = color;
        colorView.setBackgroundColor(color);
    }

    public void reset() {
        setColor(Color.TRANSPARENT);
    }

    public int getColor() {
        return color;
    }


}
