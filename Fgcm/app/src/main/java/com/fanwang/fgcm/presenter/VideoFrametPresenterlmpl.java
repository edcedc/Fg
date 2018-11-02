package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.view.impl.VideoFrametView;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/8/1.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class VideoFrametPresenterlmpl implements VideoFrametPresenter {

    private VideoFrametView view;

    public VideoFrametPresenterlmpl(VideoFrametView view) {
        this.view = view;
    }

    @Override
    public void signatureGet() {
        CloudApi.signatureGet()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        view.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        view.addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        if (jsonObject.optInt("code") == Code.CODE_SUCCESS){
                            view.onSignatureGetSucces(jsonObject.optString("data"));
                        }else {
                            ToastUtils.showShort(jsonObject.optString("desc"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });
    }
}
