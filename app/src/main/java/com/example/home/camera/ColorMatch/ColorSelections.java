package com.example.home.camera.ColorMatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by robertfernandes on 2/21/2017.
 */

public class ColorSelections extends ColorView {

    private int[] comparingColors = {
            Color.BLACK,
            Color.BLACK,
            Color.BLACK,
            Color.BLACK,
            Color.BLACK,
            Color.BLACK,
    };

    private int[][] positions = {
            {0,0},{1,0},{2,0},
            {0,1},{1,1},{2,1}
    };

    public ColorSelections(Context context) {
        super(context);
    }

    public ColorSelections(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorSelections(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void drawFrame(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        int width = getWidth()/3;
        int height = getHeight()/2;

        // draw comparing colors on bottom half
        for (int i = 0; i < comparingColors.length; i++) {
            drawBorderedRect(canvas,
                    positions[i][0] * width,
                    positions[i][1] * height,
                    width, height, comparingColors[i]);
        }
    }

    public void setComparingColors(int[] colors) {
        this.comparingColors = colors;
    }

}
