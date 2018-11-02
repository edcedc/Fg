package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;

/**
 * Created by edison on 2018/4/27.
 */

public interface EquipmentMapView extends IBaseView {


    void showData(Object object);


    void showBottomSheetDialog(String name, String eqpIds, String model, String address, double longitude, double latitude, String id, String model_labelId);
}
