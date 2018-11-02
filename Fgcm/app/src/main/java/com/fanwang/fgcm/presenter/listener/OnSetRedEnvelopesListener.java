package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IBaseAjaxListener;

/**
 * Created by edison on 2018/4/12.
 */

public interface OnSetRedEnvelopesListener extends IBaseAjaxListener {

    void onSuccess(String beanResponse, int payType);

    void onSignatureGetSucces(int red_envelopes_state, String redEnvelopesNumber, String redEnvelopes, String imgurl, int position,
                              String videoUrl, String videoDesc,  String url);
}
