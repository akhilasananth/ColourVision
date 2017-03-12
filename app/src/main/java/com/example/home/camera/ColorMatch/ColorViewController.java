package com.example.home.camera.ColorMatch;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertfernandes on 3/3/2017.
 */

public abstract class ColorViewController extends Fragment {

    protected String name;

//    public String getName() {
//        return name;
//    }

//    public void setName(String name){
//        this.name = name;
//    }

    public abstract void onVolumeUp();

    public abstract void onVolumeDown();

    public abstract void onTouchScreen();

    public abstract void onSwipeUp();

    public abstract void onSwipeDown();

    public abstract void onSwipeLeft();

    public abstract void onSwipeRight();
}
