package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;
import com.fanwang.fgcm.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2018/4/25.
 */

public interface SearchView extends IBaseView {

    void setRefresh(DataBean bean);

    void onSearchSuccess(List<DataBean> list);
}
