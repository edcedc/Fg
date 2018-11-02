package com.fanwang.fgcm.model;

import com.fanwang.fgcm.presenter.listener.OnEquipmentMapListener;

/**
 * Created by edison on 2018/4/27.
 */

public interface EquipmentMapModel {
    void ajax(double latitude, double longitude, OnEquipmentMapListener listener);
}
