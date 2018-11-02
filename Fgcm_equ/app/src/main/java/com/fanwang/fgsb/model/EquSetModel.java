package com.fanwang.fgsb.model;

import com.fanwang.fgsb.presenter.EquSetPresenterlmpl;
import com.fanwang.fgsb.presenter.listener.OnEquSetListener;

/**
 * 作者：yc on 2018/7/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface EquSetModel {

    void regionListProvince(OnEquSetListener listener);

    void regionListLevel(String provinceId, int provinceType, OnEquSetListener listener);

    void eqpListEqpAdv(String name, String id, String province, String city, String area, String address,
                       String lat, String lot, String modelLabelid, OnEquSetListener listener);

    void labelListModelLabelId(OnEquSetListener listener);
}
