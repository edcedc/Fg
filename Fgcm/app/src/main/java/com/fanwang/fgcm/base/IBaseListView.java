package com.fanwang.fgcm.base;

/**
 * Created by edison on 2018/5/7.
 */

public interface IBaseListView extends IBaseView {

    void setRefreshLayoutMode(int totalRow);

    void onError(Throwable e);

    void setData(Object data);

}
