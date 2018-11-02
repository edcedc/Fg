package com.fanwang.fgcm.model;

import com.fanwang.fgcm.presenter.listener.OnScreenListener;

/**
 * Created by edison on 2018/5/4.
 */

public interface ScreenModel {

    void ajaxRegionListProvince(OnScreenListener listener);

    void ajaxRegionListLevel(String id, int provinceType, OnScreenListener listener);

}
