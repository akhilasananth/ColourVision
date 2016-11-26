package com.example.home.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.Toast;

import static com.example.home.camera.ColorHelper.getAverageColor;

public class MainActivity extends Activity {
    private static final String TAG = "AndroidCameraApi";
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private OverlayView overlayView;
    private CameraPreview cameraPreview;
    private ColorView colorView;

    private HandlerThread handlerThread;
    private Handler handler;

    private int color = Color.BLACK;

    private ColorHelper colorHelper;

    private SensorManager mSensorManager;

    private Sensor mLightSensor;

    private boolean running = false;

    private boolean quit = false;

    private int color1;
    private int color2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        overlayView = (OverlayView) findViewById(R.id.overlayView);
        colorView = (ColorView) findViewById(R.id.colorView);

        cameraPreview = (CameraPreview) findViewById(R.id.cameraPreview);
        cameraPreview.setSurfaceTextureListener(textureListener);


        colorHelper = new ColorHelper(this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        onFrameThread.start();

        overlayView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                //Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), ColorMatchActivity.class);
                i.putExtra("Color1", color1);
                i.putExtra("Color2", color2);
                startActivity(i);
            }
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Thread onFrameThread = new Thread(new Runnable() {
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
                }
            }
        }
    });

    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    colorView.setColor1(color);
                    color1 = color;
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    colorView.setColor2(color);
                    color2 = color;
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            cameraPreview.openCamera();

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        int frames = 0;
        int FPS = 0;
        long prevTime = System.currentTimeMillis();

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            //Log.i(TAG, "FPS: " + FPS);

            long currentTime = System.currentTimeMillis();

            if (currentTime >= prevTime + 1000) {
                prevTime = currentTime;
                FPS = frames;
                frames = 0;
            }
            frames++;
        }
    };

    private SensorEventListener sensorEventListener = new SensorEventListener() {

        boolean darkFlag = false;
        boolean msgSent = true;
        int threshold = 200;

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                //Log.i("light level: ", "" + event.values[0]);
                if (event.values[0] > threshold) {
                    darkFlag = true;
                } else {
                    darkFlag = false;
                    msgSent = false;
                }

                if (!msgSent && darkFlag) {
                    Toast.makeText(MainActivity.this, "light level: " + event.values[0], Toast.LENGTH_SHORT);
                    msgSent = true;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(MainActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void startBackgroundThread() {
        running = true;
        handlerThread = new HandlerThread("Camera Preview Background");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    public void stopBackgroundThread() {
        running = false;
        try {
            if (handlerThread != null) {
                handlerThread.quitSafely();
                handlerThread.join();
            }
            handlerThread = null;
            handler = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        startBackgroundThread();
        mSensorManager.registerListener(sensorEventListener, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        if (cameraPreview.isAvailable()) {
            cameraPreview.openCamera();
        } else {
            cameraPreview.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        cameraPreview.closeCamera();
        mSensorManager.unregisterListener(sensorEventListener);
        stopBackgroundThread();

        super.onPause();
    }

    public Handler getHandler() {
        return handler;
    }

    protected void onDestroy() {
        stopBackgroundThread();
        cameraPreview.closeCamera();
        quit = true;
        try {
            if (onFrameThread != null)
                onFrameThread.join();
            onFrameThread = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
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

            overlayView.drawFrame();
        }
    }
}