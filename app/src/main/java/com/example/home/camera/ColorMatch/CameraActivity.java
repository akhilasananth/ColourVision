package com.example.home.camera.ColorMatch;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.home.camera.R;
import com.example.home.camera.colorHelper.Matcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertfernandes on 2/21/2017.
 */

public class CameraActivity extends Activity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private ColorFinder colorFinder;

    private ColorSelections colorSelections;

    private Camera camera;

    private SurfaceView actionListener;

    private SpeechManager speechManager;

    private Matcher matcher;

    private boolean running = true;
    private boolean pause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main_layout);

        camera = new Camera(this);

        colorFinder = (ColorFinder) findViewById(R.id.colorFinder);
        colorFinder.setCamera(camera);

        colorSelections = (ColorSelections) findViewById(R.id.colorSelections);

        actionListener = (SurfaceView) findViewById(R.id.actionListener);
        actionListener.setZOrderOnTop(true);
        actionListener.setAlpha(0f);
        actionListener.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        actionListener.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return false;
            }
        });
        speechManager = new SpeechManager(this);
        matcher = new Matcher();

        thread.start();
    }

    public void checkMatches() {

    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch(keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    colorSelections.addColor(Color.GREEN);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_UP) {
                    colorSelections.addColor(colorFinder.getColor());
                }
                return true;
            default:
                    return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            camera.openCamera();
            while(running) {
                if (!pause) {
                    colorSelections.drawFrame();
                    colorFinder.drawFrame();

                }
            }
        }
    });

    @Override
    protected void onResume() {
        super.onResume();
        pause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause = true;
    }

    public ColorSelections getColorSelections() {
        return colorSelections;
    }

    public ColorFinder getColorFinder() {
        return colorFinder;
    }

    public Camera getCamera() {
        return camera;
    }

}
