package com.fanwang.fgcm.model;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.bean.BaseListBean;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.base.IOnBaseListListener;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/13.
 */

public class EncloureModellmpl implements ChangyongdeListModel {

    @Override
    public void ajaxList(final int pagerNumber,Object object, final IOnBaseListListener listener) {
        CloudApi.svVideoPageNear(pagerNumber, new Observer<BaseResponseBean<BaseListBean<DataBean>>>() {
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
        });
    }
}
