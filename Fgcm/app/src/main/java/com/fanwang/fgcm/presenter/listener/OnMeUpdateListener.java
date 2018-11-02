package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IBaseAjaxListener;

/**
 * Created by Administrator on 2018/6/1.
 */

public interface OnMeUpdateListener extends IBaseAjaxListener{
    void onUpdateHead(String path);

    void onUpdateData();

}
