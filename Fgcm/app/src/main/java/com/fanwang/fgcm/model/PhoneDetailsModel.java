package com.fanwang.fgcm.model;

import android.content.Context;

import com.fanwang.fgcm.presenter.listener.OnPhoneDetailsListener;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * Created by edison on 2018/4/27.
 */

public interface PhoneDetailsModel {

    void onSubmit(int mSelectType, String s, List<LocalMedia> localMediaList, String number, String individual, String range,
                  double mLongitude, double mLatitude, String mProvince, String mCity, String mDistrict, int mRange, int redEnvelopesState, int payType,
                  Context act, String videoUrl,String url, OnPhoneDetailsListener listener);

    void signatureGet(OnPhoneDetailsListener listener);
}
