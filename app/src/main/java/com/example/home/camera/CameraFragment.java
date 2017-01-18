package com.example.home.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.home.camera.ColorHelper.getAverageColor;

/**
 * Created by robertfernandes on 1/17/2017.
 */

public class CameraFragment extends Fragment {

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
        overlayViewUpdateThread.start();
        cameraPreview = (CameraPreview) view.findViewById(R.id.cameraPreview);
        overlayView = (OverlayView) view.findViewById(R.id.overlayView);
        startThreads();

        return view;
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
                        heavyWork(cameraPreview);
                        prevTime = currTime;
                    }
                    overlayView.drawFrame();
                }
            }
        }
    });

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

    public void heavyWork(CameraPreview cPrev) {
        Bitmap bmp = cPrev.getBitmap();
        if (bmp != null) {
            int searchRadius = 5;
            int searchDiameter = searchRadius * 2;
            int[] colors = new int[searchDiameter * searchDiameter];
            int startX = cPrev.getWidth() / 2 - searchRadius;
            int startY = cPrev.getHeight() / 2 - searchRadius;

            bmp.getPixels(colors, 0, searchDiameter, startX, startY, searchDiameter, searchDiameter);

            color = getAverageColor(colors);

            overlayView.setColor(color);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onDestroyView() {
        quit = true;
        killThreads();
        super.onDestroyView();
    }

    public void onDetach() {
        killThreads();
        super.onDetach();
    }

    public void onPause() {
        super.onPause();
        cameraPreview.closeCamera();
    }
}
