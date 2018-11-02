package com.fanwang.fgcm.model;

import com.fanwang.fgcm.base.IOnBaseListListener;
import com.fanwang.fgcm.presenter.EquipmentListPresenterlmpl;
import com.fanwang.fgcm.presenter.listener.OnEquipmentListListener;

/**
 * Created by edison on 2018/5/3.
 */

public interface EquipmentListModel {

    void ajaxLabelListModelLabelId(OnEquipmentListListener listener);

    void ajaxEqpPageTerminalEquipment(int range, String modelId, double latitude, double longitude, int pagerNumber, OnEquipmentListListener listener);

     void onPageTerminalEquipment(String regionId, String provinceArray, String cityArray, String areaArray, int pagerNumber, OnEquipmentListListener listener);

}
