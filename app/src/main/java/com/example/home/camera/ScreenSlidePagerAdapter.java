package com.example.home.camera;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertfernandes on 1/17/2017.
 */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 3;

    private static final int COLORMATCH = 0;
    private static final int CAMERAPREVIEW = 1;
    private static final int ITEMVIEW = 2;

    private CameraFragment cameraFragment;
    private ColorMatchFragment colorMatchFragment;
    private ItemFragment itemFragment;

    private List<PageFragment> fragmentList;

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        cameraFragment = new CameraFragment();
        colorMatchFragment = new ColorMatchFragment();
        itemFragment = new ItemFragment();

        fragmentList.add(cameraFragment);
        fragmentList.add(colorMatchFragment);
        fragmentList.add(itemFragment);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case COLORMATCH:
                return colorMatchFragment;
            case CAMERAPREVIEW:
                return cameraFragment;
            case ITEMVIEW:
                return itemFragment;
            default:
                return null;
        }
    }

    public void updateFragments() {
        for (PageFragment fragment : fragmentList) {
            if (fragment.isVisible())
                fragment.update();
        }

    }

    public int getColor() {
        int color = cameraFragment.getColor();
        colorMatchFragment.setColor(color);
        return color;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
