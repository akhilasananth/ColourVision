package com.example.home.camera.ColorMatch;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.home.camera.R;

/**
 * Created by robertfernandes on 3/3/2017.
 */

public class EmotionController extends ColorViewController {

    private SpeechManager speechManager;
    private Camera camera;

    private CurrentColorChoice currentChoice;
    private ColorSelections colorSelections;

    private String[] emotions = {
            "Happy", "Sad"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ColorPager.Emotion.getLayoutResId(), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView listView = (ListView) getView().findViewById(R.id.emotionsList);

        for (String emotion : emotions) {
            TextView tv = new TextView(getContext());
            tv.setText(emotion);
            tv.setTextSize(20);
            listView.addFooterView(tv);
        }

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((TextView)view).getText();
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        colorSelections = (ColorSelections) getView().findViewById(R.id.colorSelections);

        addView(colorSelections);
    }

    public EmotionController initialize(SpeechManager speechManager, Camera camera) {
        this.speechManager = speechManager;
        this.camera = camera;
        return this;
    }

    @Override
    public void onVolumeUp() {

    }

    @Override
    public void onVolumeDown() {

    }

    @Override
    public void onTouchScreen() {

    }

    @Override
    public void onSwipeUp() {

    }

    @Override
    public void onSwipeDown() {

    }

    @Override
    public void onSwipeLeft() {

    }

    @Override
    public void onSwipeRight() {

    }
}
