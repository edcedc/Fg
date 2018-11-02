package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FForgetPwdBinding;
import com.fanwang.fgcm.presenter.ForgetPwdPresenter;
import com.fanwang.fgcm.presenter.ForgetPwdPresenterlmpl;
import com.fanwang.fgcm.utils.CountDownTimerUtils;
import com.fanwang.fgcm.view.impl.ForgetPwdView;

/**
 * Created by edison on 2018/4/10.
 *  忘记密码
 */

public class ForgetPwdFrg extends BaseFragment<FForgetPwdBinding> implements ForgetPwdView, View.OnClickListener{

    @Override
    protected void initParms(Bundle bundle) {

    }

    private ForgetPwdPresenter presenter;

    @Override
    protected int bindLayout() {
        return R.layout.f_forget_pwd;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.top_forget_pwd));
        presenter = new ForgetPwdPresenterlmpl(this);
        mB.refreshLayout.setPureScrollModeOn();
        mB.btCode.setOnClickListener(this);
        mB.btLogin.setOnClickListener(this);
        setSwipeBackEnable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_code:
                presenter.code(mB.etPhone.getText().toString());
                break;
            case R.id.bt_login:
                presenter.forget(mB.etPhone.getText().toString(), mB.etCode.getText().toString(), mB.etPwd.getText().toString(), mB.etPwd2.getText().toString());
                break;
        }
    }

    @Override
    public void onLoginSuccess() {
//        pop();
        UIHelper.startMainFrg(this);
    }

    @Override
    public void onForgeSuccess() {
        presenter.login(mB.etPhone.getText().toString(), mB.etPwd.getText().toString());
    }

    @Override
    public void setPhoneError(String string) {
        mB.etPhone.setError(string);
    }

    @Override
    public void setPwdError(String string) {
        mB.etPwd.setError(string);
    }

    @Override
    public void OnCodeSuccess() {
        new CountDownTimerUtils(act, 60000, 1000, mB.btCode).start();
    }

    @Override
    public void setCodeErroe(String s) {
        mB.etCode.setError(s);
    }

    @Override
    public void setPwd2Error(String string) {
        mB.etPwd2.setError(string);
    }

    @Override
    public void onError(Throwable e) {

    }

}
