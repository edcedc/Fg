package com.fanwang.fgcm.presenter;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.model.EquipmentDetailsModel;
import com.fanwang.fgcm.model.EquipmentDetailsModellmpl;
import com.fanwang.fgcm.presenter.listener.OnEquipmentDetailsListener;
import com.fanwang.fgcm.view.impl.EquipmentDetailsView;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/28.
 */

public class EquipmentDetailsPresenterlmpl implements EquipmentDetailsPresenter, OnEquipmentDetailsListener {

    private EquipmentDetailsModel model;
    private EquipmentDetailsView view;

    public EquipmentDetailsPresenterlmpl(EquipmentDetailsView view){
        this.view = view;
        model = new EquipmentDetailsModellmpl();
    }

    @Override
    public void onSubmit(final Context act, final int mSelectType, final String remark, final List<LocalMedia> localMediaList, String seconds,
                         String time, final String eqpId, final double latitude, final double longitude, final boolean isMore, String price) {
        if (mSelectType == -1){
            ToastUtils.showShort(Utils.getApp().getString(R.string.please_advertising_scope));
            return;
        }
        switch (mSelectType){
            case 2:
            case 3:
                if (StringUtils.isEmpty(remark)){
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
        if (StringUtils.isEmpty(seconds)){
            ToastUtils.showShort(Utils.getApp().getString(R.string.empty_advertising_time));
            return;
        }
        seconds = seconds.substring(0, seconds.length() - 1);
        if (StringUtils.isEmpty(time)){
            ToastUtils.showShort(Utils.getApp().getString(R.string.empty_launch_time));
            return;
        }
        final String[] split = time.replaceAll(" ", "").split("~");
        final OnEquipmentDetailsListener listener = this;
        final String finalSeconds = seconds;
        JSONObject information = User.getInstance().getUserInformation();
        if (information != null){
            JSONObject userExtend = information.optJSONObject("userExtend");
            double balance = userExtend.optDouble("balance");
            String s = price.replaceAll("￥", "").replaceAll(" ", "");
            if (Double.valueOf(s) > balance){
                ToastUtils.showShort("广告币不足");
                view.startAdvertising();
                return;
            }
        }

        if (mSelectType == 0){
            model.signatureGet(listener);
        }else {
            model.eqpSave(act, mSelectType, localMediaList,
                    seconds, split[0], split[1], eqpId,
                    remark,-1, latitude, longitude,isMore, "", listener);
        }

        /*DialogUtil.showSelectEquipmentModel(act, DialogUtil.showSelectListType_2, new DialogUtil.OnClickListener() {
            @Override
            public void onClick(int position) {
                if (position == 1) {
                    ToastUtils.showShort("别乱点微信支付");
                    return;
                }
                model.eqpSave(act, mSelectType, localMediaList, finalSeconds, split[0], split[1], eqpId, remark,position, latitude, longitude,isMore,  listener);
            }
        });*/
    }

    private int mPosition = -1;//记录是什么支付


    @Override
    public void getEquId(String id) {
        if (StringUtils.isEmpty(id))return;
        model.eqpGetEqpSystemPrice(id, this);
    }

    @Override
    public void submit2(final Activity act, final int mSelectType, final String remark, final List<LocalMedia> localMediaList, String seconds,
                        String time, final String eqpId, final double latitude, final double longitude, final boolean isMore,
                        String price, String videoURL) {
        if (mSelectType == -1){
            ToastUtils.showShort(Utils.getApp().getString(R.string.please_advertising_scope));
            return;
        }
        switch (mSelectType){
            case 2:
            case 3:
                if (StringUtils.isEmpty(remark)){
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
        if (StringUtils.isEmpty(seconds)){
            ToastUtils.showShort(Utils.getApp().getString(R.string.empty_advertising_time));
            return;
        }
        seconds = seconds.substring(0, seconds.length() - 1);
        if (StringUtils.isEmpty(time)){
            ToastUtils.showShort(Utils.getApp().getString(R.string.empty_launch_time));
            return;
        }
        final String[] split = time.replaceAll(" ", "").split("~");
        final OnEquipmentDetailsListener listener = this;
        final String finalSeconds = seconds;
        JSONObject information = User.getInstance().getUserInformation();
        if (information != null){
            JSONObject userExtend = information.optJSONObject("userExtend");
            double balance = userExtend.optDouble("balance");
            String s = price.replaceAll("￥", "").replaceAll(" ", "");
            if (Double.valueOf(s) > balance){
                ToastUtils.showShort("广告币不足");
                view.startAdvertising();
                return;
            }
        }
        model.eqpSave(act, mSelectType, localMediaList,
                seconds, split[0], split[1], eqpId,
                remark,-1, latitude, longitude,isMore, videoURL, listener);
    }

    @Override
    public void onGetEqpSystemPriceSuccess(Object object) {
        view.showEqpSystemPrice(object);
    }

    @Override
    public void onSuccessEqpSave(BaseResponseBean responseBean) {
        view.onSuccessEqpSave();
    }

    @Override
    public void onSignatureGetSucces(String s) {
        view.onSignatureGetSucces(s);
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
}
