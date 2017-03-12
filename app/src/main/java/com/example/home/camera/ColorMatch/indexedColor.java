package com.example.home.camera.ColorMatch;

/**
 * Created by StartGazer on 3/12/2017.
 */

public class IndexedColor {
    private int color;
    private int index;

    public IndexedColor( int index, int color){
        this.index = index;
        this.color = color;
    }

    public int getIndex() {
        return index;
    }



    public int getColor() {
        return color;
    }

}
