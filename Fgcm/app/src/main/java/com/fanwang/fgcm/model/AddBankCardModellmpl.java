package com.fanwang.fgcm.model;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.presenter.listener.OnAddBankCardListener;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/5/7.
 */

public class AddBankCardModellmpl implements AddBankCardModel{

    @Override
    public void onSuccess(String s, String s1, String s2, final OnAddBankCardListener listener) {
        CloudApi.userBankCardAddBankCard(s, s1, s2)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<JSONObject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onAddDisposable(d);
                    }

                    @Override
                    public void onNext(Response<JSONObject> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().optInt("code") == Code.CODE_SUCCESS){
                            JSONObject data = baseResponseBeanResponse.body().optJSONObject("data");
                            Gson gson = new Gson();
                            String json = gson.toJson(data);
                            DataBean javaBean = gson.fromJson(json, DataBean.class);
//                            DataBean data = baseResponseBeanResponse.body().data;
                            listener.onSuccess(javaBean);
                        }
                        ToastUtils.showShort(baseResponseBeanResponse.body().optString("desc"));
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
//                        ToastUtils.showShort("银行卡账号不对");
                    }

                    @Override
                    public void onComplete() {
                        listener.hideLoading();
                    }
                });
    }

}
