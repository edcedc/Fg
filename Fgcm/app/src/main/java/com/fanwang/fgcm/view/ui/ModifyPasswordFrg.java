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
import com.fanwang.fgcm.databinding.FModifyPwdBinding;
import com.lzy.okgo.model.Response;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/5/8.
 *  修改密码
 */

public class ModifyPasswordFrg extends BaseFragment<FModifyPwdBinding>{
    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_modify_pwd;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.modify_password));
        mB.refreshLayout.setPureScrollModeOn();
        mB.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old = mB.etOldPwd.getText().toString();
                String pwd1 = mB.etNewPwd.getText().toString();
                String pwd2 = mB.etNewPwd2.getText().toString();
                if (StringUtils.isEmpty(old)){
                    mB.etOldPwd.setError(getString(R.string.empty_text));
                    return;
                }
                if (StringUtils.isEmpty(pwd1)){
                    mB.etNewPwd.setError(getString(R.string.empty_text));
                    return;
                }
                if (StringUtils.isEmpty(pwd2)){
                    mB.etNewPwd2.setError(getString(R.string.empty_text));
                    return;
                }
                if (!pwd1.equals(pwd2)){
                    showToast("新密码和重复密码不一致");
                    return;
                }
                CloudApi.userUpdatePassword(old, pwd1, pwd2)
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
//                                    User.getInstance().setUserObj(null);
//                                    User.getInstance().setUserInformation(null);
//                                    User.getInstance().setLogin(false);
//                                    UIHelper.startMainAct();
//                                    ActivityUtils.finishAllActivities();
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
            }
        });
    }
}
