package com.example.home.camera.ColorMatch;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.example.home.camera.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertfernandes on 2/21/2017.
 */

public class ColorSelections {

    private static final int MAX_NUM_COLORS = 6;

    private View[] colors = new View[MAX_NUM_COLORS];

    private ColorList colorList = new ColorList(MAX_NUM_COLORS);

    public ColorSelections(View view) {
        colors[0] = view.findViewById(R.id.color1);
        colors[1] = view.findViewById(R.id.color2);
        colors[2] = view.findViewById(R.id.color3);
        colors[3] = view.findViewById(R.id.color4);
        colors[4] = view.findViewById(R.id.color5);
        colors[5] = view.findViewById(R.id.color6);
    }

    public void addColor(int color) {
        colorList.addColor(color);
        updateColors();
    }

    public void updateColors() {
        int[] colorArray = colorList.getColors();
        for (int i = 0; i < colorList.getNumColors(); i++) {
            colors[i].setBackgroundColor(colorArray[i]);
        }
    }

    public void resetColors() {
        colorList = new ColorList(MAX_NUM_COLORS);
        for (View c : colors) {
            c.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public List<IndexedColor> getColors() {
        ArrayList<IndexedColor> list = new ArrayList<>();
        int[] colorArray = colorList.getColors();
        for (int i = 0; i < colorArray.length; i++) {
            list.add(new IndexedColor(i,colorArray[i]));
        }
        return list;
    }

    public int[] getColorArray(){
        return colorList.getColors();
    }

}
