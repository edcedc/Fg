package com.fanwang.fgcm.model;

import com.fanwang.fgcm.presenter.listener.OnAboutListener;

/**
 * Created by edison on 2018/5/8.
 */

public interface AboutModel {
    void ajaxData(OnAboutListener listener);

    void ajaxVersion(OnAboutListener listener);
}
