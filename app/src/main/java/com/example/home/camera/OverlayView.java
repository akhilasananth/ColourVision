package com.example.home.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.example.home.camera.ColorHelper.RGBtoXYZ;
import static com.example.home.camera.ColorHelper.XYZtoCIELab;
import static com.example.home.camera.ColorHelper.getDeltaE;
import static com.example.home.camera.ColorHelper.getColorName;

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

            drawColorInfo(canvas);

            drawCenter(canvas);

            drawFPS(canvas);

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

    public void drawColorInfo(Canvas canvas) {
        paint.setColor(getResources().getColor(R.color.Whitesmoke));
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight()/10, paint);

        int height = canvas.getHeight()/10 - 10;

        int closestColor = getClosestColor(mColor);

        paint.setColor(mColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(10, 10, height, height, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);

        canvas.drawRect(10, 10, height, height, paint);

        paint.setTextSize(90);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        canvas.drawText(getColorName(closestColor), 200, height/2, paint);
    }

    public void drawCenter(Canvas canvas) {
        paint.setColor(Color.WHITE);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 10, paint);
    }

    public void drawFPS(Canvas canvas) {
        canvas.drawText("FPS: " + FPS, 200, 200, paint);
    }

    public void setColor(int color) {
        mColor = color;
    }

    public int getClosestColor(int color) {
        int closestColor = 0;
        double currentDistance = Double.MAX_VALUE;

        for(String x : getResources().getStringArray(R.array.values)){;
            int currentColor = Color.parseColor(x);

            double tempDistance = getDeltaE(XYZtoCIELab(RGBtoXYZ(currentColor)), XYZtoCIELab(RGBtoXYZ(color)));

            if(tempDistance < currentDistance){
                currentDistance = tempDistance;
                closestColor = currentColor;
            }
        }
        return closestColor;
    }
}