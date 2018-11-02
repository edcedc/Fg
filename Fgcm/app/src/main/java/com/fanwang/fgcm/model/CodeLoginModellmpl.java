package com.fanwang.fgcm.model;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.presenter.listener.OnCodeLoginListener;
import com.fanwang.fgcm.utils.SharedAccount;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/4/12.
 */

public class CodeLoginModellmpl implements CodeLoginModel {

    @Override
    public void code(String phone, final OnCodeLoginListener listener) {
        CloudApi.userGetCodeLogin(phone)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onAddDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            listener.onCodeSuccess();
                        }
                        ToastUtils.showShort(baseResponseBeanResponse.body().desc);
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
    public void login(final String phone, String code, final OnCodeLoginListener listener) {
        CloudApi.userCodeLogin(phone, code)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onAddDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        JSONObject data = jsonObject.optJSONObject("data");
                        if (data != null && data.optInt("code") == Code.CODE_SUCCESS){
                            JSONObject obj = data.optJSONObject("data");
                            User.getInstance().setLogin(true);
                            User.getInstance().setUserObj(obj);
                            SharedAccount.getInstance(Utils.getApp()).saveSessionId(obj.optString("sessionId"));
                            SharedAccount.getInstance(Utils.getApp()).save(phone, phone.substring(phone.length() - 6, phone.length()));
                            listener.onLoginSuccess();
                        }
                        ToastUtils.showShort(data.optString("desc"));
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
