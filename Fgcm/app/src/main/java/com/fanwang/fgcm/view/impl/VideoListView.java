package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;

/**
 * Created by edison on 2018/4/13.
 */

public interface VideoListView extends IBaseView {

    void setRefresh(Object data);

    void onError(Throwable e);

    void setRefreshLayoutMode(int totalRow);

    void setLoadMore();

}
