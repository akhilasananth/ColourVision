package com.example.home.camera.ColorMatch;

import android.graphics.Color;
import android.util.Log;

import com.example.home.camera.colorHelper.Matcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 23/02/2017.
 */

public class ColorMatchingController {

    private static final String TAG = "ColorMatchingController";

    private static final int MAX_NUM_COLORS = 6;

    private int initialColor = Color.BLACK;

    private ColorList colorList;

    private Matcher matcher;

    private ColorSelections colorSelections;

    private SpeechManager speechManager;

    public ColorMatchingController(ColorSelections colorSelections, SpeechManager speechManager) {
        this.colorSelections = colorSelections;
        this.speechManager = speechManager;
        colorList = new ColorList(MAX_NUM_COLORS);
        matcher = new Matcher(Matcher.MatchType.Complimentary);
    }

    public List<Integer> getColors() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i : colorList.getColors()) {
            list.add(i);
        }
        return list;
    }

    public void addInitialColor(int color) {
        initialColor = color;
        colorSelections.setInitialColor(color);
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
        for (Integer i : getMatchingColors()) {
            Log.e(TAG, "" + Integer.toHexString(i));
        }
    }

}
