package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IBaseAjaxListener;
import com.fanwang.fgcm.bean.BaseResponseBean;

/**
 * Created by Administrator on 2018/5/24.
 */

public interface OnEquipmentDetailsListener extends IBaseAjaxListener{

    void onGetEqpSystemPriceSuccess(Object object);

    void onSuccessEqpSave(BaseResponseBean responseBean);

    void onSignatureGetSucces(String s);
}
