package com.fanwang.fgcm.model;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.presenter.listener.OnLoginListener;
import com.fanwang.fgcm.utils.SharedAccount;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/4/9.
 */

public class LoginModelImpl implements LoginModel{

    @Override
    public void code(String phoen, final OnLoginListener listener) {
        CloudApi.userGetRegisterCode(phoen)
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
                                SharedAccount.getInstance(Utils.getApp()).save(phone, pwd);
                                SharedAccount.getInstance(Utils.getApp()).saveSessionId(obj.optString("sessionId"));
                                ToastUtils.showShort(data.optString("desc"));
                            }else {
                                ToastUtils.showShort(jsonObject.optString("desc"));
                            }
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
    public void registerLogin(final String phone, String code, final String pwd, String pwd2, final OnLoginListener listener) {
        CloudApi.userRegister(phone, code, pwd, pwd2)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        listener.showLoading();
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
                            listener.onRegisterLogin(phone, pwd);
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
    public void wxLogin(final String wx_openid, String wx_access_token, final OnLoginListener listener) {
        CloudApi.wxLogin(wx_openid, wx_access_token)
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
                        CloudApi.userWeChatId(jsonObject.optString("headimgurl"), jsonObject.optString("nickname"),
                                jsonObject.optString("openid"), jsonObject.optInt("sex"))
                                .doOnSubscribe(new Consumer<Disposable>() {
                                    @Override
                                    public void accept(Disposable disposable) throws Exception {

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
                                            JSONObject data = jsonObject.optJSONObject("data");
                                            if (data.optInt("code") == Code.CODE_SUCCESS){
                                                JSONObject obj = data.optJSONObject("data");
                                                if (obj != null){
                                                    String sessionId = obj.optString("sessionId");
                                                    String username = obj.optString("username");
                                                    if (StringUtils.isEmpty(username)){
                                                        listener.onWXRegisterSuccess(sessionId);
                                                        ToastUtils.showShort(jsonObject.optString("desc"));
                                                    }else {
                                                        listener.onLoginSuccess();
                                                    }
                                                    User.getInstance().setLogin(true);
                                                    User.getInstance().setUserObj(obj);
                                                    SharedAccount.getInstance(Utils.getApp()).saveSessionId(sessionId);

                                                    /*JSONObject userExtend = obj.optJSONObject("userExtend");
                                                    if (userExtend != null){
                                                        String sessionId = obj.optString("sessionId");
                                                        JSONObject mobile = userExtend.optJSONObject("mobile");
                                                        if (mobile != null){
                                                            listener.onLoginSuccess();
                                                        }else {
                                                            listener.onWXRegisterSuccess(sessionId);
                                                            ToastUtils.showShort(jsonObject.optString("desc"));
                                                        }

                                                    }*/
                                                }
                                            }
                                            ToastUtils.showShort(jsonObject.optString("desc"));
                                        }else {
                                            ToastUtils.showShort(jsonObject.optString("desc"));
                                        }

                                        /*if (jsonObject.optString("desc").equals("注册成功")){
                                            listener.onWXRegisterSuccess();
                                            ToastUtils.showShort(jsonObject.optString("desc"));
                                        }else if (jsonObject.optString("desc").equals("未绑定手机")){
                                            listener.onWXRegisterSuccess();
                                            ToastUtils.showShort(jsonObject.optString("desc"));
                                        }else {
                                            JSONObject data = jsonObject.optJSONObject("data");
                                            if (data != null && data.optInt("code") == Code.CODE_SUCCESS){
                                                listener.onLoginSuccess();
                                                JSONObject obj = data.optJSONObject("data");
                                                User.getInstance().setLogin(true);
                                                User.getInstance().setUserObj(obj);
                                                SharedAccount.getInstance(Utils.getApp()).saveSessionId(obj.optString("sessionId"));
                                                ToastUtils.showShort(data.optString("desc"));

                                            }else {
                                                ToastUtils.showShort(jsonObject.optString("desc"));
                                            }
                                        }*/
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
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onComplete() {
//                        listener.hideLoading();
                    }
                });
    }

}
