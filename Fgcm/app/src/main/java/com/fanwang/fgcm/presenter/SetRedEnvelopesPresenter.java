package com.fanwang.fgcm.presenter;

import android.app.Activity;
import android.content.Context;

/**
 * Created by edison on 2018/4/23.
 */

public interface SetRedEnvelopesPresenter {

    void submit(Context act, int red_envelopes_state, String redEnvelopesNumber, String redEnvelopes,
                String imgurl,int payType, String videoUrl, String videoDesc, String url);

    void signatureGet(int position);

    void submit2(Activity act, int red_envelopes_state, String s, String s1, String imgUrl, int payType, String videoUrl, String videoDesc, String url);
}
