package com.fanwang.fgcm.view.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.ImageAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FEquipmentDetailsBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.presenter.EquipmentDetailsPresenter;
import com.fanwang.fgcm.presenter.EquipmentDetailsPresenterlmpl;
import com.fanwang.fgcm.utils.DataListTool;
import com.fanwang.fgcm.utils.DialogUtil;
import com.fanwang.fgcm.utils.PictureSelectorTool;
import com.fanwang.fgcm.utils.TopRightMenuTool;
import com.fanwang.fgcm.view.impl.EquipmentDetailsView;
import com.fanwang.fgcm.weight.FullyGridLayoutManager;
import com.fanwang.fgcm.weight.toprightmenu.MenuItem;
import com.fanwang.fgcm.weight.toprightmenu.TopRightMenu;
import com.fanwang.fgcm.weight.videorecord.utils.TXUGCPublish;
import com.fanwang.fgcm.weight.videorecord.utils.TXUGCPublishTypeDef;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/4/28.
 *  设备详情
 */

public class EquipmentDetailsFrg extends BaseFragment<FEquipmentDetailsBinding> implements EquipmentDetailsView , View.OnClickListener, TXUGCPublishTypeDef.ITXVideoPublishListener{

    private double latitude;
    private double longitude;

    private List<MenuItem> menuItems = DataListTool.getPublishingModeList();
    private List<MenuItem> timeItems = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private List<LocalMedia> localMediaList = new ArrayList<>();
    private int mSelectType = -1;//广告类型
    private int mSslectTime = -1;//广告时长
    private EquipmentDetailsPresenter presenter;
    private String modelId;
    private String id;
    private String model_labelId;
    private double second = -1;

    private ProgressDialog dialog;
    private TXUGCPublish mVideoPublish;

    @Override
    protected void initParms(Bundle bundle) {
        String name = bundle.getString("name");
        String eqpIds = bundle.getString("eqpIds");
        model_labelId = bundle.getString("model_labelId");
        id = bundle.getString("id");
        modelId = bundle.getString("model");
        String address = bundle.getString("address");
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");
        mB.tvName.setText("设备名称：" + name);
        mB.tvModel.setText("设备型号：" + modelId);
        mB.tvId.setText("设备ID：" + eqpIds);
        mB.tvAddress.setText("设备地址：" + address);
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_equipment_details;
    }

    @Override
    protected void initView(View view) {
        presenter = new EquipmentDetailsPresenterlmpl(this);
        setToolbarTitle(getString(R.string.top_launch_details));
        mB.refreshLayout.setPureScrollModeOn();
        mB.tvAdvertisementType.setOnClickListener(this);
        mB.tvAdvertisingTime.setOnClickListener(this);
        mB.tvLaunchTime.setOnClickListener(this);
        mB.btSubmit.setOnClickListener(this);
        showImg();
        EventBusActivityScope.getDefault(act).register(this);
        presenter.getEquId(model_labelId);
        mB.etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mB.tvTextSize.setText(editable.length() + "/40");
                if (editable.length() > 40){
                    return;
                }
            }
        });
        dialog = new ProgressDialog(act);
        if (mVideoPublish == null) {
            mVideoPublish = new TXUGCPublish(act, User.getInstance().getUserId());
            mVideoPublish.setListener(this);
        }
    }

    private void showImg() {
        if (imageAdapter == null) {
            imageAdapter = new ImageAdapter(act, new ImageAdapter.onAddPicClickListener() {
                @Override
                public void onAddPicClick() {
                    if (mSelectType == 1){
                        imageAdapter.setSelectMax(3);
                        PictureSelectorTool.PictureSelectorImage(act, localMediaList, 3, BundleDataInEvent.select_img);
                    }else if (mSelectType == 0){
                        imageAdapter.setSelectMax(1);
                        PictureSelectorTool.PictureSelectorVideo(act, BundleDataInEvent.select_video);
                    }
                }
            });
        }
        mB.recyclerView.setLayoutManager(new FullyGridLayoutManager(act, 3, GridLayoutManager.VERTICAL, false));
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PictureSelectorTool.PictureMediaType(act, localMediaList, position);
            }
        });
    }

    @Override
    public void showEqpSystemPrice(Object object) {
        List<DataBean> listBean = (List<DataBean>) object;
        for (int i  = 0;i < listBean.size();i++){
            DataBean bean = listBean.get(i);
            MenuItem item = new MenuItem();
            item.setText(bean.getSeconds() + "");
            item.setId(bean.getPrice() + "");
            item.setType(bean.getType());
            timeItems.add(item);
        }
    }

    @Override
    public void onSuccessEqpSave() {
        pop();
        pop();
    }

    @Override
    public void startAdvertising() {
        UIHelper.startAdvertisingFrg(this, 0);
    }

    @Override
    public void onSignatureGetSucces(String sigId) {
        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        // signature计算规则可参考 https://www.qcloud.com/document/product/266/9221
        param.signature = sigId;
        param.videoPath = localMediaList.get(0).getPath();
        param.enableResume = true;
        param.enableHttps = false;
        param.fileName= com.blankj.utilcode.util.TimeUtils.getNowMills() + "";
        LogUtils.e(sigId, localMediaList.get(0).getPath());
        int publishCode = mVideoPublish.publishVideo(param);
        if (publishCode != 0) {
            showToast("发布失败，错误码：" + publishCode);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_advertisement_type:
                TopRightMenuTool.TopRightMenu(act, menuItems, mB.tvAdvertisementType, 200, new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        MenuItem item = menuItems.get(position);
                        mB.tvAdvertisementType.setText(item.getText());
                        mSelectType = position;
                        mB.tvAdvertisingTime.setText("");
                        mB.tvLaunchTime.setText("");
                        mB.tvPrice.setText("");
                        switch (position){
                            case 0:
                                mB.tvMaterialType.setText(getText(R.string.select_video));
                                mB.lyText.setVisibility(View.GONE);
                                mB.ryRecyclerView.setVisibility(View.VISIBLE);
                                localMediaList.clear();
                                imageAdapter.notifyDataSetChanged();
                                break;
                            case 1:
                                mB.tvMaterialType.setText(getText(R.string.select_img));
                                mB.lyText.setVisibility(View.GONE);
                                mB.ryRecyclerView.setVisibility(View.VISIBLE);
                                localMediaList.clear();
                                imageAdapter.notifyDataSetChanged();
                                break;
                            case 2:
                            case 3:
                                mB.tvMaterialType.setText("        素材:");
                                mB.lyText.setVisibility(View.VISIBLE);
                                mB.ryRecyclerView.setVisibility(View.GONE);
                                break;
                        }
                    }
                });
                break;
            case R.id.tv_advertising_time://广告时长
                if (mSelectType == -1){
                    showToast("请先选择广告类型");
                    return;
                }
                final List<MenuItem> list = new ArrayList<>();
                for (MenuItem menuItem : timeItems){
                    if (mSelectType == menuItem.getType()){
                        list.add(menuItem);
                    }
                }
                TopRightMenuTool.TopRightMenu(act, list, mB.tvAdvertisingTime, 200, new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        MenuItem item = list.get(position);
                        mB.tvAdvertisingTime.setText(item.getText() + "秒");
                        second = Double.valueOf(item.getId());
                        mB.tvPrice.setText("￥ " + (second * timeNumber));
                        mSslectTime = position;
                    }
                });
                break;
            case R.id.tv_launch_time://投放时间
                UIHelper.startLaunchTimeFrg(this);
                break;
            case R.id.bt_submit://支付
                String s = mB.tvPrice.getText().toString();
                if (StringUtils.isEmpty(s)){
                    showToast("请选择投放价格");
                    return;
                }
                DialogUtil.showDialogType(act, mB.tvPrice.getText().toString(), DialogUtil.showDialog1, new DialogUtil.OnClickListener2() {
                    @Override
                    public void onClick(int position) {
                        presenter.onSubmit(act, mSelectType, mB.etText.getText().toString(), localMediaList, mB.tvAdvertisingTime.getText().toString(),
                                mB.tvLaunchTime.getText().toString(), id, latitude, longitude, false, mB.tvPrice.getText().toString());
                    }
                });
               break;
        }
    }

    @Subscribe
    public void onEventMainThread(BundleDataInEvent event) {
        if (event.getType() == BundleDataInEvent.select_img || event.getType() == BundleDataInEvent.select_video){
            localMediaList.addAll(PictureSelector.obtainMultipleResult((Intent)event.getObject()));
            imageAdapter.setList(localMediaList);
            imageAdapter.notifyDataSetChanged();
        }
    }

    private int timeNumber = 1;
    @Subscribe
    public void onEventLaunchTime(BundleDataInEvent event) {
        if (event.getType() == BundleDataInEvent.launch_time){
            String time = (String) event.getObject().toString().replaceAll(" ", "");
            String[] splitTime = time.split("~");
            String fitTimeSpan = TimeUtils.getFitTimeSpan(splitTime[0] + " 00:00:00", splitTime[1] + " 00:00:00", 1);
            fitTimeSpan = fitTimeSpan.substring(0, fitTimeSpan.length() - 1);
            timeNumber = Integer.valueOf(fitTimeSpan) + 1;
            mB.tvLaunchTime.setText(event.getObject().toString());
            if (second != -1){
                mB.tvPrice.setText("￥ " + (second * timeNumber));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusActivityScope.getDefault(act).unregister(this);
        mVideoPublish.canclePublish();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onError(Throwable e) {
        pop();
    }

    @Override
    public void onPublishProgress(long uploadBytes, long totalBytes) {
        LogUtils.e((int) (100 * uploadBytes / totalBytes));
        dialog.setMessage("正在上传中" + (int) (100 * uploadBytes / totalBytes) + "%");
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onPublishComplete(TXUGCPublishTypeDef.TXPublishResult result) {
        LogUtils.e(result.retCode + " Msg:" + (result.retCode == 0 ? result.videoURL : result.descMsg));
        dialog.setMessage("正在请求网络中...");
        if (result.retCode == 0) {
            presenter.submit2(act, mSelectType, mB.etText.getText().toString(), localMediaList, mB.tvAdvertisingTime.getText().toString(),
                    mB.tvLaunchTime.getText().toString(), id, latitude, longitude, false, mB.tvPrice.getText().toString(), result.videoURL);
        }else {
            showToast(result.descMsg);
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        mVideoPublish.canclePublish();
        hideLoading();
        return super.onBackPressedSupport();
    }
}
