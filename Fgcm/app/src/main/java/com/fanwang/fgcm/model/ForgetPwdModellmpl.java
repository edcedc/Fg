package com.fanwang.fgcm.model;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.presenter.listener.OnForgetPwdListener;
import com.fanwang.fgcm.presenter.listener.OnLoginListener;
import com.fanwang.fgcm.utils.SharedAccount;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/4/10.
 */

public class ForgetPwdModellmpl implements ForgetPwdModel {

    @Override
    public void code(String phone, final OnForgetPwdListener listener) {
        CloudApi.userGetForgetCode(phone)
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
                        }else {
                            ToastUtils.showShort(baseResponseBeanResponse.body().desc);
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
    public void forget(String phone, String code, String pwd, String pwd2, final OnForgetPwdListener listener) {
        CloudApi.userUpdateForgetPassword(phone,code, pwd, pwd2)
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
                            listener.onForgetSuccess();
                        }else {
                            listener.hideLoading();
                        }
                        ToastUtils.showShort(baseResponseBeanResponse.body().desc);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void login(final String phone, final String pwd, final OnLoginListener listener) {
        CloudApi.userLogin(phone, pwd)
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
                        if (jsonObject.optInt("code") == Code.CODE_SUCCESS){
                            JSONObject data = jsonObject.optJSONObject("data");
                            if (data != null && data.optInt("code") == Code.CODE_SUCCESS){
                                listener.onLoginSuccess();
                                JSONObject obj = data.optJSONObject("data");
                                User.getInstance().setLogin(true);
                                User.getInstance().setUserObj(obj);
                                SharedAccount.getInstance(Utils.getApp()).saveSessionId(obj.optString("sessionId"));
                                SharedAccount.getInstance(Utils.getApp()).save(phone, pwd);
                                ToastUtils.showShort(data.optString("desc"));
                            }else {
                                ToastUtils.showShort(jsonObject.optString("desc"));
                            }
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
