package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IBaseAjaxListener;
import com.fanwang.fgcm.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2018/5/5.
 */

public interface OnBankCardListListener extends IBaseAjaxListener {

    void onSuccess(List<DataBean> list);

    void onError();

}
