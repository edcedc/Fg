package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;

/**
 * Created by edison on 2018/4/23.
 */

public interface SetRedEnvelopesView extends IBaseView {

    void showFixedAmount();

    void showRandomAmount();

    void onSuccess(String beanResponse, int payType);

    void onSignatureGetSucces(int red_envelopes_state, String redEnvelopesNumber, String redEnvelopes, String imgurl, int position, String videoUrl, String videoDesc, String sigId);
}
