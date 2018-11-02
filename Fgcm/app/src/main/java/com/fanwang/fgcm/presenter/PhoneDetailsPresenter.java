package com.fanwang.fgcm.presenter;

import android.app.Activity;
import android.content.Context;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * Created by edison on 2018/4/27.
 */

public interface PhoneDetailsPresenter {
    void submit(int mSelectType, String s, List<LocalMedia> localMediaList,
                String number, String individual, String range,
                double mLongitude, double mLatitude, String mProvince, String mCity, String mDistrict, int mRange, int mRedType, Context act, String url);

    void submit2(int mSelectType, String s, List<LocalMedia> localMediaList, String s1, String s2, String s3, double mLongitude, double mLatitude,
                 String mProvince, String mCity, String mDistrict, int mKilometre, int mRedType, Activity act, String videoURL, String url);
}
