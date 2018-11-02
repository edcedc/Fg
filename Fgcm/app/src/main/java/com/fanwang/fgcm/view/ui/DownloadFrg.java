package com.fanwang.fgcm.view.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.databinding.FDownloadBinding;
import com.fanwang.fgcm.utils.GlideImageUtils;
import com.fanwang.fgcm.utils.ShareTool;
import com.lzy.okgo.model.Response;
import com.umeng.socialize.ShareAction;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/5/8.
 * 下载
 */

public class DownloadFrg extends BaseFragment<FDownloadBinding> {


    private ShareAction shareAction;

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_download;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.download));
        mB.refreshLayout.setPureScrollModeOn();
        JSONObject information = User.getInstance().getUserInformation();
        JSONObject userExtend = information.optJSONObject("userExtend");

        shareAction = ShareTool.getInstance().shareAction(act, CloudApi.registeredExtensionCode + information.optString("extensionCode"));
        mB.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAction.open();
            }
        });

        CloudApi.userGetQrCode()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            DataBean data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                GlideImageUtils.load(act, CloudApi.SERVLET_URL + data.getQr_code(), mB.ivImg);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        shareAction.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ShareTool.getInstance().release(act);
    }

}
