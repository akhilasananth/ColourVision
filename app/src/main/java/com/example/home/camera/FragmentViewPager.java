package com.example.home.camera;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by robertfernandes on 2/9/2017.
 */

public class FragmentViewPager extends ViewPager {
    public FragmentViewPager(Context context) {
        super(context);
    }

    public FragmentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (MotionEvent.ACTION_MOVE == e.getAction()) {
            super.onInterceptTouchEvent(e);
        } else if (MotionEvent.ACTION_DOWN == e.getAction()) {
            onTouch();
        } else {
            super.onInterceptTouchEvent(e);
        }
        return false;
    }

    public void onTouch() {}
}
