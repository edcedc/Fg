package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FCodeLoginBinding;
import com.fanwang.fgcm.presenter.CodeLoginPresenter;
import com.fanwang.fgcm.presenter.CoedLoginPresenterlmpl;
import com.fanwang.fgcm.utils.CountDownTimerUtils;
import com.fanwang.fgcm.view.impl.CodeLoginView;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/12.
 *  验证码登陆
 */

public class CodeLoginFrg extends BaseFragment<FCodeLoginBinding> implements CodeLoginView, View.OnClickListener{

    @Override
    protected void initParms(Bundle bundle) {

    }

    private CodeLoginPresenter presenterl;

    @Override
    protected int bindLayout() {
        return R.layout.f_code_login;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.top_code_login));
        mB.btCode.setOnClickListener(this);
        mB.btLogin.setOnClickListener(this);
        presenterl = new CoedLoginPresenterlmpl(this);

        String content = "<font color=\"#773FE3\">" + "《用户注册协议》" + "</font>";
        mB.tvAgreement.setText(Html.fromHtml("温馨提示：登陆时将自动注册，初始密码为手机号码后六位，请前往个人中心更改，且代表您已同意" + content));
        mB.tvAgreement.setOnClickListener(this);
        mB.refreshLayout.setPureScrollModeOn();
    }

    /*SupportFragmentDelegate mDelegate = new SupportFragmentDelegate(this);
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDelegate.onAttach((Activity) context);
    }*/

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void addDisposable(Disposable d) {
        super.addDisposable(d);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_code:
                presenterl.code(mB.etPhone.getText().toString());
                break;
            case R.id.bt_login:
                presenterl.login(mB.etPhone.getText().toString(), mB.etCode.getText().toString());
                break;
            case R.id.tv_agreement:
                UIHelper.startHtmlFrg(this, 0);
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
        UIHelper.startMainFrg(this);
    }
}
