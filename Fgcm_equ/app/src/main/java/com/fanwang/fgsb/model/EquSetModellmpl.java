package com.fanwang.fgsb.model;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgsb.bean.BaseResponseBean;
import com.fanwang.fgsb.bean.DataBean;
import com.fanwang.fgsb.callback.Code;
import com.fanwang.fgsb.controller.CloudApi;
import com.fanwang.fgsb.presenter.listener.OnEquSetListener;
import com.fanwang.fgsb.utils.cache.SharedAccount;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/7/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class EquSetModellmpl implements EquSetModel{
    @Override
    public void regionListProvince(final OnEquSetListener listener) {
        CloudApi.regionListProvince()
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
                                List<DataBean> listBean = new ArrayList<>();
                                for (int i = 0;i < data.size();i++){
                                    DataBean bean = data.get(i);
                                    listBean.add(bean);
                                }
                                listener.onSuccessProvince(listBean);
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
    public void regionListLevel(String provinceId, final int provinceType, final OnEquSetListener listener) {
        CloudApi.regionListLevel(provinceId)
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
                                List<DataBean> listBean = new ArrayList<>();
                                for (int i = 0;i < data.size();i++){
                                    DataBean bean = data.get(i);
                                    listBean.add(bean);
                                }
                                if (provinceType == 0){
                                    listener.onSuccessCity(listBean);
                                }else {
                                    listener.onSuccessArea(listBean);
                                }
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
    public void eqpListEqpAdv(String name, final String id, String province, String city, String area, String address, String lat, String lot, String modelLabelid, final OnEquSetListener listener) {
        CloudApi.eqpListEqpAdv(name, id, province, city, area, address, lat, lot, modelLabelid)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onAddDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            SharedAccount.getInstance(Utils.getApp()).saveMobile(id);
                            listener.onSuccess();
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
    public void labelListModelLabelId(final OnEquSetListener listener) {
        CloudApi.labelListModelLabelId()
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
                                List<DataBean> listBean = new ArrayList<>();
                                for (int i = 0;i < data.size();i++){
                                    DataBean bean = data.get(i);
                                    listBean.add(bean);
                                }
                                listener.onSuccessLabel(listBean);
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
}
