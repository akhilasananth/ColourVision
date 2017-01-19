package com.example.home.camera;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by robertfernandes on 1/19/2017.
 */

public class ItemFragment extends Fragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.item_layout, container, false);

        TableLayout table = (TableLayout) view.findViewById(R.id.table);

        for (int i = 0; i < 40; i++) {
            TableRow row = new TableRow(getActivity());

            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(R.mipmap.ic_launcher);
            ImageView imageView1 = new ImageView(getActivity());
            imageView1.setImageResource(R.mipmap.ic_launcher);

            row.addView(imageView);
            row.addView(imageView1);
            table.addView(row);
        }


        return view;
    }
}
