package com.example.home.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by home on 31/10/2016.
 */

public class ColorView extends SurfaceView {

    int color1 = Color.BLACK;
    int color2 = Color.BLACK;

    Paint paint = new Paint();

    public ColorView(Context context) {
        super(context);
    }

    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setColor1(int color) {
        color1 = color;
        update();
    }

    public void setColor2(int color) {
        color2 = color;
        update();
    }

    public void update() {
        Canvas canvas = getHolder().lockCanvas();

        if (canvas != null) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color1);

            canvas.drawRect(0, 0, width / 2, height, paint);

            paint.setColor(color2);
            canvas.drawRect(width / 2, 0, width, height, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }
}
