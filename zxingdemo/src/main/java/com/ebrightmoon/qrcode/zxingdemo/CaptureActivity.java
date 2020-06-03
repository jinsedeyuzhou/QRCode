package com.ebrightmoon.qrcode.zxingdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import cn.bingoogolapple.qrcode.zxingdemo.R;

/**
 * Time: 2020/6/2
 * Author:wyy
 * Description:
 */
public class CaptureActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);
        CaptureFragment captureFragment= new CaptureFragment();
        TakePhotoFragment takePhotoFragment= new TakePhotoFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, takePhotoFragment).commit();

    }
}
