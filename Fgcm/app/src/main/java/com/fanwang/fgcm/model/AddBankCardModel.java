package com.fanwang.fgcm.model;

import com.fanwang.fgcm.presenter.listener.OnAddBankCardListener;

/**
 * Created by edison on 2018/5/7.
 */

public interface AddBankCardModel {
    void onSuccess(String s, String s1, String s2, OnAddBankCardListener listener);
}
