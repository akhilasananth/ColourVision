package com.example.home.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by robertfernandes on 11/9/2016.
 */

public class ColorMatchActivity extends Activity {

    private ColorMatchView colorMatchView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.color_match);

        Intent i = getIntent();

        colorMatchView = (ColorMatchView) findViewById(R.id.colorMatchView);

        colorMatchView.setOnTouchListener(new OnSwipeTouchListener(this) {

            public boolean onTouch(View v, MotionEvent event) {
                super.onTouch(v, event);

                colorMatchView.update();

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

    @Override
    protected void onResume() {
        super.onResume();

        colorMatchView.update();

    }
}
