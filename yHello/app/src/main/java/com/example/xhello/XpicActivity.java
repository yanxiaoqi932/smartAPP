package com.example.xhello;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationAnalyzer;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationSetting;
import com.lidroid.xutils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import utils.Constant;

/**
 *
 */

public class XpicActivity extends Activity {
    private static final int CAMERA_PERMISSION_CODE = 1;
    private MLImageSegmentationAnalyzer analyzer;

    private static final String TAG = "XpicActivity";

    private static final int PERMISSION_REQUESTS = 1;

    private static final int WHITE = 0;

    private static final int BLUE = 1;

    private int backgroundColor = WHITE;
    @Override
    public void onCreate(Bundle savedInstanceState){
        // 检查应用是否有相机权限。
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            // 应用拥有相机权限。
//        }else {
//            // 申请相机权限。
//            requestCameraPermission();
//        }
//        checkPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x_pic);
        ViewUtils.inject(this);
    }
//    private void requestCameraPermission() {
//        final String[] permissions = new String[]{Manifest.permission.CAMERA};
//        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)) {
//            ActivityCompat.requestPermissions(this, permissions, CAMERA_PERMISSION_CODE);
//            return;
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode != CAMERA_PERMISSION_CODE) {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            return;
//        }
//        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//    }
//
//    public final class Manifest{
//        public final class permission{
//            public static final String CAMERA = "android.permission.CAMERA";
//            public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
//            public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
//        }
//    }
//
//    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//    List<String> mPermissionList = new ArrayList<>();
//
//    // private ImageView welcomeImg = null;
//    private static final int PERMISSION_REQUEST = 1;
//// 检查权限
//
//    private void checkPermission() {
//        mPermissionList.clear();
//        //判断哪些权限未授予
//        for (int i = 0; i < permissions.length; i++) {
//            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
//                mPermissionList.add(permissions[i]);
//            }
//        }
//        /**
//         * 判断是否为空
//         */
//        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
//        } else {//请求权限方法
//            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
//            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST);
//        }
//    }
    /**
     * 响应授权
     * 这里不管用户是否拒绝，都进入首页，不再重复申请权限
     */
//    public void onRequestPermissionsResult
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSION_REQUEST:
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//                break;
//        }
//    }


    public void whiteBackground(View view) {
        this.backgroundColor = WHITE;
        this.startImageSegmentation();
    }

    public void blueBackground(View view) {
        this.backgroundColor = BLUE;
        this.startImageSegmentation();
    }

    private void startImageSegmentation() {
        Intent intent = new Intent(this, StillCutPhotoActivity.class);
        intent.putExtra(Constant.VALUE_KEY, this.backgroundColor);
        startActivity(intent);
    }


}
