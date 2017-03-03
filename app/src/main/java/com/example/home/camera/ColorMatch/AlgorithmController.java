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

    private static final int MAX_NUM_COLORS = 6;

    private int initialColor = Color.BLACK;

    private ColorSelections colorSelections;
    private SpeechManager speechManager;
    private CurrentColorChoice currentChoice;

    private Camera camera;

    private Matcher matcher = new Matcher(Matcher.MatchType.Complimentary);
    private ColorList colorList = new ColorList(MAX_NUM_COLORS);

    public List<Integer> getColors() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i : colorList.getColors()) {
            list.add(i);
        }
        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ColorPager.Algorithm.getLayoutResId(), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentChoice = (CurrentColorChoice) getView().findViewById(R.id.currentChoice);
        colorSelections = (ColorSelections) getView().findViewById(R.id.colorSelections);

        addView(currentChoice);
        addView(colorSelections);
    }

    public void addInitialColor(int color) {
        initialColor = color;
        currentChoice.setColor(color);
    }

    public void addComparingColor(int color) {
        colorList.addColor(color);
        colorSelections.setComparingColors(colorList.getColors());
    }

    public void resetColors() {
        colorList = new ColorList(MAX_NUM_COLORS);
        colorSelections.setComparingColors(colorList.getColors());
    }

    public List<Integer> getMatchingColors() {
        return matcher.isMatch(initialColor, getColors());
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

    public void drawViews() {
        if (colorSelections != null && currentChoice != null) {
            colorSelections.draw();
            currentChoice.draw();
        }

    }

    @Override
    public void onVolumeUp() {
        addInitialColor(camera.getColor());
    }

    @Override
    public void onVolumeDown() {
        addComparingColor(camera.getColor());
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
