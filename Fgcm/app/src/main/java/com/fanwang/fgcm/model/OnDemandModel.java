package com.fanwang.fgcm.model;

import com.fanwang.fgcm.base.IOnBaseListListener;

/**
 * Created by edison on 2018/5/4.
 */

public interface OnDemandModel {

    void ajaxList(int pagerNumber,String id, IOnBaseListListener enclourePresenterlmpl);

}
