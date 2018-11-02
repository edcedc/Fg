package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;

/**
 * Created by edison on 2018/4/13.
 */

public interface EquipmentListView extends IBaseView {

    void setRefresh(Object object);

    void onError(Throwable e);

    void setRefreshLayoutMode(int totalRow);

    void setModelLabelIdList(Object data);

    void onRefresh();

}
