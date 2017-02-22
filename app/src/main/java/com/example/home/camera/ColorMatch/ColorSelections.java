package com.example.home.camera.ColorMatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by robertfernandes on 2/21/2017.
 */

public class ColorSelections extends SurfaceView {

    private int MAX_NUM_COLORS = 6;

    private int numColors = 0;

    private int[] colors = {
            Color.BLACK,
            Color.BLACK,
            Color.BLACK,
            Color.BLACK,
            Color.BLACK,
            Color.GREEN,
    };

    private int[][] positions = {
            {0, 0},{0, 1},
            {1, 0},{1, 1},
            {2, 0},{2, 1}
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

            for (int i = 0; i < colors.length; i++) {
                drawBorderedRect(canvas,
                        positions[i][0] * getHeight(),
                        positions[i][1] * getWidth(),
                        height, width, colors[i]);
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void drawBorderedRect(Canvas canvas, float l, float r, float h, float w, int mainColor) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(mainColor);
        canvas.drawRect(l, r, h, w, p);

        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setColor(Color.BLACK);
        canvas.drawRect(l, r, h, w, p);
    }

    public void addColor(int color) {
        setColor(numColors, color);
        numColors++;
        if (numColors == MAX_NUM_COLORS) {
            numColors = 0;
        }
    }

    public void setColor(int pos, int color) {
        colors[pos] = color;
    }

}
