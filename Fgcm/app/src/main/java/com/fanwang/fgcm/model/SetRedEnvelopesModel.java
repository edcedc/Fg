package com.fanwang.fgcm.model;

import android.content.Context;

import com.fanwang.fgcm.presenter.listener.OnSetRedEnvelopesListener;

/**
 * Created by edison on 2018/4/23.
 */

public interface SetRedEnvelopesModel {

    void ajaxVideoSave(Context act,String url,  int red_envelopes_state, String redEnvelopesNumber, String redEnvelopes,
                       String imgurl,int payType, String videoUrl, String videoDesc,
                       OnSetRedEnvelopesListener listener);

    void signatureGet(int position, OnSetRedEnvelopesListener listener);
    void signatureGet(Context act,String url, int red_envelopes_state, String redEnvelopesNumber, String redEnvelopes, String imgurl, int payType, String videoUrl, String videoDesc, OnSetRedEnvelopesListener listener);

    void TxVideo(String video, String imgUrl);
}
