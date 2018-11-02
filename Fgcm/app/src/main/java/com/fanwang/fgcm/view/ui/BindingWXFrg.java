package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FBindingWxBinding;
import com.fanwang.fgcm.presenter.BindingPresenter;
import com.fanwang.fgcm.presenter.BindingPresenterlmpl;
import com.fanwang.fgcm.utils.CountDownTimerUtils;
import com.fanwang.fgcm.view.impl.BindingWXView;

/**
 * Created by edison on 2018/4/12.
 *  绑定微信
 */

public class BindingWXFrg extends BaseFragment<FBindingWxBinding> implements View.OnClickListener, BindingWXView {

    private BindingPresenter presenter;
    private String wx_openid;

    @Override
    protected void initParms(Bundle bundle) {
        wx_openid = bundle.getString("wx_openid");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_binding_wx;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.top_binding_wx));
        presenter = new BindingPresenterlmpl(this);
        mB.btCode.setOnClickListener(this);
        mB.btLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_code:
                presenter.code(mB.etPhone.getText().toString());
                break;
            case R.id.bt_login:
                presenter.login(mB.etPhone.getText().toString(), mB.etCode.getText().toString(), wx_openid);
                break;
        }
    }

    @Override
    public void setPhoneError(String string) {
        mB.etPhone.setError(string);
    }

    @Override
    public void setCodeError(String string) {
        mB.etCode.setError(string);
    }

    @Override
    public void onCodeSuccess() {
        new CountDownTimerUtils(act, 60000, 1000, mB.btCode).start();
    }

    @Override
    public void onLoginSuccess() {
        UIHelper.startMainAct();
        ActivityUtils.finishAllActivities();
    }

    @Override
    public boolean cbSelected() {
        return mB.cbSelected.isChecked();
    }

    public void onError(Throwable e) {

    }
}
