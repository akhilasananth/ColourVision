package com.example.home.camera.ColorMatch;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.home.camera.R;
import com.example.home.camera.colorHelper.ColorHelper;
import com.example.home.camera.colorHelper.Matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by robertfernandes on 2/21/2017.
 */
public class CameraActivity extends Activity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private ColorFinder colorFinder;

    private Camera camera;

    private ColorSelections colorSelections;

    private SurfaceView actionListener;

    private boolean running = true;
    private boolean pause = false;

    private int currentColor = Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main_layout);

        camera = new Camera(this, (TextureView) findViewById(R.id.cameraPreview));

        colorFinder = (ColorFinder) findViewById(R.id.colorFinder);

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
                    colorSelections.addColor(currentColor);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_UP) {
                    colorSelections.addColor(currentColor);
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
            while(running) {
                if (!pause) {
                    update();
                }
            }
        }
    });

    private void update() {
        currentColor = camera.getAverageColor();

        colorFinder.setColor(currentColor);
        colorFinder.drawFrame();

        colorSelections.drawFrame();

    }

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

}
