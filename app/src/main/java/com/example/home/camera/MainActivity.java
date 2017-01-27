package com.example.home.camera;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.*;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.home.camera.colorHelper.ColorHelper;
import com.example.home.camera.colorHelper.Matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "AndroidCameraApi";
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private ColorHelper colorHelper;

    private static final int FIRST_PAGE = 1;

    private int currentPage = FIRST_PAGE;

    private int color1 = Color.BLACK;
    private int color2 = Color.BLACK;

    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    private ColorViewFragment colorViewFragment;

    //private DBHandler dbHandler;
    private TextToSpeech speech;

    private static final int FIRST = 1;
    private static final int SECOND = 2;
    private int currentColor = FIRST;
    private Matcher matcher;

    private SurfaceView actionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        //resetDatabase();

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentPage);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

            @Override
            public void onPageSelected(int position) {

                pagerAdapter.getItem(currentPage).onPause();
                pagerAdapter.getItem(position).onResume();
                pagerAdapter.updateFragments(position);
                currentPage = position;
            }
        });

        actionListener = (SurfaceView) findViewById(R.id.actionListener);
        actionListener.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    performAction(currentPage);
                return true;
            }
        });

        speech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });
        speech.setLanguage(Locale.getDefault());

        colorViewFragment = new ColorViewFragment();
        getFragmentManager().beginTransaction().add(R.id.colorView, colorViewFragment).commit();
        colorHelper = new ColorHelper(this);
    }
/*
    private void resetDatabase() {
        dbHandler = new DBHandler(this);

        dbHandler.onUpgrade(dbHandler.getWritableDatabase(), 0, 0);
    }
*/
    private void speak(String message) {
        speech.speak(message, TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
    }

    private void speakList(List<String> messages) {
        for (String s : messages) {
            speech.speak(s, TextToSpeech.QUEUE_ADD, Bundle.EMPTY, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        int color = pagerAdapter.getColor();

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    pagerAdapter.updateFragments(currentPage);
                    colorViewFragment.setColor1(color);
                    speak(ColorHelper.getColorName(ColorHelper.getClosestColor(color)));
                    currentColor = FIRST;
                    Log.i(TAG, ColorHelper.getColorName(ColorHelper.getClosestColor(color)));
                    color1 = color;
                    //dbHandler.insertItem(ColorHelper.getColorName(ColorHelper.getClosestColor(color)));
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    pagerAdapter.updateFragments(currentPage);
                    colorViewFragment.setColor2(color);
                    speak(ColorHelper.getColorName(ColorHelper.getClosestColor(color)));
                    Log.i(TAG, ColorHelper.getColorName(ColorHelper.getClosestColor(color)));
                    currentColor = SECOND;
                    color2 = color;
                    //dbHandler.insertItem(ColorHelper.getColorName(ColorHelper.getClosestColor(color)));
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
                Toast.makeText(MainActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void performAction(int currentPage) {
        matcher = new Matcher();
        switch (currentPage) {
            case ScreenSlidePagerAdapter.CAMERAPREVIEW:
                checkMatch(color1, color2);
                break;
            case ScreenSlidePagerAdapter.COLORMATCH:
                listMatches(currentColor);
                pagerAdapter.setMatcher(matcher);
                pagerAdapter.updateFragments(currentPage);
                break;
            default:
                break;
        }
    }

    private void listMatches(int currentColor) {
        List<String> colorNames = new ArrayList<>();
        List<Integer> matches = null;
        switch (currentColor) {
            case FIRST:
                matches = matcher.getMatchingColors(color1);
                break;
            case SECOND:
                matches = matcher.getMatchingColors(color2);
                break;
            default:
                break;
        }

        if (matches!= null) {
            for (Integer i : matches) {
                colorNames.add(ColorHelper.getColorName(ColorHelper.getClosestColor(i)));
            }
            speakList(colorNames);
        }
    }

    public void checkMatch(int color1, int color2) {
        matcher = new Matcher();
        if (matcher.isMatch(color1, color2)) {
            speak("Match");
        } else {
            speak("Not a Match");
        }
    }

    public int getColor1() {
        return color1;
    }

    public int getColor2() {
        return color2;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        super.onPause();
    }
}