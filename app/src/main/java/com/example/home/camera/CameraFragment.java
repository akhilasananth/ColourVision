package com.example.home.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.home.camera.colorHelper.ColorHelper.getAverageColor;

/**
 * Created by robertfernandes on 1/17/2017.
 */

public class CameraFragment extends PageFragment {

    String TAG = "ASDF";

    private boolean running = false;
    private boolean quit = false;

    private CameraPreview cameraPreview;
    private OverlayView overlayView;

    private int color = Color.BLACK;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.camera_preview, container, false);

        cameraPreview = (CameraPreview) view.findViewById(R.id.cameraPreview);
        overlayView = (OverlayView) view.findViewById(R.id.overlayView);

        overlayViewUpdateThread.start();
        startThreads();

        return view;
    }

    public void update() {
        AsyncTask<String, Void, String> operation = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                heavyWork();
                return "Executed";
            }
        };

        operation.execute();
    }

    public int getColor() {
        return color;
    }

    public void startThreads() {
        running = true;
        if (cameraPreview != null)
            cameraPreview.startBackgroundThread();
    }

    public void pauseThreads() {
        running = false;
        if (cameraPreview != null)
            cameraPreview.stopBackgroundThread();
    }

    public void killThreads() {
        pauseThreads();
        quit = true;
        try {
            if (overlayViewUpdateThread != null)
                overlayViewUpdateThread.join();
            overlayViewUpdateThread = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Thread overlayViewUpdateThread = new Thread(new Runnable() {
        @Override
        public void run() {
            long prevTime = System.currentTimeMillis();
            running = true;
            while (!quit) {
                if (running) {
                    long currTime = System.currentTimeMillis();
                    if (currTime >= prevTime + 1000) {
                        Log.i(TAG, "executing task");
                        heavyWork();
                        prevTime = currTime;
                    }
                }
            }
        }
    });

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void heavyWork() {
        Bitmap bmp = cameraPreview.getBitmap();
        if (bmp != null) {
            int searchRadius = 5;
            int searchDiameter = searchRadius * 2;
            int[] colors = new int[searchDiameter * searchDiameter];
            int startX = cameraPreview.getWidth() / 2 - searchRadius;
            int startY = cameraPreview.getHeight() / 2 - searchRadius;

            bmp.getPixels(colors, 0, searchDiameter, startX, startY, searchDiameter, searchDiameter);

            color = getAverageColor(colors);

            overlayView.setColor(color);
            overlayView.drawFrame();
        }
    }

    public void onDestroyView() {
        quit = true;
        killThreads();
        super.onDestroyView();
    }
}
