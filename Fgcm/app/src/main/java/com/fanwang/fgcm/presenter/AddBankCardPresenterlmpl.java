package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.model.AddBankCardModel;
import com.fanwang.fgcm.model.AddBankCardModellmpl;
import com.fanwang.fgcm.presenter.listener.OnAddBankCardListener;
import com.fanwang.fgcm.view.impl.AddBankCardView;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/5/7.
 */

public class AddBankCardPresenterlmpl implements AddBankCardPresenter, OnAddBankCardListener {

    private AddBankCardView view;
    private AddBankCardModel model;

    public AddBankCardPresenterlmpl(AddBankCardView view){
        this.view = view;
        model = new AddBankCardModellmpl();
    }

    @Override
    public void openingBank(String s, String s1) {
        if (StringUtils.isEmpty(s) || StringUtils.isEmpty(s1)){
            return;
        }
        view.openingBank();
    }

    @Override
    public void submit(String s, String s1, String s2, boolean cbSelected) {
        if (StringUtils.isEmpty(s) || StringUtils.isEmpty(s1) || StringUtils.isEmpty(s2) || !cbSelected){
            return;
        }
        model.onSuccess(s, s1, s2, this);
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
        ToastUtils.showShort(e.getMessage());
        view.hideLoading();
    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onSuccess(DataBean data) {
        view.onSuccess(data);
    }

}
