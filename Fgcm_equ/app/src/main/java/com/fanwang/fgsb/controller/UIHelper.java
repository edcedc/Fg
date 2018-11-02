package com.fanwang.fgsb.controller;

import android.os.Bundle;

import com.fanwang.fgsb.base.BaseFragment;
import com.fanwang.fgsb.view.EquSetFrg;
import com.fanwang.fgsb.view.MainFrg;
import com.fanwang.fgsb.view.SetPwdFrg;

/**
 * Created by Administrator on 2017/2/22.
 */

public final class UIHelper {

    private UIHelper() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     *  首页
     * @param root
     */
   public static void startMainFrg(BaseFragment root){
       MainFrg frg = MainFrg.newInstance();
       Bundle bundle = new Bundle();
       frg.setArguments(bundle);
       root.start(frg);
    }
     /**
     *  设置
     * @param root
     */
   public static void startEquSetFrg(BaseFragment root){
       EquSetFrg frg = EquSetFrg.newInstance();
       Bundle bundle = new Bundle();
       frg.setArguments(bundle);
       root.start(frg);
    }

    /**
     *  密码
     * @param root
     */
    public static void startSetPwdFrg(BaseFragment root){
        SetPwdFrg frg= SetPwdFrg.newInstance();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

}