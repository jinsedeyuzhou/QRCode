package com.ebrightmoon.qrcode.zxingdemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.ebrightmoon.qrcode.core.CameraPreview;
import com.ebrightmoon.qrcode.core.CameraView;

import cn.bingoogolapple.qrcode.zxingdemo.R;

/**
 * Time: 2020/6/2
 * Author:wyy
 * Description:
 * 拍照
 */
public class TakePhotoFragment extends Fragment {

    private CameraView cameraPreview;
    private View view;
    private CheckBox mCbFlash;
    private ImageView mIvPhoto;
    private String filePath;
    private ImageView mIvTake;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_take_photo, null);
        cameraPreview = view.findViewById(R.id.cameraPreview);
        mCbFlash = view.findViewById(R.id.mCbFlash);
        mIvPhoto = view.findViewById(R.id.mIvPhoto);
        mIvTake = view.findViewById(R.id.mIvTake);
        mCbFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCbFlash.isChecked()) {
                    cameraPreview.openFlashlight(); // 打开闪光灯
                } else {
                    cameraPreview.closeFlashlight(); // 打开闪光灯
                }
            }
        });

        mIvTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPreview.takePicture(new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {

                    }
                });
            }
        });

        mIvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "File Chooser"), 1);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK) {
            Uri imageFileUri = data.getData();
            if (imageFileUri == null)
                return;
            try {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(imageFileUri, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    filePath = cursor.getString(columnIndex);
                    cursor.close();
                } else {
                    filePath = imageFileUri.getPath();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        cameraPreview.startCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraPreview.onDestroy();

    }
}
