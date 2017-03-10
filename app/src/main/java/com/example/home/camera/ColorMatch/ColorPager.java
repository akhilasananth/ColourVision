package com.example.home.camera.ColorMatch;

import com.example.home.camera.R;

/**
 * Created by robertfernandes on 3/3/2017.
 */

public enum ColorPager {

    Algorithm(R.layout.algorithm_layout),
    Emotion(R.layout.emotion_layout),
    General(R.layout.general_layout);

    private int layoutResId;

    ColorPager(int layoutResId) {
        this.layoutResId = layoutResId;
    }

    public int getLayoutResId() {
        return layoutResId;
    }
}
