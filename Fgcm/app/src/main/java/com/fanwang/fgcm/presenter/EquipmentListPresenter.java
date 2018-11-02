package com.fanwang.fgcm.presenter;

/**
 * Created by edison on 2018/5/3.
 */

public interface EquipmentListPresenter {

    void onRequest(int range, String modelType, int pagerNumber, double latitude, double longitude);

    void onLabelListModelLabelId();

    void onPageTerminalEquipment(String regionId, String provinceArray, String cityArray, String areaArray, int pagerNumber);


}
