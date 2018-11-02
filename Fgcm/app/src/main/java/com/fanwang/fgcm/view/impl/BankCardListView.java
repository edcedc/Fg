package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;
import com.fanwang.fgcm.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2018/5/5.
 */

public interface BankCardListView extends IBaseView {

    void setRefresh(List<DataBean> list);

    void setLoadMore();

    void onError(Throwable e);

    void setRefreshLayoutMode(int totalRow);

}
