package com.example.home.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by robertfernandes on 1/20/2017.
 */

public class ColorView extends View {
    public ColorView(Context context) {
        super(context);
    }

    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}
