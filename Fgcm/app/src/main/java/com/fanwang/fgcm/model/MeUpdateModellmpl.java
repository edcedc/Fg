package com.fanwang.fgcm.model;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.presenter.listener.OnMeUpdateListener;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/6/1.
 */

public class MeUpdateModellmpl implements MeUpdateModel{

    @Override
    public void ajaxUserUpdate(final String path, final String nick, final String phone, final int sex, final OnMeUpdateListener listener) {
        CloudApi.userupdate(path, nick, phone, sex)
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
                            String data = jsonObject.optString("data");
                            if (data != null){
                                JSONObject information = User.getInstance().getUserInformation();
                                JSONObject userObj = User.getInstance().getUserObj();
                                JSONObject userExtend = userObj.optJSONObject("userExtend");
                                if (!StringUtils.isEmpty(path) && !StringUtils.isEmpty(information.toString())){
                                    try {
                                        information.put("head", jsonObject.optString("data"));
                                        userExtend.put("head", jsonObject.optString("data"));
                                        listener.onUpdateHead(jsonObject.optString("data"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (!StringUtils.isEmpty(nick) && !StringUtils.isEmpty(information.toString())){
                                    try {
                                        information.put("nick", nick);
                                        userExtend.put("nick", nick);
                                        listener.onUpdateData();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(information.toString())){
                                    try {
                                        information.put("username", phone);
                                        userExtend.put("mobile", phone);
                                        listener.onUpdateData();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (sex != 0 && !StringUtils.isEmpty(information.toString())){
                                    try {
                                        information.put("sex", sex);
                                        userExtend.put("sex", sex);
                                        listener.onUpdateData();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        ToastUtils.showShort(jsonObject.optString("desc"));
                    }

                    @Override
                    public void onError(Throwable e) {
//                        listener.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        listener.hideLoading();
                    }
                });
    }

}
