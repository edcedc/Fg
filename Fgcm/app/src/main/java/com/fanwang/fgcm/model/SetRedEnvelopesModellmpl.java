package com.fanwang.fgcm.model;

import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.presenter.listener.OnSetRedEnvelopesListener;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/4/23.
 */

public class SetRedEnvelopesModellmpl implements SetRedEnvelopesModel{

    @Override
    public void ajaxVideoSave(Context act,String url, int red_envelopes_state, String redEnvelopesNumber, String redEnvelopes, String imgurl, final int payType, String videoUrl, String videoDesc, final OnSetRedEnvelopesListener listener) {
        CloudApi.videoSave(url, videoUrl, imgurl,
                red_envelopes_state, Integer.valueOf(redEnvelopesNumber), Double.valueOf(redEnvelopes), payType, videoDesc, act, new CloudApi.OnVideoSaveListener() {
                    @Override
                    public void addDisposable(Disposable d) {
//                        listener.showLoading();
                    }

                    @Override
                    public void hideLoading() {
                        listener.hideLoading();
                    }

                    @Override
                    public void showLoading() {
//                        listener.showLoading();
                    }

                    @Override
                    public void onComplete() {
//                        listener.onSuccess(data, payType);
                        listener.hideLoading();
                    }

                    @Override
                    public void onComplete2(String data) {
                        listener.onSuccess(data, payType);
                    }
                });
        /*CloudApi.videoSave(act, videoUrl, imgurl,
                red_envelopes_state, Integer.valueOf(redEnvelopesNumber), Double.valueOf(redEnvelopes), payType, videoDesc,
                new CloudApi.OnVideoSaveListener() {
                    @Override
                    public void accept() {

                    }

                    @Override
                    public void showLoading() {
                        listener.showLoading();
                    }

                    @Override
                    public void onComplete() {
                        listener.hideLoading();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onAddDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject beanResponse) {
                        if (beanResponse.optInt("code") == Code.CODE_SUCCESS){
                            String data = beanResponse.optString("data");

                            listener.onSuccess(data, payType);
                        }
                        ToastUtils.showShort(beanResponse.optString("desc"));
                    }
                });*/
    }

    @Override
    public void signatureGet(final int position, final OnSetRedEnvelopesListener listener) {
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
//                            listener.onSignatureGetSucces(red_envelopes_state, redEnvelopesNumber, redEnvelopes, imgurl, position, videoUrl, videoDesc, jsonObject.optString("data"));
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

    @Override
    public void signatureGet(Context act, String url,final int red_envelopes_state, final String redEnvelopesNumber,
                             final String redEnvelopes, final String imgurl,
                             final int payType, final String videoUrl,
                             final String videoDesc, final OnSetRedEnvelopesListener listener) {
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
                            listener.onSignatureGetSucces(red_envelopes_state, redEnvelopesNumber, redEnvelopes, imgurl,
                                    payType, videoUrl, videoDesc, jsonObject.optString("data"));
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

    @Override
    public void TxVideo(String video, String imgUrl) {

    }
}
