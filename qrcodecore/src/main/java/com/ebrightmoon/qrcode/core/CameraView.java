package com.ebrightmoon.qrcode.core;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import cn.bingoogolapple.qrcode.core.R;

/**
 * Time: 2020/6/3
 * Author:wyy
 * Description:
 */
public class CameraView extends RelativeLayout implements Camera.PreviewCallback {
    private static final int NO_CAMERA_ID = -1;
    protected Camera mCamera;
    protected CameraPreview mCameraPreview;
    protected int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    public CameraView(Context context) {
        super(context);
        init(context, null);
    }


    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mCameraPreview = new CameraPreview(context);
        mCameraPreview.setDelegate(new CameraPreview.Delegate() {
            @Override
            public void onStartPreview() {

            }
        });
        mCameraPreview.setId(R.id.qrcode_camera_preview);
        addView(mCameraPreview);

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }

    /**
     * 打开后置摄像头开始预览，但是并未开始识别
     */
    public void startCamera() {
        startCamera(mCameraId);
    }
    /**
     * 打开指定摄像头开始预览，但是并未开始识别
     */
    public void startCamera(int cameraFacing) {
        if (mCamera != null || Camera.getNumberOfCameras() == 0) {
            return;
        }
        int ultimateCameraId = findCameraIdByFacing(cameraFacing);
        if (ultimateCameraId != NO_CAMERA_ID) {
            startCameraById(ultimateCameraId);
            return;
        }

        if (cameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            ultimateCameraId = findCameraIdByFacing(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } else if (cameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            ultimateCameraId = findCameraIdByFacing(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        if (ultimateCameraId != NO_CAMERA_ID) {
            startCameraById(ultimateCameraId);
        }
    }

    private int findCameraIdByFacing(int cameraFacing) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
            try {
                Camera.getCameraInfo(cameraId, cameraInfo);
                if (cameraInfo.facing == cameraFacing) {
                    return cameraId;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return NO_CAMERA_ID;
    }

    private void startCameraById(int cameraId) {
        try {
            mCameraId = cameraId;
            mCamera = Camera.open(cameraId);
            mCameraPreview.setCamera(mCamera);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    /**
     * 打开闪光灯
     */
    public void openFlashlight() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mCameraPreview.openFlashlight();
            }
        }, mCameraPreview.isPreviewing() ? 0 : 500);
    }

    /**
     * 关闭闪光灯
     */
    public void closeFlashlight() {
        mCameraPreview.closeFlashlight();
    }

    /**
     * 销毁二维码扫描控件
     */
    public void onDestroy() {
        stopCamera();
    }

    /**
     * 关闭摄像头预览，并且隐藏扫描框
     */
    private void stopCamera() {
        try {
            if (mCamera != null) {
                mCameraPreview.stopCameraPreview();
                mCameraPreview.setCamera(null);
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void takePicture(Camera.PictureCallback pictureCallback) {
        mCamera.takePicture(null, null,pictureCallback);
    }
}
