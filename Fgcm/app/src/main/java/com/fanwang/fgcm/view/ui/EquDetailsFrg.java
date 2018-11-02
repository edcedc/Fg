package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.EquipmentListAdapter;
import com.fanwang.fgcm.adapter.ImageAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FEquDetailsBinding;
import com.fanwang.fgcm.utils.DataListTool;
import com.fanwang.fgcm.utils.PictureSelectorTool;
import com.fanwang.fgcm.weight.FullyGridLayoutManager;
import com.google.gson.Gson;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/30.
 * 设备详情
 */

public class EquDetailsFrg extends BaseFragment<FEquDetailsBinding> {

    private ImageAdapter imageAdapter;
    private List<LocalMedia> localMediaList = new ArrayList<>();
    private int mSelectType;

    private EquipmentListAdapter adapter;
    private List<DataBean> listBean = new ArrayList<>();

    private int mImageSize;
    private String videoUrl;

    @Override
    protected void initParms(Bundle bundle) {
        DataBean bean = new Gson().fromJson(bundle.getString("Bean"), DataBean.class);
        mSelectType = bean.getType();
        mB.tvOrder.setText("订单号：" + bean.getOrder_number());
        mB.tvTime.setText("开始时间：" + bean.getCreate_time());
        mB.tvLength.setText("广告时长：" + bean.getAdv_duration() + "秒");
        String start_delivery_time = bean.getStart_delivery_time();
        if (!StringUtils.isEmpty(start_delivery_time) && start_delivery_time.length() > 18){
            start_delivery_time = start_delivery_time.substring(0, start_delivery_time.length() - 9);
        }
        String end_delivery_time = bean.getEnd_delivery_time();
        if (!StringUtils.isEmpty(end_delivery_time) && end_delivery_time.length() > 18){
            end_delivery_time = end_delivery_time.substring(0, end_delivery_time.length() - 9);
        }
        mB.tvLaunchTime.setText("投放时间：" + start_delivery_time + " - " + end_delivery_time);
        String reason = bean.getReason();
        if (!StringUtils.isEmpty(reason)){
            mB.tvReason.setText(reason);
            mB.lyReason.setVisibility(View.VISIBLE);
        }
        switch (bean.getAudit()) {
            case 0:
                mB.tvExamineState.setText("审核状态：待处理");
                break;
            case 1:
                mB.tvExamineState.setText("审核状态：审核通过");
                break;
            default:
                mB.tvExamineState.setText("审核状态：审核不通过");
                break;
        }
        String ss = "api/";
        switch (mSelectType) {//0视频广告，1图片广告，2定格广告,3滚动广告',
            case 0:
                mSelectType = 2;
                mB.tvType.setText("广告类型：视频");
                mB.rlList.setVisibility(View.GONE);
                mB.lyContent.setVisibility(View.GONE);
                String url = bean.getVideo_url();
                if (!StringUtils.isEmpty(url)){
                    mB.ryRecyclerView.setVisibility(View.VISIBLE);
                    LocalMedia media = new LocalMedia();
//                    String s = (String) CloudApi.SERVLET_URL + url;
//                    if (s.contains(ss)){
//                        url = s.replace(ss, "");
//                    }
                    videoUrl = url;
                    media.setPath(videoUrl);
                    media.setPictureType("video/mp4");
                    media.setMimeType(PictureConfig.TYPE_VIDEO);
//                    media.setPictureType("image/jpeg");
                    localMediaList.add(media);

                }
                break;
            case 1:
                mSelectType = 1;
                mB.tvType.setText("广告类型：图片");
                mB.rlList.setVisibility(View.GONE);
                mB.ryRecyclerView.setVisibility(View.VISIBLE);
                mB.lyContent.setVisibility(View.GONE);
                String image1 = bean.getImage1();
                if (!StringUtils.isEmpty(image1)){
                    LocalMedia media = new LocalMedia();
                    String imgURL = CloudApi.SERVLET_URL + image1;
                    if (imgURL.contains(ss)){
                        imgURL = imgURL.replace(ss, "");
                    }
                    media.setPath(imgURL);
                    media.setMimeType(PictureConfig.TYPE_IMAGE);
                    media.setPictureType("image/jpeg");
                    localMediaList.add(media);
                }
                String image2 = bean.getImage2();
                if (!StringUtils.isEmpty(image2)){
                    LocalMedia media = new LocalMedia();
                    media.setMimeType(PictureConfig.TYPE_IMAGE);
                    media.setPictureType("image/jpeg");
                    String imgURL = CloudApi.SERVLET_URL + image2;
                    if (imgURL.contains(ss)){
                        imgURL = imgURL.replace(ss, "");
                    }
                    media.setPath(imgURL);
                    localMediaList.add(media);
                }
                String image3 = bean.getImage3();
                if (!StringUtils.isEmpty(image3)){
                    LocalMedia media = new LocalMedia();
                    media.setMimeType(PictureConfig.TYPE_IMAGE);
                    media.setPictureType("image/jpeg");
                    String imgURL = CloudApi.SERVLET_URL + image3;
                    if (imgURL.contains(ss)){
                        imgURL = imgURL.replace(ss, "");
                    }
                    media.setPath(imgURL);
                    localMediaList.add(media);
                }
                break;
            case 2:
                mB.tvType.setText("广告类型：定格广告");
                mB.rlList.setVisibility(View.GONE);
                mB.ryRecyclerView.setVisibility(View.GONE);
                mB.lyContent.setVisibility(View.VISIBLE);
                mB.tvContent.setText(bean.getRemark());
                break;
            default:
                mB.tvType.setText("广告类型：滚动广告");
                mB.rlList.setVisibility(View.GONE);
                mB.ryRecyclerView.setVisibility(View.GONE);
                mB.lyContent.setVisibility(View.VISIBLE);
                mB.tvContent.setText(bean.getRemark());
                break;
        }
        showPhoneEquType(bean);
    }

    /**
     * 判断手机还是设备类型
     */
    private void showPhoneEquType(DataBean bean) {
        List<DataBean.ListEqpBean> listEqp = bean.getListEqp();
        if (listEqp != null){//设备
            mB.tvTime.setVisibility(View.GONE);
            mB.tvLaunchTime.setVisibility(View.VISIBLE);
            mB.tvLength.setVisibility(View.VISIBLE);
            mB.rlList.setVisibility(View.VISIBLE);
            mB.tvRange.setVisibility(View.GONE);
            mB.tvAddress.setText("设备位置：" + bean.getProvince() + bean.getCity() + bean.getArea());
            mB.tvPrice.setText("支付金额：￥" + bean.getPrice());
            for (int i = 0;i < listEqp.size();i++){
                DataBean.ListEqpBean bean1 = listEqp.get(i);
                DataBean bean2 = new DataBean();
                bean2.setName(bean1.getName());
                bean2.setModelName(bean1.getModelLabel());
                bean2.setEqp_ids(bean1.getEqp_ids());
                bean2.setAddress(bean1.getProvince() + bean1.getCity() + bean1.getArea());
                listBean.add(bean2);
            }
            showEquRecyclerView();
        }else {//手机
            mB.tvTime.setVisibility(View.VISIBLE);
            mB.tvLaunchTime.setVisibility(View.GONE);
            mB.tvLength.setVisibility(View.GONE);
            mB.rlList.setVisibility(View.GONE);
            mB.tvRange.setVisibility(View.VISIBLE);
            mB.tvAddress.setText("位置：" + bean.getAddress());
            if (bean.getRed_envelopes_state() == 0){
                mB.tvRed.setVisibility(View.GONE);
                mB.tvPrice.setVisibility(View.GONE);
            }else {
                mB.tvRed.setVisibility(View.VISIBLE);
                mB.tvPrice.setVisibility(View.VISIBLE);
                mB.tvPrice.setText("总金额：￥" + bean.getPrice());
            }
            switch (bean.getRed_envelopes_state()){// '红包算法，0无红包，1固定红包，2随机红包',
                case 1:
                    mB.tvRed.setText("固定红包个数：" + bean.getPrice() + "/" + bean.getRed_envelopes_number());
                    break;
                case 2:
                    mB.tvRed.setText("随机红包个数：" + bean.getPrice() + "/" + bean.getRed_envelopes_number());
                    break;
                default:
                    break;
            }
            for (int i = 0;i < 4;i++){
                if (i == bean.getScope_type()){
                    mB.tvRange.setText("范围：" + DataListTool.toChinese((i + 1) + "") + "公里");
                    break;
                }
            }
            switch (bean.getScope_type()){
                case 5:
                    mB.tvRange.setText("范围：省");
                    break;
                case 6:
                    mB.tvRange.setText("范围：市");
                    break;
                case 7:
                    mB.tvRange.setText("范围：区");
                    break;
            }
        }
    }

    private void showEquRecyclerView() {
        if (adapter == null){
            adapter = new EquipmentListAdapter(act, listBean, false);
        }
        mB.rvList.setLayoutManager(new LinearLayoutManager(act));
        mB.rvList.setHasFixedSize(true);
        mB.rvList.setItemAnimator(new DefaultItemAnimator());
        mB.rvList.setAdapter(adapter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_equ_details;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.top_ad_details));
        mB.refreshLayout.setPureScrollModeOn();
        if (imageAdapter == null) {
            imageAdapter = new ImageAdapter(act, new ImageAdapter.onAddPicClickListener() {
                @Override
                public void onAddPicClick() {
                    if (mSelectType == 1) {
//                        PictureSelectorTool.PictureSelectorImage(act, localMediaList, localMediaList.size(), BundleDataInEvent.select_img);
                    } else if (mSelectType == 0) {
//                        imageAdapter.setSelectMax(1);
//                        PictureSelectorTool.PictureSelectorVideo(act, BundleDataInEvent.select_video);
                    }
                }
            });
        }
        if (mSelectType == 2){
            imageAdapter.setVideoImg(true);
            imageAdapter.notifyDataSetChanged();
        }
        imageAdapter.setDeleteImg(false);
        imageAdapter.setSelectMax(localMediaList.size());

        mB.recyclerView.setLayoutManager(new FullyGridLayoutManager(act, 4, GridLayoutManager.VERTICAL, false));
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (mSelectType == 2){
                    LocalMedia media = localMediaList.get(position);
                    String path = media.getPath();
                    UIHelper.startPlayVideoFrg(EquDetailsFrg.this, videoUrl);
                }else {
                    PictureSelectorTool.PictureMediaType(act, localMediaList, position);
                }
            }
        });
        imageAdapter.setList(localMediaList);
        imageAdapter.notifyDataSetChanged();
    }

}
