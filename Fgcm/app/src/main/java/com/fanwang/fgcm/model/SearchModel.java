package com.fanwang.fgcm.model;

import com.fanwang.fgcm.presenter.listener.OnSearchListListener;

/**
 * Created by edison on 2018/4/25.
 */

public interface SearchModel {

    void ajaxPlaceV2(String s, OnSearchListListener listener);
}
