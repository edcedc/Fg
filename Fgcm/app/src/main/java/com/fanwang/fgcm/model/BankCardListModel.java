package com.fanwang.fgcm.model;

import com.fanwang.fgcm.presenter.listener.OnBankCardListListener;

/**
 * Created by edison on 2018/5/5.
 */

public interface BankCardListModel {

    void ajaxList(int pagerNumber, OnBankCardListListener listener);


}
