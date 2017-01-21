package com.example.home.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.home.camera.colorHelper.ColorHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by robertfernandes on 1/19/2017.
 */

public class ItemFragment extends TableViewFragment {

    private DBHandler dbHandler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new DBHandler(getActivity());
    }

    @Override
    public List<TableRow> createRows() {
        ArrayList<TableRow> rows = new ArrayList<>();
        HashMap<Integer, String> colors = dbHandler.getAllItems();

        for (final int colorId : colors.keySet()) {
            TableRow row = new TableRow(getActivity());
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 0, 1f / colors.size()));
            row.setWeightSum(1.0f);
            row.setPadding(10, 10, 10, 10);

            TextView name = new TextView(getActivity());
            ColorView colorView = new ColorView(getActivity());

            name.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.5f));
            colorView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.5f));

            name.setText(colors.get(colorId));
            colorView.setBackgroundColor(ColorHelper.getColor(colors.get(colorId)));

            row.addView(name);
            row.addView(colorView);

            rows.add(row);
        }

        return rows;
    }
}
