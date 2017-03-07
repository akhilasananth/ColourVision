package com.example.home.camera.ColorMatch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.home.camera.R;
import com.example.home.camera.colorHelper.ColorHelper;
import com.example.home.camera.colorHelper.Matcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 23/02/2017.
 */

public class AlgorithmController extends ColorViewController {

    private static final String TAG = "AlgorithmController";

    private ColorSelections colorSelections;
    private SpeechManager speechManager;
    private CurrentColorChoice currentChoice;

    private Camera camera;

    private Matcher matcher = new Matcher(Matcher.MatchType.Complimentary);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ColorPager.Algorithm.getLayoutResId(), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentChoice = new CurrentColorChoice(getView().findViewById(R.id.initialColor));
        colorSelections = new ColorSelections(getView().findViewById(R.id.colorSelections));
    }

    public void addInitialColor(int color) {
        currentChoice.setColor(color);
    }

    public void addComparingColor(int color) {
        colorSelections.addColor(color);
    }

    public void resetColors() {
        colorSelections.resetColors();
    }

    public List<Integer> getMatchingColors() {
        return matcher.isMatch(currentChoice.getColor(), colorSelections.getColors());
    }

    public void checkMatches() {
        List<String> colorNames = new ArrayList<>();

        for (Integer i : getMatchingColors()) {
            colorNames.add(ColorHelper.getColorName(ColorHelper.getClosestColor(i)));
        }

        speechManager.speakList(colorNames);
    }

    public AlgorithmController initialize(SpeechManager speechManager, Camera camera) {
        this.speechManager = speechManager;
        this.camera = camera;
        return this;
    }

    @Override
    public void onVolumeUp() {
        int color = camera.getColor();
        addInitialColor(color);
        speechManager.speak(ColorHelper.getColorName(ColorHelper.getClosestColor(color)));
    }

    @Override
    public void onVolumeDown() {
        int color = camera.getColor();
        addComparingColor(color);
        speechManager.speak(ColorHelper.getColorName(ColorHelper.getClosestColor(color)));
    }

    @Override
    public void onTouchScreen() {
        checkMatches();
    }

    @Override
    public void onSwipeUp() {
        resetColors();
    }

    @Override
    public void onSwipeDown() {
        resetColors();
    }

    @Override
    public void onSwipeLeft() {
        resetColors();
    }

    @Override
    public void onSwipeRight() {
        resetColors();
    }
}
