package com.example.home.camera.ColorMatch;

import android.content.Context;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.home.camera.R;
import com.example.home.camera.colorHelper.ColorHelper;
import com.example.home.camera.colorHelper.Matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by robertfernandes on 3/3/2017.
 */

public class EmotionController extends ColorViewController {

    private SpeechManager speechManager;
    private Camera camera;

    private String emotion = "";
    private ColorSelections colorSelections;
    private NumberPicker numberPicker;
    private String[] emotions;
    private int index =0;

    private Matcher matcher = new Matcher();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ColorPager.Emotion.getLayoutResId(), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        emotions = getContext().getResources().getStringArray(R.array.emotions);

        numberPicker = (NumberPicker)getView().findViewById(R.id.emotion);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(emotions.length - 1);
        numberPicker.setDisplayedValues(emotions);

        colorSelections = new ColorSelections(getView().findViewById(R.id.colorSelections));
        emotion = emotions[numberPicker.getValue()];
    }

    public EmotionController initialize(SpeechManager speechManager, Camera camera) {
        this.speechManager = speechManager;
        this.camera = camera;
//        setName("Emotion Matching");
        return this;
    }

    public void addComparingColor(int color) {
        this.index++;
        colorSelections.addColor(color);
    }

    public List<IndexedColor> getMatchingColors() {
        return matcher.isMatch(emotion, colorSelections.getColors());
    }

    public void checkMatches() {
        List<String> colorNames = new ArrayList<>();
        List<IndexedColor> matchingColors = getMatchingColors();

        for (IndexedColor c : matchingColors) {

            if(c.getIndex()<this.index){
                colorNames.add("Color " + (c.getIndex()+1) + " " + ColorHelper.getColorName(c.getColor()));
            }

        }
        speechManager.speakList(colorNames);

        if(matchingColors.isEmpty()){
            speechManager.speak("No matches");
        }
    }
    public void resetColors() {
        colorSelections.resetColors();
    }

    @Override
    public void onVolumeUp() {
        speechManager.speak(emotion);
    }

    @Override
    public void onVolumeDown() {
        int color = camera.getColor();
        addComparingColor(color);
        speechManager.speak(ColorHelper.getColorName(color));
    }

    @Override
    public void onTouchScreen() {
        checkMatches();
    }

    @Override
    public void onSwipeUp() {
        numberPicker.setValue(numberPicker.getValue() + 1);
        emotion = emotions[numberPicker.getValue()];
        speechManager.speak(emotion);
    }

    @Override
    public void onSwipeDown() {
        numberPicker.setValue(numberPicker.getValue() - 1);
        emotion = emotions[numberPicker.getValue()];
        speechManager.speak(emotion);
    }

    @Override
    public void onSwipeLeft() {
        resetColors();
        speechManager.speak("General matching");
    }

    @Override
    public void onSwipeRight() {
        resetColors();
        speechManager.speak("Color wheel matching");
    }
}
