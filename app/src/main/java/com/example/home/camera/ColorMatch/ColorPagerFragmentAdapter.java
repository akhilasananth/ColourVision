package com.example.home.camera.ColorMatch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by robertfernandes on 3/3/2017.
 */

public class ColorPagerFragmentAdapter extends FragmentPagerAdapter {

    private AlgorithmController algorithmController;

    private EmotionController emotionController;

    public ColorPagerFragmentAdapter(FragmentManager fm, AlgorithmController algorithmController, EmotionController emotionController) {
        super(fm);
        this.algorithmController = algorithmController;
        this.emotionController = emotionController;
    }

    @Override
    public Fragment getItem(int position) {
        ColorPager colorPager = ColorPager.values()[position];
        switch (colorPager) {
            case Algorithm:
                return algorithmController;
            case Emotion:
                return emotionController;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return ColorPager.values().length;
    }
}