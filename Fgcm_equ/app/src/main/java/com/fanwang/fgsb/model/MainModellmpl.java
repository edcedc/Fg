package com.fanwang.fgsb.model;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgsb.bean.BaseResponseBean;
import com.fanwang.fgsb.bean.DataBean;
import com.fanwang.fgsb.callback.Code;
import com.fanwang.fgsb.controller.CloudApi;
import com.fanwang.fgsb.presenter.listener.OnMainListener;
import com.fanwang.fgsb.utils.cache.SharedAccount;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/6/20.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class MainModellmpl implements MainModel {

    @Override
    public void eqpListEqpAdv(String macAddress, final OnMainListener listener) {
        CloudApi.userMobileDevices(macAddress)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

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
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS) {
                            List<DataBean> data = baseResponseBeanResponse.body().data;
                            if (data != null && data.size() != 0) {
                                listener.onSuccess(data);
                            }
                        } else {
                            ToastUtils.showShort(baseResponseBeanResponse.body().desc);
                        }
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

}
