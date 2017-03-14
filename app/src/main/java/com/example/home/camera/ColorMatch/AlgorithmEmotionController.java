package com.example.home.camera.ColorMatch;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.example.home.camera.R;
import com.example.home.camera.colorHelper.ColorHelper;
import com.example.home.camera.colorHelper.Matcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertfernandes on 3/9/2017.
 */

public class AlgorithmEmotionController extends ColorViewController {



    private ColorSelections colorSelections;
    private SpeechManager speechManager;
    private CurrentColorChoice currentColorChoice;
    private String emotion = "";
    private int index =0;

    private Camera camera;

    private NumberPicker numberPicker;
    private String[] emotions;

    private Matcher matcher = new Matcher();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ColorPager.General.getLayoutResId(), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        emotions = getContext().getResources().getStringArray(R.array.emotions);

        currentColorChoice = new CurrentColorChoice(getView().findViewById(R.id.initialColor));
        colorSelections = new ColorSelections(getView().findViewById(R.id.colorSelections));

        numberPicker = (NumberPicker)getView().findViewById(R.id.emotion);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(emotions.length - 1);
        numberPicker.setDisplayedValues(emotions);

        emotion = emotions[numberPicker.getValue()];
    }

    public void addInitialColor(int color) {
        currentColorChoice.setColor(color);
    }

    public void addComparingColor(int color) {
        this.index++;
        colorSelections.addColor(color);
    }

    public void resetColors() {
        colorSelections.resetColors();
        currentColorChoice.reset();
    }

    public List<IndexedColor> getMatchingColors() {
        List<IndexedColor> finalMatches = new ArrayList<IndexedColor>();
        boolean hasEmotionMatchColor = false;
        List<IndexedColor> matches = matcher.isMatch(currentColorChoice.getColor(), colorSelections.getColors());
        for(IndexedColor c : matches){
            hasEmotionMatchColor = matcher.isMatch(emotion,c.getColor());
        }

        if(hasEmotionMatchColor || matcher.isMatch(emotion,currentColorChoice.getColor())){
            finalMatches = matches;
        }
        return finalMatches;
    }

    public void checkMatches() {
        List<String> colorNames = new ArrayList<>();

        List<IndexedColor> colors = getMatchingColors();

        for (IndexedColor c : colors) {
            if(c.getIndex()<index) {
                colorNames.add("Color " + (c.getIndex() + 1) + " " + ColorHelper.getColorName(c.getColor()));
            }
        }

        speechManager.speakList(colorNames);

        if(colors.isEmpty()){
            speechManager.speak("No matches");
        }
    }

    public AlgorithmEmotionController initialize(SpeechManager speechManager, Camera camera) {
        this.speechManager = speechManager;
        this.camera = camera;
//        setName("General Matching");
        return this;
    }

    @Override
    public void onVolumeUp() {
        int color = camera.getColor();
        addInitialColor(color);
        speechManager.speak(ColorHelper.getColorName(color));
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
    }

    @Override
    public void onSwipeRight() {
        resetColors();
        speechManager.speak("Emotion matching");
    }
}
