package com.example.home.camera.ColorMatch;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.renderscript.RenderScript;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import com.example.home.camera.colorHelper.ColorHelper;

import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by robertfernandes on 2/21/2017.
 */

public class Camera {
    private static final String TAG = "Camera";
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private CameraCaptureSession cameraCaptureSession;

    private HandlerThread handlerThread;
    private Handler handler;

    private String cameraId;

    private CameraDevice cameraDevice;

    private Activity activity;
    private CaptureRequest.Builder captureRequestBuilder;

    private Size imageDimension;

    private TextureView textureView;

    public Camera(Activity activity, TextureView textureView) {
        this.activity = activity;
        this.textureView = textureView;
        textureView.setSurfaceTextureListener(surfaceTextureListener);
        handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice camera) {
            try {
                cameraDevice = camera;
                SurfaceTexture texture = textureView.getSurfaceTexture();
                texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
                Surface surface = new Surface(texture);

                captureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                captureRequestBuilder.set(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, CameraMetadata.CONTROL_VIDEO_STABILIZATION_MODE_ON);
                captureRequestBuilder.addTarget(surface);

                camera.createCaptureSession(Arrays.asList(surface), sessionStateCallback, handler);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onDisconnected(CameraDevice camera) {

        }

        @Override
        public void onError(CameraDevice camera, int error) {

        }
    };

    private CameraCaptureSession.StateCallback sessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            cameraCaptureSession = session;
            captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            try {
                session.setRepeatingRequest(captureRequestBuilder.build(), null, handler);
                turnOnFlashlight();
            } catch (Exception e) {

            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {

        }
    };

    public void openCamera() {
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;

            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                return;
            }

            manager.openCamera(cameraId, deviceStateCallback, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeCamera() {
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    public void turnOnFlashlight() {
        try {
            captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null);
        } catch (Exception e) {

        }
    }

    public void turnOffFlashlight() {
        try {
            captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_OFF);
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null);
        } catch (Exception e) {

        }
    }

    public int getColor() {

        //Get colors from the 5 bits on the center of the camera view
        Bitmap bmp = getImage();

        if (bmp != null) {
            int searchRadius = 5;
            int searchDiameter = searchRadius * 2;
            int[] colors = new int[searchDiameter * searchDiameter];
            int startX = bmp.getWidth()/2 - searchRadius;
            int startY = bmp.getHeight()/2 - searchRadius;

            bmp.getPixels(colors, 0, searchDiameter, startX, startY, searchDiameter, searchDiameter);

            //get the average color from the input colors
            int color = ColorHelper.getAverageColor(colors);


            //Gets the closest color to the corrected color
            int correctedColor = ColorHelper.colorCorrector(color);

            int finalColor = ColorHelper.getClosestColor(correctedColor);

            return finalColor;
        }

        return Color.BLACK;
    }

    public Bitmap getImage() {
        return textureView.getBitmap();
    }
}
