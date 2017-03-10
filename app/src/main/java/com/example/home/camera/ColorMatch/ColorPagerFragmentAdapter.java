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

    private AlgorithmEmotionController algorithmEmotionController;

    public ColorPagerFragmentAdapter(FragmentManager fm, AlgorithmController algorithmController, EmotionController emotionController, AlgorithmEmotionController algorithmEmotionController) {
        super(fm);
        this.algorithmController = algorithmController;
        this.emotionController = emotionController;
        this.algorithmEmotionController = algorithmEmotionController;
    }

    @Override
    public Fragment getItem(int position) {
        ColorPager colorPager = ColorPager.values()[position];
        switch (colorPager) {
            case Algorithm:
                return algorithmController;
            case Emotion:
                return emotionController;
            case General:
                return algorithmEmotionController;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return ColorPager.values().length;
    }
}