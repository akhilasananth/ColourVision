package com.example.home.camera.ColorMatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.TextureView;

import com.example.home.camera.colorHelper.ColorHelper;

import static com.example.home.camera.colorHelper.ColorHelper.getAverageColor;

/**
 * Created by robertfernandes on 2/21/2017.
 */

public class ColorFinder extends SurfaceView {

    private int searchSize = 10;

    private int color;
    private Paint paint = new Paint();

    private Camera camera;

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

        updateColor();

        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);

            int height = getHeight() - 10;
            int closestColor = ColorHelper.getClosestColor(color);

            paint.setColor(closestColor);
            canvas.drawRect(10, 10, height, height, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.BLACK);
            canvas.drawRect(10, 10, height, height, paint);

            paint.setTextSize(90);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.GREEN);
            canvas.drawText(ColorHelper.getColorName(closestColor), 200, height/2, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    int[] colorArray = new int[searchSize * searchSize];

    public void updateColor() {
        Bitmap bmp = camera.getTextureView().getBitmap();
        if (bmp != null) {
            int startX = bmp.getHeight() / 2 - searchSize/2;
            int startY = bmp.getWidth() / 2 - searchSize/2;

            bmp.getPixels(colorArray, 0, searchSize, startX, startY, searchSize, searchSize);

            // double[] temp = calculateCorrection(getAverageColor(colors));
            // color = Color.rgb((int) temp[0], (int)temp[1], (int) temp[2]);

            color = getAverageColor(colorArray);
        }
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
