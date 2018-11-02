package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IBaseAjaxListener;
import com.fanwang.fgcm.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2018/4/25.
 */

public interface OnSearchListListener extends IBaseAjaxListener {

    void onSearchSuccess(List<DataBean> list);

}
