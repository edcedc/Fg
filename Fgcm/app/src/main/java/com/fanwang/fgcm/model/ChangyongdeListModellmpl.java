package com.fanwang.fgcm.model;

 import com.blankj.utilcode.util.ToastUtils;
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
 * Created by edison on 2018/4/13.
 */

public class ChangyongdeListModellmpl implements ChangyongdeListModel{
    @Override
    public void ajaxList(int pagerNumber,Object object, final IOnBaseListListener listener) {
        CloudApi.videoPageWeeks(pagerNumber, (String)object)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
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
                        }else {
                            ToastUtils.showShort(beanResponse.body().desc);

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

        /*CloudApi.videoPageWeeks(pagerNumber,(String)object, new Observer<BaseResponseBean<BaseListBean<DataBean>>>() {
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
                        }
                    }
                }else {
                    ToastUtils.showShort(responseBean.desc);

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
        });*/
    }
}
