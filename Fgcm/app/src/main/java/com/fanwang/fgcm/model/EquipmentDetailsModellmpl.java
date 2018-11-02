package com.fanwang.fgcm.model;

import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.presenter.listener.OnEquipmentDetailsListener;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/4/28.
 */

public class EquipmentDetailsModellmpl implements EquipmentDetailsModel{

    @Override
    public void eqpGetEqpSystemPrice(String id, final OnEquipmentDetailsListener listener) {
        CloudApi.eqpGetEqpSystemPrice(id)
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
                                for (int i = 0;i < data.size(); i++){
                                    DataBean bean = data.get(i);
                                    listBean.add(bean);
                                }
                                listener.onGetEqpSystemPriceSuccess(listBean);
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
    public void eqpSave(Context act, int type, List<LocalMedia> localMediaList, String seconds, String launchTime, String endTime, String eqpId, final String remark, int payType, double latitude, double longitude, boolean isMore,String videoUrl, final OnEquipmentDetailsListener listener) {
        CloudApi.eqpSave(act, type, localMediaList, seconds, launchTime, endTime, eqpId, remark, payType, latitude,
                longitude, isMore,videoUrl, new OnEquipmentDetailsListener() {
                    @Override
                    public void onGetEqpSystemPriceSuccess(Object object) {

                    }

                    @Override
                    public void onSuccessEqpSave(BaseResponseBean responseBean) {
                        if (responseBean.code == Code.CODE_SUCCESS){
                            listener.onSuccessEqpSave(responseBean);
                        }
                        ToastUtils.showShort(responseBean.desc);
                    }

                    @Override
                    public void onSignatureGetSucces(String s) {

                    }

                    @Override
                    public void showLoading() {
                        listener.showLoading();
                    }

                    @Override
                    public void hideLoading() {
                        listener.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onAddDisposable(Disposable d) {
                        listener.onAddDisposable(d);
                    }
                });
    }

    @Override
    public void signatureGet(final OnEquipmentDetailsListener listener) {
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
                            listener.onSignatureGetSucces(jsonObject.optString("data"));
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

}
