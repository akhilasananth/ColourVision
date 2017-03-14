package com.example.home.camera.ColorMatch;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.home.camera.R;
import com.example.home.camera.colorHelper.ColorHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by robertfernandes on 2/21/2017.
 */
public class CameraActivity extends FragmentActivity{

    private static final int REQUEST_CAMERA_PERMISSION = 200;


    /**
     * View to show live color feed
     */
    private ColorFinder colorFinder;
    /**
     * View to handle input over whole screen
     */
    private SurfaceView actionListener;
    /**
     * View pager to allow swiping between different views
     */
    private ViewPager viewPager;

    /**
     * Camera controller object
     */
    private Camera camera;
    /**
     * Controller object to handle color matching
     */
    private AlgorithmController algorithmController;
    /**
     * Controller object to handle emotion matching
     */
    private EmotionController emotionController;

    private AlgorithmEmotionController algorithmEmotionController;
    /**
     * Controller object to handle Text to speech
     */
    private SpeechManager speechManager;
    /**
     * Current color view controller
     */
    private ColorViewController currentController;
    private ColorPagerFragmentAdapter colorPagerFragmentAdapter;

    private ColorHelper colorHelper;

    private boolean running = true;
    private boolean pause = false;

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

        colorHelper = new ColorHelper(this);

        instantiateViews();
        instantiateControllers();

        updateThread.start();

    }


    /**
     * Instantiate the View Objects
     */
    public void instantiateViews() {
        colorFinder = new ColorFinder(findViewById(R.id.cameraColor), (TextView)findViewById(R.id.colorName));

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        actionListener = (SurfaceView) findViewById(R.id.actionListener);
        actionListener.setZOrderOnTop(true);
        actionListener.setAlpha(0f);
        actionListener.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        actionListener.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                currentController.onSwipeRight();
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
//                speechManager.speak(currentController.getName());
            }

            @Override
            public void onSwipeLeft() {
                currentController.onSwipeLeft();
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
//                speechManager.speak(currentController.getName());
            }

            @Override
            public void onSwipeTop() {
                currentController.onSwipeUp();
            }

            @Override
            public void onSwipeBottom() {
                currentController.onSwipeDown();
            }

            @Override
            public void onTouch() {
                currentController.onTouchScreen();
            }
        });
    }

    /**
     * Instantiate the Controller Objects
     */
    public void instantiateControllers() {
        camera = new Camera(this, (TextureView) findViewById(R.id.cameraPreview));
        speechManager = new SpeechManager(this);

        camera.turnOnFlashlight();

        algorithmController = new AlgorithmController().initialize(speechManager, camera);
        emotionController = new EmotionController().initialize(speechManager, camera);
        algorithmEmotionController = new AlgorithmEmotionController().initialize(speechManager, camera);

        colorPagerFragmentAdapter = new ColorPagerFragmentAdapter(getSupportFragmentManager(),
                algorithmController,
                emotionController,
                algorithmEmotionController
        );

        viewPager.setAdapter(colorPagerFragmentAdapter);

    }

    /**
     * Update function used to update Views
     */
    private void update() {

        final int color = camera.getColor();

        try {
            Thread.sleep(100);
        } catch(Exception e) {

        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                colorFinder.updateColor(color);
            }
        });

        currentController = (ColorViewController)colorPagerFragmentAdapter.getItem(viewPager.getCurrentItem());
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
                    currentController.onVolumeUp();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_UP) {
                    currentController.onVolumeDown();
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
        camera.turnOnFlashlight();
        pause = false;
    }


    @Override
    protected void onStart(){
        super.onStart();
        camera.turnOnFlashlight();
    }

    @Override
    protected void onPause() {
        camera.turnOffFlashlight();
        camera.closeCamera();
        super.onPause();
        pause = true;
    }
}
