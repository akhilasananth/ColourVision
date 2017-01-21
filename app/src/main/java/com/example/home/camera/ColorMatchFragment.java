package com.example.home.camera;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.home.camera.colorHelper.ColorHelper;
import com.example.home.camera.colorHelper.Matcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertfernandes on 1/17/2017.
 */

public class ColorMatchFragment extends TableViewFragment {

    private int color = Color.BLACK;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public List<TableRow> createRows() {
        Matcher matcher = new Matcher();
        List<Integer> matchingColors = matcher.getMatchingColors(color);

        List<TableRow> rows = new ArrayList<>();

        for (Integer i : matchingColors) {
            TableRow row = new TableRow(getActivity());
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 0, 1f / matchingColors.size()));
            row.setWeightSum(1.0f);

            TextView name = new TextView(getActivity());
            ColorView colorView = new ColorView(getActivity());

            name.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.5f));
            colorView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.5f));

            name.setText(ColorHelper.getColorName(ColorHelper.getClosestColor(i)));
            colorView.setBackgroundColor(i);

            row.addView(name);
            row.addView(colorView);
            rows.add(row);
        }
        return rows;
    }
}
