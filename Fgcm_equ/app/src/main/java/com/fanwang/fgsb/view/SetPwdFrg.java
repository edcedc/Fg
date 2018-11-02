package com.fanwang.fgsb.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fanwang.fgsb.R;
import com.fanwang.fgsb.base.BaseFragment;
import com.fanwang.fgsb.controller.UIHelper;
import com.fanwang.fgsb.presenter.SetPwdPresenterlmpl;
import com.fanwang.fgsb.view.impl.SetPwdView;
import com.flyco.roundview.RoundTextView;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 作者：yc on 2018/7/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  设置密码
 */

public class SetPwdFrg extends BaseFragment implements SetPwdView {

    public static SetPwdFrg newInstance() {
        Bundle args = new Bundle();
        SetPwdFrg fragment = new SetPwdFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private EditText etPwd, etPwd1, etPwd2;
    private RoundTextView btSubmit;
    private RoundTextView btModifyPwd;
    private RoundTextView btCancel;
    private View fyPwd1, tvPrompt, lyPwd2;
    private SetPwdPresenterlmpl presenterl;

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_set_pwd;
    }

    @Override
    protected void initView(View view) {
        presenterl = new SetPwdPresenterlmpl(this);
        etPwd = view.findViewById(R.id.et_pwd);
        btSubmit = view.findViewById(R.id.bt_submit);
        btModifyPwd = view.findViewById(R.id.bt_modify_pwd);
        btCancel = view.findViewById(R.id.bt_cancel);
        etPwd1 = view.findViewById(R.id.et_pwd1);
        etPwd2 = view.findViewById(R.id.et_pwd2);
        fyPwd1 = view.findViewById(R.id.fy_pwd1);
        tvPrompt = view.findViewById(R.id.tv_prompt);
        lyPwd2 = view.findViewById(R.id.ly_pwd2);

        RxView.clicks(btSubmit)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object object) {
                        presenterl.login(etPwd.getText().toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        RxView.clicks(btModifyPwd)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object object) {
                        if (btModifyPwd.getText().toString().equals(getString(R.string.modify_pwd))){
                            showModifyPwd();
                        }else {
                            presenterl.modify(etPwd.getText().toString(), etPwd1.getText().toString(), etPwd2.getText().toString());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(btCancel)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object object) {
                        pop();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void showLoginPwd(){
        fyPwd1.setVisibility(View.INVISIBLE);
        btModifyPwd.setText(getText(R.string.modify_pwd));
        lyPwd2.setVisibility(View.GONE);
        tvPrompt.setVisibility(View.GONE);
    }

    private void showModifyPwd(){
        btSubmit.setVisibility(View.GONE);
        fyPwd1.setVisibility(View.VISIBLE);
        lyPwd2.setVisibility(View.VISIBLE);
        tvPrompt.setVisibility(View.VISIBLE);
        btModifyPwd.setText(getText(R.string.submit1));

    }


    @Override
    public void startMain() {
//        UIHelper.startMainFrg(this);
        UIHelper.startEquSetFrg(this);
    }

    @Override
    public void cancel() {
        pop();
    }

}
