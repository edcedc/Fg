package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IBaseAjaxListener;
import com.fanwang.fgcm.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2018/5/4.
 */

public interface OnScreenListener extends IBaseAjaxListener {

   void onSuccessRegionListProvince(Object object);

   void onSuccessCity(Object object);

   void onSuccessArea(Object object);

}
