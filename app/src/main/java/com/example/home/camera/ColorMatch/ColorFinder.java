package com.example.home.camera.ColorMatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

import static com.example.home.camera.colorHelper.ColorHelper.*;

/**
 * Created by robertfernandes on 2/21/2017.
 */

public class ColorFinder extends SurfaceView {

    private static final String TAG = "ColorFinder";

    private int color = Color.BLACK;
    private Paint paint = new Paint();

    public ColorFinder(Context context) {
        super(context);
    }

    public ColorFinder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorFinder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void drawFrame() {
        Canvas canvas = getHolder().lockCanvas();

        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);

            int height = getHeight() - 10;

            int closestColor = getClosestColor(color);

            paint.setColor(closestColor);
            canvas.drawRect(10, 10, height, height, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.BLACK);
            canvas.drawRect(10, 10, height, height, paint);

            paint.setTextSize(90);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.GREEN);
            canvas.drawText(getColorName(closestColor), 200, height/2, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
