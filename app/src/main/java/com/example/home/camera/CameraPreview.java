package com.example.home.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by robertfernandes on 11/9/2016.
 */

public class CameraPreview extends TextureView {

    private static final String TAG = "CameraPreview";
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private String cameraId;
    private Size imageDimension;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession cameraCaptureSession;

    private MainActivity mainActivity;

    private Handler backgroundHandler;
    private HandlerThread backgroundThread;

    public CameraPreview(Context context) {
        super(context);
        init(context);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mainActivity = (MainActivity)context;
    }

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {

        }

        @Override
        public void onError(CameraDevice camera, int error) {
        }
    };

    public void openCamera() {
        CameraManager manager = (CameraManager) mainActivity.getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                return;
            }

            manager.openCamera(cameraId, stateCallback, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera");
    }

    public void createCameraPreview() {
        try {
            SurfaceTexture texture = getSurfaceTexture();
            assert texture != null;

            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());

            Surface surface = new Surface(texture);

            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.set(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, CameraMetadata.CONTROL_VIDEO_STABILIZATION_MODE_ON);
            captureRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession session) {
                    if (cameraDevice == null) {
                        return;
                    }
                    cameraCaptureSession = session;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    Toast.makeText(mainActivity, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePreview() {
        if (cameraDevice == null) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        try {
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, mainActivity.getHandler());
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


}
