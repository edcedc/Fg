package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IBaseAjaxListener;

/**
 * Created by edison on 2018/4/12.
 */

public interface OnVideroDetailsListener extends IBaseAjaxListener {

    void onSuccess( );

    void onFabulousSuccess(int state);

    void onPlaybackSuccess();
}
