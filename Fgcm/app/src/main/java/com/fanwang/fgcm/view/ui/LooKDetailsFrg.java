package com.fanwang.fgcm.view.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FLookDetailsBinding;
import com.fanwang.fgcm.utils.Constants;
import com.fanwang.fgcm.utils.DialogUtil;
import com.fanwang.fgcm.utils.GlideImageUtils;
import com.fanwang.fgcm.utils.ShareTool;
import com.google.gson.Gson;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.umeng.socialize.ShareAction;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import io.reactivex.disposables.Disposable;

/**
 * 作者：yc on 2018/6/12.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  看广告详情
 */

public class LooKDetailsFrg extends BaseFragment<FLookDetailsBinding> implements  BGABanner.Delegate, BGABanner.Adapter<ImageView, String>{

    private boolean isAutoComplete = false;//是否播放完毕
    private boolean isDownTimer = false;//倒计时是否完成
    private ShareAction shareAction;
    private DataBean bean;

    private List<String> listImage = new ArrayList<>();
    private List<String> tips = new ArrayList<String>();

    private String shareUrl;

    @Override
    protected void initParms(Bundle bundle) {
        bean = new Gson().fromJson(bundle.getString("bean"), DataBean.class);
        switch (bean.getType()){
            case 0:
                shareUrl = CloudApi.videoShare + bean.getId();
                mB.videoPlayer.setVisibility(View.VISIBLE);
                loadCover(CloudApi.SERVLET_URL + bean.getCover_image(), bean.getVideo_url());
                break;
            case 1:
                shareUrl = CloudApi.imageShare + bean.getId();
                mB.banner.setVisibility(View.VISIBLE);
                String image1 = bean.getImage1();
                String image2 = bean.getImage2();
                String image3 = bean.getImage3();
                if (!StringUtils.isEmpty(image1))listImage.add(image1);
                if (!StringUtils.isEmpty(image2))listImage.add(image2);
                if (!StringUtils.isEmpty(image3))listImage.add(image3);
                mB.banner.setData(listImage, tips);
                mB.banner.setAdapter(LooKDetailsFrg.this);
                break;
            case 2:
                shareUrl = CloudApi.articleShare + bean.getId();
                mB.tvFixed.setVisibility(View.VISIBLE);
                mB.tvFixed.setText(bean.getRemark());
                break;
            case 3:
                shareUrl = CloudApi.articleShare + bean.getId();
                mB.tvRoll.setVisibility(View.VISIBLE);
                mB.tvRoll.setText(bean.getRemark());
                mB.tvRoll.init(act.getWindowManager());
                mB.tvRoll.startScroll();
                break;
        }
        shareAction = ShareTool.getInstance().shareAction(act, shareUrl);
        if (bean.getRed_envelopes_state() != 0 && bean.getIsRedEnvelopesRecord() == 0 && bean.getType() != 0){
            isAutoComplete = true;
            isDownTimer = true;
            downTimer.start();
        }

        String url = bean.getUrl();
        if (!StringUtils.isEmpty(url)){
            mB.tvUrl.setVisibility(View.VISIBLE);
            mB.tvUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIHelper.startHtmlFrg(LooKDetailsFrg.this, "详情", bean.getUrl(), 3);
                }
            });
        }
    }

    /**
     *  设置视频封面和视频
     */
    private void loadCover(String imgUrl, String videoUrl) {
//        String ss = "api/";
//        String s = (String) videoUrl;
//        if (s.contains(ss)){
//            videoUrl = s.replace(ss, "");
//        }
        ImageView imageView = new ImageView(act);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideImageUtils.load(act, imgUrl, imageView);
        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setThumbImageView(imageView)
                .setIsTouchWiget(false)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setUrl(videoUrl)
                .setCacheWithPlay(true)
                .setStartAfterPrepared(true)
                .setLooping(false)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        LogUtils.e("onPrepared");

                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        LogUtils.e("onEnterFullscreen");
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                        LogUtils.e("onAutoComplete");
                        isAutoComplete = true;
                        isDownTimer = true;
                        downTimer.cancel();
                        if (bean.getRed_envelopes_state() != 0 && bean.getIsRedEnvelopesRecord() == 0){
                            showRed(isDownTimer, isAutoComplete);
                        }
                    }

                    @Override
                    public void onClickStartError(String url, Object... objects) {
                        super.onClickStartError(url, objects);
                        LogUtils.e("onClickStartError");
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        LogUtils.e("onQuitFullscreen");
                    }
                })
                .setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        LogUtils.e("onClick");
                    }
                })
                .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                    @Override
                    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
                        Debuger.printfLog(" progress " + progress + " secProgress " + secProgress + " currentPosition " + currentPosition + " duration " + duration);
                    }
                })
                .build(mB.videoPlayer);
        mB.videoPlayer.startPlayLogic();
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_look_details;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.top_look_ads), getString(R.string.share));
//        String url = CloudApi.SERVLET_URL + "pc/adv/detail?advId=" + bean.getId();
//        url = url.replace("api/", "");
    }

    @Override
    protected void setOnRightClickListener() {
        super.setOnRightClickListener();
        shareAction.open();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LooKDetailsFrg.this != null && downTimer != null){
            downTimer.cancel();
        }
        mB.videoPlayer.release();
//        mB.videoPlayer.clearCurrentCache();
        ShareTool.getInstance().release(act);
    }

    @Override
    public void onPause() {
        super.onPause();
        mB.videoPlayer.onVideoPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mB.videoPlayer.onVideoResume();
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        shareAction.close();
    }


    private CountDownTimer downTimer = new CountDownTimer(Constants.video_length, 1000 ) {
        @Override
        public void onTick(long millisUntilFinished) {
            //                LogUtils.e(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            isDownTimer = true;
            showRed(isDownTimer, isAutoComplete);
        }
    };


    /**
     *  显示红包
     * @param isDownTimer
     * @param isAutoComplete
     */
    private void showRed(final boolean isDownTimer, final boolean isAutoComplete){
        if (isDownTimer && isAutoComplete){
            DialogUtil.showRedEnvelopes(act,bean.getRed_envelopes(), bean.getId(),
                     new DialogUtil.ReceiveRedListener() {
                        @Override
                        public void showLoading() {
                            LooKDetailsFrg.this.showLoading();
                        }

                        @Override
                        public void hideLoading() {
                            LooKDetailsFrg.this.isDownTimer = false;
                            LooKDetailsFrg.this.isAutoComplete = false;
                            LooKDetailsFrg.this.hideLoading();
                        }

                        @Override
                        public void addDisposable(Disposable disposable) {
                            LooKDetailsFrg.this.addDisposable(disposable);
                        }
                    });
        }
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
        GlideImageUtils.load(act, CloudApi.SERVLET_URL + model, itemView);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {

    }
}
