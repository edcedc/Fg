package com.fanwang.fgcm.view.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FLoginBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.presenter.LoginPresenter;
import com.fanwang.fgcm.presenter.LoginPresenterImpl;
import com.fanwang.fgcm.utils.CountDownTimerUtils;
import com.fanwang.fgcm.utils.SharedAccount;
import com.fanwang.fgcm.view.impl.LoginView;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/4/8.
 * 登陆
 */

public class LoginFrg extends BaseFragment<FLoginBinding> implements LoginView, View.OnClickListener{

    public static LoginFrg newInstance() {
        Bundle args = new Bundle();
        LoginFrg fragment = new LoginFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    private Drawable srcBottom;
    private LoginPresenter presenter;

    @Override
    protected int bindLayout() {
        return R.layout.f_login;
    }

    @Override
    protected void initView(View view) {
        presenter = new LoginPresenterImpl(this);
        srcBottom = getResources().getDrawable(R.mipmap.a03);
        mB.tvLogin.setOnClickListener(this);
        mB.btLogin.setOnClickListener(this);
        mB.tvRegister.setOnClickListener(this);
        mB.btCode.setOnClickListener(this);
        mB.btCodeLogin.setOnClickListener(this);
        mB.btForgetPwd.setOnClickListener(this);
        mB.btRegisterLogin.setOnClickListener(this);
        mB.btWxLogin.setOnClickListener(this);
        mB.btForgetPwd.setOnClickListener(this);
        mB.cbSelected.setOnClickListener(this);
        setSwipeBackEnable(false);
        String mobile = SharedAccount.getInstance(act).getMobile();
        String password = SharedAccount.getInstance(act).getPassword();
        if (!StringUtils.isEmpty(mobile) && !StringUtils.isEmpty(password)){
            mB.etLoginPhone.setText(mobile);
            mB.etLoginPwd.setText(password);
        }
        EventBusActivityScope.getDefault(act).register(this);
    }

    @Subscribe
    public void onMainThreadEvent(BundleDataInEvent event){
        if (event.getType() == BundleDataInEvent.mLookMap){

        }
    }

    @Override
    public boolean onBackPressedSupport() {
        act.finish();
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusActivityScope.getDefault(act).unregister(this);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setSofia(true);
    }

    @Override
    public void showLogin() {
        mB.lyLogin.setVisibility(View.VISIBLE);
        mB.lyRegister.setVisibility(View.GONE);
        mB.tvLogin.setTextColor(getResources().getColor(R.color.white));
        mB.tvRegister.setTextColor(getResources().getColor(R.color.gray_5D5C5A));
        mB.tvLogin.setCompoundDrawablesWithIntrinsicBounds(null, null, null, srcBottom);
        mB.tvRegister.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    @Override
    public void showRegister() {
        mB.lyRegister.setVisibility(View.VISIBLE);
        mB.lyLogin.setVisibility(View.GONE);
        mB.tvRegister.setTextColor(getResources().getColor(R.color.white));
        mB.tvLogin.setTextColor(getResources().getColor(R.color.gray_5D5C5A));
        mB.tvRegister.setCompoundDrawablesWithIntrinsicBounds(null, null, null, srcBottom);
        mB.tvLogin.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    @Override
    public void wxLogin() {

    }

    @Override
    public void setLoginPhoneError(String string) {
        mB.etLoginPhone.setError(string);
    }

    @Override
    public void setLoginPwdError(String string) {
        mB.etLoginPwd.setError(string);
    }

    @Override
    public void onCodeSuccess() {
        new CountDownTimerUtils(act, 60000, 1000, mB.btCode).start();
    }

    @Override
    public void setRegisterPhoneError(String toast) {
        mB.etRegisterPhone.setError(toast);
    }

    @Override
    public void setRegisterCodeError(String toast) {
        mB.etRegisterCode.setError(toast);
    }

    @Override
    public void setRegisterPwdError(String toast) {
        mB.etRegisterPwd.setError(toast);
    }

    @Override
    public void setRegisterPwd2Error(String toast) {
        mB.etRegisterPwd2.setError(toast);
    }

    @Override
    public void onSuccess() {
        UIHelper.startMainAct();
        act.finish();
    }

    @Override
    public void onWXRegisterSuccess(String wx_openid) {
        UIHelper.startBindingWXFrg(this, wx_openid);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                showLogin();
                break;
            case R.id.bt_wx_login:
                boolean b = UMShareAPI.get(act).isAuthorize(act, SHARE_MEDIA.WEIXIN);
                if (b){
//                    presenter.wxLogin(SharedAccount.getInstance(act).getWX_OPENID(), SharedAccount.getInstance(act).getWX_ACCESS_TOKEN());
                    UMShareAPI.get(act).deleteOauth(act, SHARE_MEDIA.WEIXIN, listener);
                }else {
                    UMShareAPI.get(act).doOauthVerify(act, SHARE_MEDIA.WEIXIN, listener);
                }
                break;
            case R.id.tv_register:
                showRegister();
                break;
            case R.id.bt_code:
                presenter.getCode(mB.etRegisterPhone.getText().toString());
                break;
            case R.id.bt_login:
                /*EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
                CloudApi.text()
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Response<JSONObject>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Response<JSONObject> jsonObjectResponse) {
                                LogUtils.e(jsonObjectResponse.body().optString("data"));
                                pay(jsonObjectResponse.body().optString("data"));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });*/

                presenter.login(mB.etLoginPhone.getText().toString(), mB.etLoginPwd.getText().toString());
                break;
            case R.id.bt_forget_pwd:
                UIHelper.startForgetPwdFrg(this);
                break;
            case R.id.bt_register_login:
                presenter.registerLogin(mB.etRegisterPhone.getText().toString(), mB.etRegisterCode.getText().toString(), mB.etRegisterPwd.getText().toString(), mB.etRegisterPwd2.getText().toString(), mB.cbSelected.isChecked());
                break;
            case R.id.bt_code_login:
                UIHelper.startCodeLogin(this);
                break;
            case R.id.cb_selected:
                UIHelper.startHtmlFrg(this, 0);
                break;
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(act).onSaveInstanceState(outState);
    }

    private UMAuthListener listener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
//            showLoading();
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//            hideLoading();
            if (map != null && map.size() != 0){
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    LogUtils.e(entry.getKey(), entry.getValue());
                    if (entry.getKey().equals("openid")){
                        SharedAccount.getInstance(act).saveWX_OPENID(entry.getValue());
                    }
                    if (entry.getKey().equals("access_token")){
                        SharedAccount.getInstance(act).saveWX_ACCESS_TOKEN(entry.getValue());
                    }
                }
                presenter.wxLogin(SharedAccount.getInstance(act).getWX_OPENID(), SharedAccount.getInstance(act).getWX_ACCESS_TOKEN());
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable t) {
//            hideLoading();
            LogUtils.e("失败：" + t.getMessage());
            showToast(t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
//            hideLoading();
            LogUtils.e("取消了");
        }
    };

}
