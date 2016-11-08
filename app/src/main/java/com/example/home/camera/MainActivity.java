package com.example.home.camera;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.home.camera.ColorHelper.getAverageColor;

public class MainActivity extends Activity {
    private static final String TAG = "AndroidCameraApi";
    private TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private OverlayView overlayView;

    private ColorView colorView;

    private int color = Color.BLACK;

    private ColorHelper colorHelper;

    private SensorManager mSensorManager;

    private Sensor mLightSensor;

    private Thread onFrameThread;

    private boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        textureView = (TextureView) findViewById(R.id.texture);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);

        overlayView = (OverlayView) findViewById(R.id.overlayView);

        colorView = (ColorView) findViewById(R.id.colorView);

        colorHelper = new ColorHelper(this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        onFrameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                long prevTime = System.currentTimeMillis();
                running = true;
                while(running) {

                    long currTime = System.currentTimeMillis();
                    if (currTime >= prevTime + 1000) {
                        mBackgroundHandler.post(new Runnable() {
                            TextureView tView = textureView;
                            @Override
                            public void run() {
                                if (tView != null)
                                    heavyWork(tView);
                            }
                        });
                        prevTime = currTime;
                    }
                }
            }
        });
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    colorView.setColor1(color);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    colorView.setColor2(color);
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
            openCamera();
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
            //new LongOperation().execute("");
            Log.i(TAG, "FPS: " + FPS);

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
                Log.i("light level: ", "" + event.values[0]);
                if(event.values[0]>threshold){
                    darkFlag = true;
                }
                else{
                    darkFlag = false;
                    msgSent = false;
                }

                if(!msgSent && darkFlag){
                    Toast.makeText(MainActivity.this,"light level: " + event.values[0], Toast.LENGTH_SHORT );
                    msgSent = true;
                }

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = null;
            try {
                int height = image.getHeight();
                int width = image.getWidth();

                int searchRadius = 5;
                int searchDiameter = searchRadius * 2;

                int[] colors = new int[searchDiameter * searchDiameter];

                int x = width/2 - searchRadius;
                int y = height/2 - searchRadius;

                Rect cropRect = new Rect(x - searchRadius, y + searchRadius, x + searchRadius, y + searchRadius);
                image.setCropRect(cropRect);

                image = reader.acquireLatestImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.capacity()];
                buffer.get(bytes);

                BitmapFactory.decodeByteArray(bytes, 0, 0)
                    .getPixels(colors, 0, searchDiameter, x, y, searchDiameter, searchDiameter);

                color = getAverageColor(colors);

                overlayView.setColor(color);

                overlayView.drawFrame();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (image != null) {
                    image.close();
                }
            }
        }
    };

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //This is called when the camera is open
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }
        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }
        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        onFrameThread.start();
    }

    protected void stopBackgroundThread() {

        try {
            if (mBackgroundThread != null) {
                mBackgroundThread.quitSafely();
                mBackgroundThread.join();
            }
            mBackgroundThread = null;
            mBackgroundHandler = null;

            running = false;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;

            int width = imageDimension.getWidth();
            int height = imageDimension.getHeight();

            texture.setDefaultBufferSize(width, height);

            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(MainActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }

    protected void updatePreview() {
        if(null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        startBackgroundThread();
        mSensorManager.registerListener(sensorEventListener, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        closeCamera();
        mSensorManager.unregisterListener(sensorEventListener);
        stopBackgroundThread();
        super.onPause();
    }

    protected void onDestroy() {
        stopBackgroundThread();
        closeCamera();
        super.onDestroy();
    }

    public void heavyWork(TextureView tView){
        Bitmap bmp = tView.getBitmap();
        if(bmp != null){
            int searchRadius = 5;
            int searchDiameter = searchRadius * 2;
            int[] colors = new int[searchDiameter * searchDiameter];
            int startX = tView.getWidth()/2 - searchRadius;
            int startY = tView.getHeight()/2 - searchRadius;

            bmp.getPixels(colors, 0, searchDiameter, startX, startY, searchDiameter, searchDiameter);

            color = getAverageColor(colors);

            overlayView.setColor(color);

            overlayView.drawFrame();
        }
    }

    public class LongOperation extends AsyncTask<String, Void, String> {

        TextureView tView;

        @Override
        protected String doInBackground(String... params) {
            if (tView != null)
                heavyWork(tView);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {

        }

        @Override
        protected void onPreExecute() {
            tView = textureView;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}