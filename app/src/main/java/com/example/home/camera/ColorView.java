package com.example.home.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import java.util.HashMap;
/**
 * Created by home on 31/10/2016.
 */

public class ColorView extends SurfaceView {

    HashMap colorMap = new HashMap();

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
        color2 = getClosestColor(color);
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

    public int getClosestColor(int color) {
        //TODO
        String[] colorNames = getResources().getStringArray(R.array.items);
        String[] hexValues = getResources().getStringArray(R.array.values);

        int closestColor = 0;
        int rDelta = 255;
        int gDelta = 255;
        int bDelta = 255;

        int index = 0;
        int currentIndex = 0;

        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        double currentDistance = Double.MAX_VALUE;

        for(String x:hexValues){
            int currentColor = Color.parseColor(x);
            int rCurrent = Color.red(currentColor);
            int gCurrent = Color.green(currentColor);
            int bCurrent = Color.blue(currentColor);

            double tempDistance=(Math.pow((double)((rCurrent-r)*0.30),2.0) + Math.pow((double)((gCurrent-g)*0.59),2.0) + Math.pow((double)((bCurrent-b)*0.11),2.0));;

            if(tempDistance<currentDistance){
                currentDistance = tempDistance;
                closestColor = currentColor;
                currentIndex = index;
            }

            index++;
        }


        Log.i("The colour is ", colorNames[currentIndex] );
        return closestColor;
    }

    public boolean colorsMatch(int color1, int color2) {
        //TODO
        return false;
    }

}
