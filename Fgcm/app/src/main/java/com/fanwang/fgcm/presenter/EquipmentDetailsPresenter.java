package com.fanwang.fgcm.presenter;

import android.app.Activity;
import android.content.Context;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * Created by edison on 2018/4/28.
 */

public interface EquipmentDetailsPresenter {

    void onSubmit(Context act, int mSelectType, String remark, List<LocalMedia> localMediaList, String seconds, String time
            , String eqpId, double latitude, double longitude, boolean isMore, String price);

    void getEquId(String id);

    void submit2(Activity act, int mSelectType, String s, List<LocalMedia> localMediaList, String s1, String s2, String id, double latitude, double longitude, boolean b, String s3, String videoURL);
}
