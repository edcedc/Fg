package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IBaseAjaxListener;

/**
 * Created by edison on 2018/5/8.
 */

public interface OnAboutListener extends IBaseAjaxListener{

    void setWeb(Object object);

    void showVersion(String url, String version);
}
