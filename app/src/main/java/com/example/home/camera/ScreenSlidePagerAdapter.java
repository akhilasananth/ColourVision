package com.example.home.camera;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by robertfernandes on 1/17/2017.
 */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 2;

    private static final int COLORMATCH = 0;
    private static final int CAMERAPREVIEW = 1;

    private CameraFragment cameraFragment;
    private ColorMatchFragment colorMatchFragment;

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
        cameraFragment = new CameraFragment();
        colorMatchFragment = new ColorMatchFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case COLORMATCH:
                return colorMatchFragment;
            case CAMERAPREVIEW:
                return cameraFragment;
            default:
                return null;
        }
    }

    public int getColor() {
        return cameraFragment.getColor();
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
