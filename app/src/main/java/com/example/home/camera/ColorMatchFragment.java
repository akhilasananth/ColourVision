package com.example.home.camera;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by robertfernandes on 1/17/2017.
 */

public class ColorMatchFragment extends Fragment {

    private SurfaceView colorView1;
    private SurfaceView colorView2;
    private SurfaceView colorView3;
    private SurfaceView colorView4;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.color_match, container, false);

        colorView1 = (SurfaceView) view.findViewById(R.id.color1);
        colorView2 = (SurfaceView) view.findViewById(R.id.color2);
        colorView3 = (SurfaceView) view.findViewById(R.id.color3);
        colorView4 = (SurfaceView) view.findViewById(R.id.color4);

        colorView1.setBackgroundColor(Color.RED);
        colorView2.setBackgroundColor(Color.GREEN);
        colorView3.setBackgroundColor(Color.BLUE);
        colorView4.setBackgroundColor(Color.YELLOW);

        return view;
    }

    public void updateColors(int c1, int c2, int c3, int c4) {
        colorView1.setBackgroundColor(c1);
        colorView2.setBackgroundColor(c2);
        colorView3.setBackgroundColor(c3);
        colorView4.setBackgroundColor(c4);
    }
}
