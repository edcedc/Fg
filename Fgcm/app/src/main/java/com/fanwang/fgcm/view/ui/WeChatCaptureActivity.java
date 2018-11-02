package com.fanwang.fgcm.view.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.google.zxing.R;
import com.google.zxing.Result;
import com.google.zxing.client.android.AutoScannerView;
import com.google.zxing.client.android.BaseCaptureActivity;

import org.greenrobot.eventbus.EventBus;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * 模仿微信的扫描界面
 */
public class WeChatCaptureActivity extends BaseCaptureActivity {

    private static final String TAG = WeChatCaptureActivity.class.getSimpleName();

    private SurfaceView surfaceView;
    private AutoScannerView autoScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_capture);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        autoScannerView = (AutoScannerView) findViewById(R.id.autoscanner_view);
        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoScannerView.setCameraManager(cameraManager);
    }

    @Override
    public SurfaceView getSurfaceView() {
        return (surfaceView == null) ? (SurfaceView) findViewById(R.id.preview_view) : surfaceView;
    }

    @Override
    public void dealDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        Log.e(TAG, "dealDecode ~~~~~ " + rawResult.getText() + " " + barcode + " " + scaleFactor);
        playBeepSoundAndVibrate(true, false);
//        ToastUtils.showShort(rawResult.getText());
        LogUtils.e(rawResult.getText());
        EventBusActivityScope.getDefault(this).post(new BundleDataInEvent(BundleDataInEvent.demand_zxing, rawResult.getText()));
        EventBus.getDefault().post(new BundleDataInEvent(BundleDataInEvent.demand_zxing, rawResult.getText()));
        finish();
 //        对此次扫描结果不满意可以调用
//        reScan();
    }
}
