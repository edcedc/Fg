package com.fanwang.fgcm.model;

import com.fanwang.fgcm.presenter.listener.OnVideroDetailsListener;

/**
 * Created by edison on 2018/4/20.
 */

public interface VideoDescModel {

    void ajaxDetails(OnVideroDetailsListener listener);

    void ajaxSavePraise(String id, int state, OnVideroDetailsListener listener);

    void ajaxVideoAddPlayback(String id, OnVideroDetailsListener listener);
}
