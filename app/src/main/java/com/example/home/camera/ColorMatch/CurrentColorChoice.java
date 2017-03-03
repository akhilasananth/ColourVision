package com.example.home.camera.ColorMatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * Created by robertfernandes on 3/3/2017.
 */

public class CurrentColorChoice extends ColorView {

    private int initialColor = Color.BLACK;

    public CurrentColorChoice(Context context) {
        super(context);
    }

    public CurrentColorChoice(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CurrentColorChoice(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void drawFrame(Canvas canvas) {
        // draw Inital color on top half
        drawBorderedRect(canvas, 0, 0, getWidth(), getHeight(), initialColor);
    }

    public void setColor(int color) {
        this.initialColor = color;
    }


}
