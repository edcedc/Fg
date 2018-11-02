package com.fanwang.fgcm.model;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.presenter.listener.OnRechargeListener;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/5/30.
 */

public class RechargeModellmpl implements RechargeModel{

    @Override
    public void ajaxListInformation(final OnRechargeListener listener) {
        CloudApi.advSystemListInformation()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<List<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onAddDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<List<DataBean>>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            List<DataBean> data = baseResponseBeanResponse.body().data;
                            if (data != null && data.size() != 0){
                                listener.onListInformationSuccess(data);
                            }
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
    public void ajaxPay(int mPay, final String price, String url, final OnRechargeListener listener) {
        CloudApi.pay(url, Integer.valueOf(price), mPay)
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
                            listener.onPaySuccess(jsonObject.optString("data"), Double.valueOf(price));
                        }
                        ToastUtils.showShort(jsonObject.optString("desc"));
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
