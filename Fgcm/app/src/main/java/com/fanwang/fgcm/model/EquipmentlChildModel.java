package com.fanwang.fgcm.model;

import com.fanwang.fgcm.base.IOnBaseListListener;

/**
 * Created by Administrator on 2018/5/30.
 */

public interface EquipmentlChildModel {
    void ajaxPageUserEepADV(int pagerNumber, int mPosition, String url, IOnBaseListListener listener);
    void ajaxPageUserEepADV(int pagerNumber, int mPosition, String url,String equId, IOnBaseListListener listener);
}
