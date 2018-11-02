package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.model.EquipmentListDetailsModel;
import com.fanwang.fgcm.model.EquipmentListDetailsModellmpl;
import com.fanwang.fgcm.view.impl.EquipmentListDetailsView;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/5/4.
 */

public class EquipmentListDetailsPresenterlmpl implements EquipmentListDetailsPresenter, EquipmentListDetailsListener{

    private EquipmentListDetailsView view;
    private EquipmentListDetailsModel model;

    public EquipmentListDetailsPresenterlmpl(EquipmentListDetailsView view){
        this.view = view;
        model = new EquipmentListDetailsModellmpl();
    }

    @Override
    public void submit(int mSelectType, String s, List<LocalMedia> localMediaList, String s1, String s2) {
        if (mSelectType == -1){
            ToastUtils.showShort(view.getMyContext().getString(R.string.please_advertising_scope));
            return;
        }
        switch (mSelectType){
            case 0:
            case 1:
                if (StringUtils.isEmpty(s)){
                    ToastUtils.showShort(view.getMyContext().getString(R.string.empty_text));
                    return;
                }
                break;
            case 2:
                if (localMediaList.size() == 0){
                    ToastUtils.showShort(view.getMyContext().getString(R.string.empty_img));
                    return;
                }
                break;
            case 3:
                if (localMediaList.size() == 0){
                    ToastUtils.showShort(view.getMyContext().getString(R.string.empty_video));
                    return;
                }
                break;
            default:

                break;
        }
        if (StringUtils.isEmpty(s1)){
            ToastUtils.showShort(view.getMyContext().getString(R.string.empty_advertising_time));
            return;
        }
        if (StringUtils.isEmpty(s2)){
            ToastUtils.showShort(view.getMyContext().getString(R.string.empty_launch_time));
            return;
        }
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

    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }
}
