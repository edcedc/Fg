package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IOnBaseListListener;

/**
 * Created by edison on 2018/4/13.
 */

public interface OnCommentBottomListListener extends IOnBaseListListener {

    void onCommentSuccess(Object object);

    void onTwoCommentSuccess(Object object);
}
