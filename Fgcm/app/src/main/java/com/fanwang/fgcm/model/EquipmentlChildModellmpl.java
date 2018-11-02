package com.fanwang.fgcm.model;

import com.fanwang.fgcm.base.IOnBaseListListener;
import com.fanwang.fgcm.bean.BaseListBean;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.lzy.okgo.model.Response;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/5/30.
 */

public class EquipmentlChildModellmpl implements EquipmentlChildModel{

    @Override
    public void ajaxPageUserEepADV(int pagerNumber, int mPosition, String url, final IOnBaseListListener listener) {
        CloudApi.pageUserEepADV(pagerNumber, mPosition, url)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<BaseListBean<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onAddDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<BaseListBean<DataBean>>> beanResponse) {
                        if (beanResponse.body().code == Code.CODE_SUCCESS){
                            BaseListBean<DataBean> data = beanResponse.body().data;
                            if (data != null){
                                List<DataBean> list = data.getList();
                                if (list != null && list.size() != 0){
                                    listener.onSuccess(list);
                                    listener.onRefreshLayoutMode(data.getTotalRow());
                                }
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

    @Override
    public void ajaxPageUserEepADV(int pagerNumber, int mPosition, String url, String equId, final IOnBaseListListener listener) {
        CloudApi.eqpPageInvestmentADV(pagerNumber, mPosition, url, equId)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<BaseListBean<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onAddDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<BaseListBean<DataBean>>> beanResponse) {
                        if (beanResponse.body().code == Code.CODE_SUCCESS){
                            BaseListBean<DataBean> data = beanResponse.body().data;
                            if (data != null){
                                List<DataBean> list = data.getList();
                                if (list != null && list.size() != 0){
                                    listener.onSuccess(list);
                                    listener.onRefreshLayoutMode(data.getTotalRow());
                                }
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
