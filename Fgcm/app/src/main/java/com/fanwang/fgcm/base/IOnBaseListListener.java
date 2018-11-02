package com.fanwang.fgcm.base;

/**
 * Created by edison on 2018/4/13.
 */

public interface IOnBaseListListener extends IBaseAjaxListener {

    void onSuccess(Object data);

    void onRefreshLayoutMode(int totalRow);

}
