package com.fanwang.fgcm.model;

import android.content.Context;

import com.fanwang.fgcm.presenter.listener.OnEquipmentDetailsListener;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * Created by edison on 2018/4/28.
 */

public interface EquipmentDetailsModel {

    void eqpGetEqpSystemPrice(String id, OnEquipmentDetailsListener listener);

    void eqpSave(Context act, int type, List<LocalMedia> localMediaList, String seconds, String launchTime, String endTime,
                 String eqpId, String remark,int payType,
                 double latitude, double longitude,boolean isMore,String videoUrl, OnEquipmentDetailsListener listener);

    void signatureGet(OnEquipmentDetailsListener listener);
}
