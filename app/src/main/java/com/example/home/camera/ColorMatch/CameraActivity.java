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

import com.example.home.camera.OnSwipeTouchListener;
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

    /**
     * View to show live color feed
     */
    private ColorFinder colorFinder;
    /**
     * View to display selected colors
     */
    private ColorSelections colorSelections;
    /**
     * View to handle input over whole screen
     */
    private SurfaceView actionListener;


    /**
     * Camera controller object
     */
    private Camera camera;

    /**
     * Controller object to handle color matching
     */
    private ColorMatchingController colorMatchingController;

    /**
     * Controller object to handle Text to speech
     */
    private SpeechManager speechManager;


    private boolean running = true;
    private boolean pause = false;

    private int currentColor = Color.BLACK;

    /**
     * Thread to update the views
     * calls update() function
     */
    private Thread updateThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while(running) {
                if (!pause) {
                    update();
                }
            }
        }
    });

    /**
     * onCreate function is called when the activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main_layout);

        instantiateViews();
        instantiateControllers();

        updateThread.start();
    }


    /**
     * Instantiate the View Objects
     */
    public void instantiateViews() {
        colorFinder = (ColorFinder) findViewById(R.id.colorFinder);
        colorSelections = (ColorSelections) findViewById(R.id.colorSelections);

        actionListener = (SurfaceView) findViewById(R.id.actionListener);
        actionListener.setZOrderOnTop(true);
        actionListener.setAlpha(0f);
        actionListener.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        actionListener.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                colorMatchingController.resetColors();
            }

            @Override
            public void onSwipeLeft() {
                colorMatchingController.resetColors();
            }

            @Override
            public void onSwipeTop() {
                colorMatchingController.resetColors();
            }

            @Override
            public void onSwipeBottom() {
                colorMatchingController.resetColors();
            }

            @Override
            public void onTouch() {
                colorMatchingController.checkMatches();
            }
        });
    }

    /**
     * Instantiate the Controller Objects
     */
    public void instantiateControllers() {
        camera = new Camera(this, (TextureView) findViewById(R.id.cameraPreview));
        speechManager = new SpeechManager(this);
        colorMatchingController = new ColorMatchingController(colorSelections);
    }

    /**
     * Update function used to update Views
     */
    private void update() {
        currentColor = camera.getAverageColor();

        colorFinder.setColor(currentColor);
        colorFinder.drawFrame();

        colorSelections.drawFrame();

    }

    /**
     * dispatchKeyEvent handles button presses
     *
     * @param event
     * @return
     */
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch(keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    colorMatchingController.addColor(currentColor);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_UP) {
                    colorMatchingController.addColor(currentColor);
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

}
