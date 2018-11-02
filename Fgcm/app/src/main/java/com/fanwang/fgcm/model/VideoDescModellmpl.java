package com.fanwang.fgcm.model;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.presenter.listener.OnVideroDetailsListener;
import com.lzy.okgo.model.Response;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/4/20.
 */

public class VideoDescModellmpl  implements VideoDescModel{

    @Override
    public void ajaxDetails(final OnVideroDetailsListener listener) {
        /*CloudApi.commentPageAWC(pagerNumber, new Observer<BaseResponseBean<BaseListBean<DataBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                listener.onAddDisposable(d);
            }

            @Override
            public void onNext(BaseResponseBean<BaseListBean<DataBean>> responseBean) {
                if (responseBean.code == Code.CODE_SUCCESS){
                    BaseListBean<DataBean> data = responseBean.data;
                    if (data != null){
                        List<DataBean> list = data.getList();
                        if (list != null && list.size() != 0){
                            listener.onSuccess(list);
                            listener.onRefreshLayoutMode(data.getTotalRow());
                        }else {
                            listener.hideLoading();
                        }
                    }
                }else {
                    ToastUtils.showShort(responseBean.desc);
                    listener.hideLoading();
                }
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e);
            }

            @Override
            public void onComplete() {

            }
        });*/
    }

    @Override
    public void ajaxSavePraise(String id, final int state, final OnVideroDetailsListener listener) {
        CloudApi.savePraise(state, id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            listener.onFabulousSuccess(state);
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
    public void ajaxVideoAddPlayback(String id, final OnVideroDetailsListener listener) {
        CloudApi.svVideoAddPlayback(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<BaseResponseBean> baseResponseBeanResponse) {
                        listener.onPlaybackSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
