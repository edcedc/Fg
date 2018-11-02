package com.fanwang.fgsb.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgsb.bean.BaseResponseBean;
import com.fanwang.fgsb.bean.DataBean;
import com.fanwang.fgsb.callback.Code;
import com.fanwang.fgsb.controller.CloudApi;
import com.fanwang.fgsb.model.MainModel;
import com.fanwang.fgsb.model.MainModellmpl;
import com.fanwang.fgsb.presenter.listener.OnMainListener;
import com.fanwang.fgsb.view.impl.MainView;
import com.lzy.okgo.model.Response;

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

public class MainPresenterlmpl implements MainPresenter, OnMainListener {

    private MainView view;
    private MainModel model;

    public MainPresenterlmpl(MainView view){
        this.view = view;
        this.model = new MainModellmpl();
    }

    @Override
    public void eqpListEqpAdv(String macAddress) {
        if (StringUtils.isEmpty(macAddress))return;
        model.eqpListEqpAdv(macAddress, this);
    }

    @Override
    public void downloadCodeGet() {
        CloudApi.downloadCodeGet()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        view.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            String desc = baseResponseBeanResponse.body().desc;
                            view.showDownload(CloudApi.SERVLET_URL + desc);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void showLoading() {
        view.showLoading();
    }

    @Override
    public void hideLoading() {
        view.hideLoading();
    }

    @Override
    public void onError(Throwable e) {
        view.onError(e);
    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onSuccess(Object object) {
        view.setData(object);
    }
}
