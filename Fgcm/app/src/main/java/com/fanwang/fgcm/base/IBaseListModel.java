package com.fanwang.fgcm.base;

import com.fanwang.fgcm.base.IOnBaseListListener;

/**
 * Created by edison on 2018/5/7.
 */

public interface IBaseListModel {

    void ajaxData(int pagerNumber, IOnBaseListListener listener);

}
