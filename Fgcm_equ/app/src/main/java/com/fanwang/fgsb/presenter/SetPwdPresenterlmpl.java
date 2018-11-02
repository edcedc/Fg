package com.fanwang.fgsb.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgsb.model.SetPwdModel;
import com.fanwang.fgsb.model.SetPwdModellmpl;
import com.fanwang.fgsb.utils.cache.SharedAccount;
import com.fanwang.fgsb.view.impl.SetPwdView;

/**
 * 作者：yc on 2018/7/6.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class SetPwdPresenterlmpl implements SetPwdPresenter {

    private SetPwdView view;

    private SetPwdModel model;

    public SetPwdPresenterlmpl(SetPwdView view) {
        this.view = view;
        model = new SetPwdModellmpl();
    }

    @Override
    public void login(String pwd) {
        if (StringUtils.isEmpty(pwd)){
            ToastUtils.showShort("请输入密码");
            return;
        }

        SharedAccount account = SharedAccount.getInstance(Utils.getApp());
        String pwd1 = account.getPwd();
        if (StringUtils.isEmpty(pwd1)){
            account.save("123456");
            pwd1 = account.getPwd();
        }

        if (pwd.equals(pwd1)){
            view.startMain();
        }else {
            ToastUtils.showShort("密码错误");
        }
    }

    @Override
    public void modify(String s, String s1, String s2) {
        if (StringUtils.isEmpty(s) || StringUtils.isEmpty(s1) || StringUtils.isEmpty(s2)){
            ToastUtils.showShort("请填写完整");
            return;
        }
        if (!s1.equals(s2)){
            ToastUtils.showShort("两次密码不一致");
            return;
        }
        if ((s1.length() < 5 || s1.length() >17) || (s2.length() < 5 || s2.length() >17)){
            ToastUtils.showShort("密码长度6-16位之间");
            return;
        }
        SharedAccount.getInstance(Utils.getApp()).save(s1);
        view.cancel();
    }
}
