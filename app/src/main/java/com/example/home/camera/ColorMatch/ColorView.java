package com.example.home.camera.ColorMatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by robertfernandes on 3/3/2017.
 */

public abstract class ColorView extends SurfaceView{

    public ColorView(Context context) {
        super(context);
    }

    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void drawBorderedRect(Canvas canvas, float l, float t, float w, float h, int mainColor) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(mainColor);
        canvas.drawRect(l, t, l + w, t + h, p);

        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setColor(Color.BLACK);
        canvas.drawRect(l, t, l + w, t + h, p);
    }

    public void draw() {
        Canvas canvas = getHolder().lockCanvas();
        if (canvas != null) {
            drawFrame(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public abstract void drawFrame(Canvas canvas);
}
