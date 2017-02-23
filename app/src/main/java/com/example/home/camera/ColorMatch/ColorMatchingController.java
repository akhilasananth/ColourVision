package com.example.home.camera.ColorMatch;

import android.graphics.Color;

import com.example.home.camera.colorHelper.Matcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 23/02/2017.
 */

public class ColorMatchingController {

    private static final int MAX_NUM_COLORS = 6;

    private ColorList colorList;

    private Matcher matcher;

    private ColorSelections colorSelections;

    public ColorMatchingController(ColorSelections colorSelections) {
        this.colorSelections = colorSelections;
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

    public void addColor(int color) {
        colorList.addColor(color);
        colorSelections.setColors(colorList.getColors());
    }

    public void resetColors() {
        colorList = new ColorList(MAX_NUM_COLORS);
        colorSelections.setColors(colorList.getColors());
    }

    public void checkMatches() {

    }

}
