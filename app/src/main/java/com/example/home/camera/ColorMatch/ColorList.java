package com.example.home.camera.ColorMatch;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 23/02/2017.
 */

public class ColorList {

    private int maxNumColors = 0;

    private int numColors = 0;

    private int index = 0;

    private int[] colors;

    public ColorList(int maxNumColors) {
        this.maxNumColors = maxNumColors;
        colors = new int[maxNumColors];
        for (int i = 0; i < maxNumColors; i++) {
            colors[i] = Color.BLACK;
        }
    }

    public void addColor(int color) {
        setColor(index, color);
        index++;
        if (index == maxNumColors) {
            index = 0;
        }
        if (numColors != maxNumColors) {
            numColors++;
        }
    }


    public void setColor(int pos, int color) {
        colors[pos] = color;
    }

    public int[] getColors() {
        return colors;
    }

    public int getNumColors() {
        return numColors;
    }

    public int getIndex() {
        return index;
    }

    public int getColorAt(int i) {
        return colors[i];
    }

    public int selectColor(int i) {
        index = i;
        return colors[i];
    }



}
