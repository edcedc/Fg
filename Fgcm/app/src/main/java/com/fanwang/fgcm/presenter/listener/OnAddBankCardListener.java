package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IBaseAjaxListener;
import com.fanwang.fgcm.bean.DataBean;

/**
 * Created by edison on 2018/5/7.
 */

public interface OnAddBankCardListener extends IBaseAjaxListener {

    void onSuccess(DataBean data);

}
