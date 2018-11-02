package com.fanwang.fgcm.model;

import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.presenter.listener.OnPhoneDetailsListener;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/4/27.
 */

public class PhoneDetailsModellmpl implements PhoneDetailsModel {

    @Override
    public void onSubmit(int mSelectType, String s, List<LocalMedia> localMediaList, String number, String individual, String range,
                         double mLongitude, double mLatitude, String mProvince, String mCity, String mDistrict, int mRange,
                         final int redEnvelopesState, final int payType,
                         Context act,String videoUrl,String url, final OnPhoneDetailsListener listener) {

        CloudApi.advSave(url, act, mSelectType, s, localMediaList, number, individual, range, mLongitude, mLatitude, mProvince, mCity, mDistrict, mRange,redEnvelopesState,payType,videoUrl,  new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {
                listener.onAddDisposable(d);
            }

            @Override
            public void onNext(JSONObject responseBean) {
                if (responseBean.optInt("code") == Code.CODE_SUCCESS){
                    String data = responseBean.optString("data");
                    if (data != null){
                        listener.onSuccess(payType, data);
                    }
                }
                ToastUtils.showShort(responseBean.optString("desc"));
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e);
            }

            @Override
            public void onComplete() {
                listener.hideLoading();
            }
        }, listener);
    }

    @Override
    public void signatureGet(final OnPhoneDetailsListener listener) {
        CloudApi.signatureGet()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onAddDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        if (jsonObject.optInt("code") == Code.CODE_SUCCESS){
                            listener.onSignatureGetSucces(jsonObject.optString("data"));
                        }else {
                            ToastUtils.showShort(jsonObject.optString("desc"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        listener.hideLoading();
                    }
                });
    }

}
