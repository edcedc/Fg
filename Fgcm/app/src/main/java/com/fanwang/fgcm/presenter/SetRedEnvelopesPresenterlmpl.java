package com.fanwang.fgcm.presenter;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.model.SetRedEnvelopesModel;
import com.fanwang.fgcm.model.SetRedEnvelopesModellmpl;
import com.fanwang.fgcm.presenter.listener.OnSetRedEnvelopesListener;
import com.fanwang.fgcm.view.impl.SetRedEnvelopesView;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/23.
 */

public class SetRedEnvelopesPresenterlmpl implements SetRedEnvelopesPresenter, OnSetRedEnvelopesListener {

    private SetRedEnvelopesView redEnvelopesView;
    private SetRedEnvelopesModel model;

    public SetRedEnvelopesPresenterlmpl(SetRedEnvelopesView view) {
        this.redEnvelopesView = view;
        this.model = new SetRedEnvelopesModellmpl();
    }

    @Override
    public void showLoading() {
        redEnvelopesView.showLoading();
    }

    @Override
    public void hideLoading() {
        redEnvelopesView.hideLoading();
    }

    @Override
    public void onError(Throwable e) {
        ToastUtils.showShort(e.getMessage());
        LogUtils.e(e.getMessage());
    }

    @Override
    public void onAddDisposable(Disposable d) {
        redEnvelopesView.addDisposable(d);
    }

    @Override
    public void onSuccess(String beanResponse, int payType) {
        redEnvelopesView.onSuccess(beanResponse, payType);
    }

    @Override
    public void onSignatureGetSucces(int red_envelopes_state, String redEnvelopesNumber, String redEnvelopes, String imgurl, int position, String videoUrl, String videoDesc, String sigId) {
        redEnvelopesView.onSignatureGetSucces(red_envelopes_state, redEnvelopesNumber, redEnvelopes, imgurl, position,videoUrl, videoDesc, sigId);
    }

    @Override
    public void submit(Context act, int red_envelopes_state, String redEnvelopesNumber, String redEnvelopes, String imgurl,
                       int payType, String videoUrl, String videoDesc, String url) {
        if (StringUtils.isEmpty(redEnvelopesNumber)){
            ToastUtils.showShort(act.getString(R.string.empty_individual));
            return;
        }
        if (StringUtils.isEmpty(redEnvelopes)){
            ToastUtils.showShort(act.getString(R.string.empty_money));
            return;
        }
        if (Double.valueOf(redEnvelopes) < 0.2){
            ToastUtils.showShort("分配金额不能少于0.3");
            return;
        }
        if (red_envelopes_state == 2 && Double.valueOf(redEnvelopes) / Double.valueOf(redEnvelopesNumber) < 0.3){
            ToastUtils.showShort("分配金额不能少于0.3");
            return;
        }
        model.signatureGet(act,url, red_envelopes_state, redEnvelopesNumber, redEnvelopes,imgurl, payType, videoUrl,videoDesc,this);

//        model.ajaxVideoSave(act, red_envelopes_state, redEnvelopesNumber, redEnvelopes,imgurl, payType, videoUrl,videoDesc, this);
    }

    @Override
    public void signatureGet(int position) {
        model.signatureGet(position, this);

    }

    @Override
    public void submit2(Activity act, int red_envelopes_state, String redEnvelopesNumber, String redEnvelopes, String imgUrl, int payType, String videoUrl, String videoDesc, String url) {
        model.ajaxVideoSave(act,url, red_envelopes_state, redEnvelopesNumber, redEnvelopes,imgUrl, payType, videoUrl,videoDesc, this);
    }

}
