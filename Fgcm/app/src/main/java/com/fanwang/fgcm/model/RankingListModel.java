package com.fanwang.fgcm.model;

import com.fanwang.fgcm.base.IOnBaseListListener;

/**
 * Created by edison on 2018/4/13.
 */

public interface RankingListModel{

    void ajaxList(int pagerNumber,Object object, IOnBaseListListener listener);

}
