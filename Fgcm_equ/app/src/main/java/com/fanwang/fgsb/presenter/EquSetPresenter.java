package com.fanwang.fgsb.presenter;

/**
 * 作者：yc on 2018/7/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface EquSetPresenter {
    void regionListProvince();

    void regionListLevel(String provinceId, int i);

    void save(String name, String id, String province,
              String city, String area, String address, String lat, String lot, String modelLabelid);

    void labelListModelLabelId();
}
