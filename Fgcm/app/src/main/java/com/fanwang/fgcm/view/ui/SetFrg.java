package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FSetBinding;
import com.fanwang.fgcm.utils.SharedAccount;
import com.igexin.sdk.PushManager;

/**
 * Created by edison on 2018/5/8.
 *  设置
 */

public class SetFrg extends BaseFragment<FSetBinding> implements View.OnClickListener{
    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_set;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.set));
        mB.refreshLayout.setPureScrollModeOn();
        mB.lyPassword.setOnClickListener(this);
        mB.btSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_password:
                UIHelper.startModifyPasswordFrg(this);
                break;
            case R.id.bt_submit:
                User.getInstance().setUserObj(null);
                User.getInstance().setUserInformation(null);
                User.getInstance().setLogin(false);
                SharedAccount.getInstance(act).delete();
                PushManager.getInstance().stopService(act);
                UIHelper.startSplashAct();
                ActivityUtils.finishAllActivities();
                break;
        }
    }
}
