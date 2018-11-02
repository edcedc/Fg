package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;
import com.fanwang.fgcm.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2018/5/4.
 */

public interface OnDemandView extends IBaseView {

    void setRefresh(List<DataBean> bean);

    void setLoadMore();

    void onError(Throwable e);

    void setRefreshLayoutMode(int totalRow);

}
