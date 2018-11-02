package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.databinding.FReportBinding;
import com.lzy.okgo.model.Response;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/5/8.
 *  投诉举报
 */

public class ComplaintsReportFrg extends BaseFragment<FReportBinding> implements View.OnClickListener{

    private String id;

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_report;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.complaints_report));
        mB.btSubmit.setOnClickListener(this);
        mB.refreshLayout.setPureScrollModeOn();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_submit:
                String s = mB.etText.getText().toString();
                if (StringUtils.isEmpty(s)){
                    return;
                }
                CloudApi.problemSave(s, id)
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoading();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Response<BaseResponseBean>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);
                            }

                            @Override
                            public void onNext(Response<BaseResponseBean> baseResponseBeanResponse) {
                                if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                    pop();
                                }
                                ToastUtils.showShort(baseResponseBeanResponse.body().desc);
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.showShort(e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                hideLoading();
                            }
                        });
                break;
        }
    }
}
