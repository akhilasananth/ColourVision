package com.example.home.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by home on 30/10/2016.
 */

public class OverlayView extends SurfaceView {

    private SurfaceHolder surfaceHolder;

    private Paint paint;

    private int mColor;

    public OverlayView(Context context) {
        super(context);
        init();
    }

    public OverlayView (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverlayView (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        surfaceHolder = getHolder();
        setBackgroundColor(Color.TRANSPARENT);
        setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    int frames = 0;

    int FPS = 0;

    long prevTime = System.currentTimeMillis();

    protected void drawFrame() {

        Canvas canvas = surfaceHolder.lockCanvas();

        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawColor(Color.TRANSPARENT);

            paint.setStyle(Paint.Style.FILL);

            paint.setColor(mColor);
            canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, 60, paint);

            paint.setColor(Color.WHITE);
            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 10, paint);

            paint.setTextSize(60);
            paint.setColor(Color.WHITE);
            canvas.drawText("FPS: " + FPS, 200, 200, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }

        long currentTime = System.currentTimeMillis();

        if (currentTime >= prevTime + 1000) {
            prevTime = currentTime;
            FPS = frames;
            frames = 0;
        }
        frames++;
    }

    public void setColor(int color) {
        mColor = color;
    }

}
