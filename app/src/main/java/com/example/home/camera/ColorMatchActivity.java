package com.example.home.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by robertfernandes on 11/9/2016.
 */

public class ColorMatchActivity extends Activity {

    private ColorView colorView;

    private SurfaceView color1;
    private SurfaceView color2;
    private SurfaceView color3;
    private SurfaceView color4;

    private int c1 = Color.RED;
    private int c2 = Color.BLUE;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.color_match);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            c1 = extras.getInt("Color1");
            c2 = extras.getInt("Color2");
        }

        colorView = (ColorView) findViewById(R.id.colorView);

        color1 = (SurfaceView) findViewById(R.id.color1);
        color2 = (SurfaceView) findViewById(R.id.color2);
        color3 = (SurfaceView) findViewById(R.id.color3);
        color4 = (SurfaceView) findViewById(R.id.color4);

        colorView.setOnTouchListener(new OnSwipeTouchListener(this) {

            public boolean onTouch(View v, MotionEvent event) {
                super.onTouch(v, event);
                update();
                return true;
            }

            @Override
            public void onSwipeRight() {
                finish();
            }

            @Override
            public void onSwipeLeft() {
                finish();
            }

            @Override
            public void onSwipeTop() {
                finish();
            }

            @Override
            public void onSwipeBottom() {
                finish();
            }
        });
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    update();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    update();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    public void update() {
        colorView.setColor1(c1);
        colorView.setColor1(c2);

        color1.setBackgroundColor(Color.GREEN);
        color2.setBackgroundColor(Color.RED);
        color3.setBackgroundColor(Color.BLUE);
        color4.setBackgroundColor(Color.YELLOW);
    }
}
