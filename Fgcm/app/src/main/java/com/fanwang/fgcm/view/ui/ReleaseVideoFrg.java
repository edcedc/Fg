package com.fanwang.fgcm.view.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.tencent.ugc.TXRecordCommon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2018/4/16.
 *  发布视频
 */

public class ReleaseVideoFrg extends BaseFragment{


    private int mAspectRatio; // 视频比例
    private int mRecordResolution = TXRecordCommon.VIDEO_RESOLUTION_720_1280;; // 录制分辨率
    private int mBiteRate = 9600; // 码率
    private int mFps = 20; // 帧率
    private int mGop = 3; // 关键帧间隔
    public static final String RECORD_CONFIG_HOME_ORIENTATION = "record_config_home_orientation";

    @Override
    protected int bindLayout() {
        return R.layout.f_release_video;
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected void initView(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(act, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(act, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(act, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(act,permissions.toArray(new String[0]),100);
               pop();
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                break;
            default:
                break;
        }
    }
}
