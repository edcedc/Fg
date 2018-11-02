package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;

/**
 * Created by edison on 2018/5/4.
 */

public interface ScreenView extends IBaseView {

    void showRecyclerViewType();

    void showCity(Object object);

    void showProvince(Object object);

    void showArea(Object object);
}
