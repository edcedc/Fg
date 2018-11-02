package com.fanwang.fgsb.view.impl;

import com.fanwang.fgsb.base.IBaseView;

/**
 * 作者：yc on 2018/7/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface EquSetView extends IBaseView {
    void showProvince(Object object);

    void showArea(Object object);

    void showCity(Object object);

    void onSaveSuccess();

    void onSaveLabel(Object object);
}
