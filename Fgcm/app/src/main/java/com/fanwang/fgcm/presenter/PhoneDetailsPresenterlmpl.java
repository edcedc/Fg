package com.fanwang.fgcm.presenter;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.model.PhoneDetailsModel;
import com.fanwang.fgcm.model.PhoneDetailsModellmpl;
import com.fanwang.fgcm.presenter.listener.OnPhoneDetailsListener;
import com.fanwang.fgcm.utils.DialogUtil;
import com.fanwang.fgcm.view.impl.PhoneDetailsView;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/27.
 */

public class PhoneDetailsPresenterlmpl implements PhoneDetailsPresenter, OnPhoneDetailsListener {

    private PhoneDetailsView view;
    private PhoneDetailsModel model;

    public PhoneDetailsPresenterlmpl(PhoneDetailsView detailsView) {
        this.view = detailsView;
        model = new PhoneDetailsModellmpl();
    }

    @Override
    public void showLoading() {
        view.showLoading();
    }

    @Override
    public void hideLoading() {
        view.hideLoading();
    }

    @Override
    public void onError(Throwable e) {
        view.hideLoading();
        ToastUtils.showShort(e.getMessage().toString());
    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onSuccess(int payType, String data) {
        view.onSuccess(payType, data);
    }

    @Override
    public void onSignatureGetSucces(String s) {
        view.onSignatureGetSucces(s);
    }

    @Override
    public void submit(final int mSelectType, final String s, final List<LocalMedia> localMediaList, final String number,
                       final String individual, final String range,
                       final double mLongitude, final double mLatitude,
                       final String mProvince, final String mCity, final String mDistrict, final int mRange,
                       final int mRedType, final Context act, final String url) {
        if (mSelectType == -1) {
            ToastUtils.showShort(Utils.getApp().getString(R.string.please_advertising_scope));
            return;
        }
        switch (mSelectType){
            case 2:
            case 3:
                if (StringUtils.isEmpty(s)){
                    ToastUtils.showShort(Utils.getApp().getString(R.string.empty_text));
                    return;
                }
                break;
            case 1:
                if (localMediaList.size() == 0){
                    ToastUtils.showShort(Utils.getApp().getString(R.string.empty_img));
                    return;
                }
                break;
            case 0:
                if (localMediaList.size() == 0){
                    ToastUtils.showShort(Utils.getApp().getString(R.string.empty_video));
                    return;
                }
                break;
            default:

                break;
        }
        if (StringUtils.isEmpty(number) || Double.valueOf(number) <= 0) {
            ToastUtils.showShort(Utils.getApp().getString(R.string.empty_money));
            return;
        }
        if (Double.valueOf(number) < 0.2){
            ToastUtils.showShort("分配金额不能少于0.3");
            return;
        }
        if (mRedType == 0 && Double.valueOf(number) / Double.valueOf(individual) < 0.3){
            ToastUtils.showShort("分配金额不能少于0.3");
            return;
        }
        if (StringUtils.isEmpty(individual) || Double.valueOf(individual) <= 0) {
            ToastUtils.showShort(Utils.getApp().getString(R.string.empty_individual));
            return;
        }
        if (StringUtils.isEmpty(range)) {
            ToastUtils.showShort(Utils.getApp().getString(R.string.empty_scope));
            return;
        }
        final OnPhoneDetailsListener listener = this;
        DialogUtil.showSelectEquipmentModel(act, DialogUtil.showSelectListType_2, new DialogUtil.OnClickListener() {
            @Override
            public void onClick(int position) {
                mPosition = position;
                if (mSelectType == 3){
                    model.signatureGet(listener);
                }else {
                    model.onSubmit(mSelectType, s, localMediaList, number,
                            individual, range,
                            mLongitude, mLatitude, mProvince, mCity, mDistrict, mRange, mRedType, position,act, "",url,  listener);
                }
            }
        });
    }
    private int mPosition = -1;//记录是什么支付

    @Override
    public void submit2(final int mSelectType, final String s, final List<LocalMedia> localMediaList, final String number,
                        final String individual, final String range,
                        final double mLongitude, final double mLatitude,
                        final String mProvince, final String mCity, final String mDistrict, final int mRange,
                        final int mRedType, final Activity act, String videoURL, String url) {
        model.onSubmit(mSelectType, s, localMediaList, number,
                individual, range,
                mLongitude, mLatitude, mProvince, mCity, mDistrict, mRange, mRedType, mPosition,act, videoURL,  url,  this);
    }
}
