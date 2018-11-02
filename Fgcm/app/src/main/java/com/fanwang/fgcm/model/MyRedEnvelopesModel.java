package com.fanwang.fgcm.model;

import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.base.IOnBaseListListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2018/4/13.
 */

public class MyRedEnvelopesModel implements MyRedEnvelopesModellmpl {

    @Override
    public void ajaxList(int pagerNumber, IOnBaseListListener listener) {

        List<DataBean> listBean = new ArrayList<>();
        for (int i = 0;i < 5;i++){
            listBean.add(new DataBean());
        }
        listener.onSuccess(listBean);

        listener.onRefreshLayoutMode(4);

        /*OkGo.<BaseResponseBean>post("http://10.0.0.38:8080/java_base_project/api/user")
                .params("mobile", "18812345672")
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableBody<BaseResponseBean>())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
//                        listener.showLoading();
                    }
                })
                .map(new Function<BaseResponseBean, BaseResponseBean>() {
                    @Override
                    public BaseResponseBean apply(BaseResponseBean baseResponseBean) throws Exception {
                        return baseResponseBean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onAddDisposable(d);
                    }

                    @Override
                    public void onNext(BaseResponseBean bean) {
                        if (bean.code == Code.CODE_SUCCESS){

                            listener.onListSuccess();
                        }else {
                            ToastUtils.showShort(bean.desc);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage().toString());
                        listener.onError();
                    }

                    @Override
                    public void onComplete() {
//                        listener.hideLoading();
                    }
                });*/
    }

}
