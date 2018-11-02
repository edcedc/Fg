package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;
import com.fanwang.fgcm.bean.DataBean;

/**
 * Created by edison on 2018/5/7.
 */

public interface AddBankCardView extends IBaseView {

    void openingBank();

    void onSuccess(DataBean data);
}
