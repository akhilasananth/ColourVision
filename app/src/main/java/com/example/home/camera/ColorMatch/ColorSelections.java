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

public class ColorSelections extends SurfaceView {

    private int initialColor = Color.BLACK;

    private int[] comparingColors = {
            Color.BLACK,
            Color.BLACK,
            Color.BLACK,
            Color.BLACK,
            Color.BLACK,
            Color.BLACK,
    };

    private int[][] positions = {
            {0, 0},{1, 0},
            {0, 1},{1, 1},
            {0, 2},{1, 2}
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

    public void drawFrame() {
        Canvas canvas = getHolder().lockCanvas();
        int height = getHeight()/3;
        int width = getWidth()/2;

        if (canvas != null) {

            canvas.drawColor(Color.BLACK);

            for (int i = 0; i < comparingColors.length; i++) {
                drawBorderedRect(canvas,
                        positions[i][0] * width,
                        positions[i][1] * height,
                        width, height, comparingColors[i]);
            }

            drawBorderedRect(canvas, width/2, height/2, width, height*2, initialColor);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void drawBorderedRect(Canvas canvas, float l, float t, float w, float h, int mainColor) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(mainColor);
        canvas.drawRect(l, t, l + w, t + h, p);

        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setColor(Color.BLACK);
        canvas.drawRect(l, t, l + w, t + h, p);
    }

    public void setInitialColor(int color) {
        this.initialColor = color;
    }

    public void setComparingColors(int[] colors) {
        this.comparingColors = colors;
    }

}
