package com.example.home.camera;

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

import java.util.List;

/**
 * Created by robertfernandes on 1/20/2017.
 */

public abstract class TableViewFragment extends PageFragment {

    private TableLayout tableLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_layout, container, false);
        tableLayout = (TableLayout) view.findViewById(R.id.table);

        tableLayout.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));
        tableLayout.setWeightSum(1.0f);

        update();

        return view;
    }

    public void update() {
        tableLayout.removeAllViews();
        List<TableRow> rows = createRows();

        for (TableRow row : rows) {
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 0, 1f / rows.size()));
            row.setWeightSum(1.0f);
            row.setPadding(10, 10, 10, 10);

            tableLayout.addView(row);
        }
    }

    public abstract List<TableRow> createRows();

}
