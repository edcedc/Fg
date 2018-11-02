package com.fanwang.fgsb.presenter.listener;

import com.fanwang.fgsb.base.IBaseAjaxListener;
import com.fanwang.fgsb.bean.DataBean;

import java.util.List;

/**
 * 作者：yc on 2018/7/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface OnEquSetListener extends IBaseAjaxListener{

    void onSuccessProvince(Object object);

    void onSuccessCity(Object object);

    void onSuccessArea(Object object);

    void onSuccess();

    void onSuccessLabel(Object object);
}
