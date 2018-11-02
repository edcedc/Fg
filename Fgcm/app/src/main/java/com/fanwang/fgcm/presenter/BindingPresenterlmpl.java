package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.model.BindingWXModel;
import com.fanwang.fgcm.model.BindingWxlModellmpl;
import com.fanwang.fgcm.presenter.listener.OnBindingWXListener;
import com.fanwang.fgcm.view.impl.BindingWXView;
import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/12.
 */

public class BindingPresenterlmpl implements BindingPresenter, OnBindingWXListener {

    private BindingWXView wxView;
    private BindingWXModel wxModel;

    public BindingPresenterlmpl(BindingWXView view) {
        this.wxView = view;
        this.wxModel = new BindingWxlModellmpl();
    }

    @Override
    public void code(String phone) {
        if (StringUtils.isEmpty(phone)){
            wxView.setPhoneError(Utils.getApp().getResources().getString(R.string.empty_phone));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            wxView.setPhoneError(Utils.getApp().getResources().getString(R.string.mobile_exact_phone));
            return;
        }
        wxModel.code(phone, this);
    }

    @Override
    public void login(String phone, String code, String wx_openid) {
        if (StringUtils.isEmpty(phone)){
            wxView.setPhoneError(Utils.getApp().getResources().getString(R.string.empty_phone));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            wxView.setPhoneError(Utils.getApp().getResources().getString(R.string.mobile_exact_phone));
            return;
        }
        if (StringUtils.isEmpty(code)){
            wxView.setCodeError(Utils.getApp().getResources().getString(R.string.empty_code));
            return;
        }
        if (!wxView.cbSelected()){
            ToastUtils.showShort(Utils.getApp().getResources().getString(R.string.empty_agreement));
            return;
        }
        wxModel.login(phone, code, wx_openid, this);
    }

    @Override
    public void showLoading() {
        wxView.showLoading();
    }

    @Override
    public void hideLoading() {
        wxView.hideLoading();
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onAddDisposable(Disposable d) {
        wxView.addDisposable(d);
    }

    @Override
    public void onCodeSuccess() {
        wxView.onCodeSuccess();
    }

    @Override
    public void onLoginSuccess() {
        wxView.onLoginSuccess();
    }
}
