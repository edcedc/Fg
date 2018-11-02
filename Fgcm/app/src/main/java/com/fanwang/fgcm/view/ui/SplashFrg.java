package com.fanwang.fgcm.view.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FSplashBinding;
import com.fanwang.fgcm.utils.GlideImageUtils;
import com.fanwang.fgcm.utils.SharedAccount;
import com.fanwang.fgcm.weight.RuntimeRationale;
import com.fanwang.fgcm.weight.ShareLoginOK;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/6/15.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class SplashFrg extends BaseFragment<FSplashBinding> implements BGABanner.Delegate, BGABanner.Adapter<ImageView, String> {

    public static SplashFrg newInstance() {
        Bundle args = new Bundle();
        SplashFrg fragment = new SplashFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private List<String> listImage = new ArrayList<>();
    private List<String> tips = new ArrayList<String>();

    private final int mHandle_splash = 0;
    private final int mHandle_permission = 1;

    private Activity act;

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_splash;
    }

    @Override
    protected void initView(View view) {
        act = getActivity();


        mB.banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (listImage.size() == 1){
                    handler.sendMessageDelayed(handler.obtainMessage(mHandle_splash), 1500);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == listImage.size() - 1){
                    handler.sendMessageDelayed(handler.obtainMessage(mHandle_splash), 1500);
                }else {
                    handler.removeMessages(mHandle_splash);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        handler.sendEmptyMessageDelayed(mHandle_permission, 1000);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case mHandle_splash:
                    startLog();
                    break;
                case mHandle_permission:
                    setHasPermission();
                    break;
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    /**
     *  设置权限
     */
    private void setHasPermission() {
        AndPermission.with(SplashFrg.this)
                .runtime()
                .permission(
//                        CONTACTS
//                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE//写入外部存储, 允许程序写入外部存储，如SD卡
                        , Manifest.permission.CAMERA//拍照权限, 允许访问摄像头进行拍照
                        , Manifest.permission.ACCESS_COARSE_LOCATION//获取错略位置, 通过WiFi或移动基站的方式获取用户错略的经纬度信息，定位精度大概误差在30~1500米
//                        , Manifest.permission.RECORD_AUDIO //录制视频
                )
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        setPermissionOk();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(SplashFrg.this, permissions)) {
                            showSettingDialog(act, permissions);
                        }else {
                            setPermissionCancel();
                        }
                    }
                })
                .start();
    }


    /**
     * Display setting dialog.
     */
    public void showSettingDialog(Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermissionCancel();
                    }
                })
                .show();
    }

    /**
     * Set permissions.
     */
    private void setPermission() {
        AndPermission.with(this)
                .runtime()
                .setting()
                .onComeback(new Setting.Action() {
                    @Override
                    public void onAction() {
                        setHasPermission();
//                        ToastUtils.showShort("用户从设置页面返回。");
                    }
                })
                .start();
    }


    /**
     * 权限有任何一个失败都会走的方法
     */
    private void setPermissionCancel() {
        act.finish();
    }

    private boolean isBindingWX = false;
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (isBindingWX){
            act.finish();
        }
    }

    /**
     *  权限都成功
     */
    private void setPermissionOk(){
        if (ShareLoginOK.getInstance(act).getLoginOk()){
            final String sessionId = SharedAccount.getInstance(Utils.getApp()).getSessionId();
            if (!StringUtils.isEmpty(sessionId)){
                CloudApi.userInformation()
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<JSONObject>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);
                            }

                            @Override
                            public void onNext(JSONObject jsonObject) {
                                if (jsonObject.optInt("code") == Code.CODE_SUCCESS){
                                    JSONObject data = jsonObject.optJSONObject("data");
                                    if (data != null){
                                        User.getInstance().setLogin(true);
                                        User.getInstance().setUserObj(data);
                                        String username = data.optString("username");
                                        if (StringUtils.isEmpty(username)){
                                            isBindingWX = true;
                                            UIHelper.startBindingWXFrg(SplashFrg.this, sessionId);
                                        }else {
                                            UIHelper.startMainAct();
                                            act.finish();
                                        }

                                        /*JSONObject userExtend = data.optJSONObject("userExtend");
                                        if (userExtend != null){
                                            User.getInstance().setLogin(true);
                                            User.getInstance().setUserObj(data);
//                                            SharedAccount.getInstance(Utils.getApp()).saveSessionId(data.optString("sessionId"));
                                            String mobile = userExtend.optString("mobile");
                                            if (!StringUtils.isEmpty(mobile)){
                                                UIHelper.startMainAct();
                                                act.finish();
                                            }else {
                                                isBindingWX = true;
                                                UIHelper.startBindingWXFrg(SplashFrg.this, sessionId);
                                            }
//                                            User.getInstance().setLogin(true);
//                                            User.getInstance().setUserObj(data);
//                                            User.getInstance().setUserInformation(data);
//                                            UIHelper.startMainAct();
//                                            act.finish();
                                        }*/
                                    }
                                }else {
                                    startLog();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                startLog();
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
            }else {
                startLog();
            }

        }else {
            CloudApi.guidanceList()
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<BaseResponseBean<List<DataBean>>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }

                        @Override
                        public void onNext(Response<BaseResponseBean<List<DataBean>>> baseResponseBeanResponse) {
                            if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                List<DataBean> data = baseResponseBeanResponse.body().data;
                                if (data != null && data.size() != 0){
                                    for (int i = 0;i < data.size(); i++){
                                        DataBean bean = data.get(i);
                                        listImage.add(bean.getImage());
                                    }
                                    mB.banner.setData(listImage, tips);
                                    mB.banner.setAdapter(SplashFrg.this);
                                }else {
                                    startLog();
                                }
                            }else {
                                startLog();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.e(e.getMessage());
                            startLog();
//                            listImage.add(R.mipmap.f03);
//                            listImage.add(R.mipmap.f04);
//                            listImage.add(R.mipmap.f05);
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }

    private void startLog(){
        UIHelper.startLoginFrg(SplashFrg.this);
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
        GlideImageUtils.load(act, CloudApi.SERVLET_URL + model, itemView);
    }

}
