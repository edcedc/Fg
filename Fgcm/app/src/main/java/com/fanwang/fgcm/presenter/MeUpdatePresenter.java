package com.fanwang.fgcm.presenter;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * Created by Administrator on 2018/6/1.
 */

public interface MeUpdatePresenter {
    void onHead(List<LocalMedia> path);

    void onNick(String trim);

    void onModel(String trim);

    void onSex(int mRB);
}
