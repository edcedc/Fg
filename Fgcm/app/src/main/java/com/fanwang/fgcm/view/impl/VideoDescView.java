package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;

/**
 * Created by edison on 2018/4/20.
 */

public interface VideoDescView extends IBaseView {

    void onSuccess();

    void FabulousSuccessState(int state);

    void onPlaybackSuccess();
}
