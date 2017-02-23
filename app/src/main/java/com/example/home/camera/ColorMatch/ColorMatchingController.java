package com.example.home.camera.ColorMatch;

import android.graphics.Color;

import com.example.home.camera.colorHelper.Matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by home on 23/02/2017.
 */

public class ColorMatchingController {

    private ColorList colorList;

    private Matcher matcher;

    public ColorMatchingController() {
        colorList = new ColorList(6);
        matcher = new Matcher();
        matcher.selectRandomStrategy();
    }

    public List<Integer> getColors() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i : colorList.getColors()) {
            list.add(i);
        }
        return list;
    }

}
