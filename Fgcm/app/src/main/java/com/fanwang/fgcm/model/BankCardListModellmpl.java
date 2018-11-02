package com.fanwang.fgcm.model;

import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.presenter.listener.OnBankCardListListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2018/5/5.
 */

public class BankCardListModellmpl implements BankCardListModel{

    @Override
    public void ajaxList(int pagerNumber, OnBankCardListListener listener) {
        List<DataBean> listBean = new ArrayList<>();
        for (int i = 0;i < 5;i++){
            listBean.add(new DataBean());
        }
        listener.onSuccess(listBean);
    }

}
