package com.example.home.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by robertfernandes on 11/7/2016.
 */

public class ColorMatchView extends SurfaceView {


    public ColorMatchView(Context context) {
        super(context);
    }

    public ColorMatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorMatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void update() {
        Canvas c = getHolder().lockCanvas();
        if (c != null) {
            c.drawColor(Color.BLACK);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);

            c.drawRect(200, 200, 500, 500, paint);

            getHolder().unlockCanvasAndPost(c);
        }
    }
}
