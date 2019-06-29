package com.fanwang.fgcm.view.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FVideoDescBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.presenter.VideoDescPresenter;
import com.fanwang.fgcm.presenter.VideoDescPresenterlmpl;
import com.fanwang.fgcm.utils.Constants;
import com.fanwang.fgcm.utils.DialogUtil;
import com.fanwang.fgcm.utils.GlideImageUtils;
import com.fanwang.fgcm.utils.ShareTool;
import com.fanwang.fgcm.view.impl.VideoDescView;
import com.google.gson.Gson;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.umeng.socialize.ShareAction;

import io.reactivex.disposables.Disposable;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;


/**
 * Created by edison on 2018/4/13.
 *  短视频详情
 */

public class VideoDescFrg extends BaseFragment<FVideoDescBinding> implements View.OnClickListener, VideoDescView {

    private CommentBottomFrg bottomFrg;
    private VideoDescPresenter presenter;
    private DataBean bean;
    private boolean isAutoComplete = false;//是否播放完毕
    private boolean isDownTimer = false;//倒计时是否完成
    private ShareAction shareAction;
    private int isPraise = -1;//点赞之类状态
    private int position;
    private int mEventState;

    @Override
    protected void initParms(Bundle bundle) {
        position = bundle.getInt("position");
        mEventState = bundle.getInt("EventState");
        bean = new Gson().fromJson(bundle.getString("Bean"), DataBean.class);
        String nick = bean.getNick();
        if (!StringUtils.isEmpty(nick) && !nick.equals("null")){
            mB.tvName.setText(nick);
        }else {
            mB.tvName.setText(bean.getName());
        }
        mB.tvPlay.setText("播放量：" + bean.getPlayback());
        GlideImageUtils.load(act, CloudApi.SERVLET_URL + bean.getHead(), mB.ivHead);
        loadCover(CloudApi.SERVLET_URL + bean.getCover_image(), bean.getVideo_url());
        mB.tvAwcNumber.setText(bean.getAwc_number() + "");
        mB.tvPraise.setText(bean.getPraise() + "");
        mB.tvContent.setText(bean.getVideo_desc());
        isPraise = bean.getIsPraise();
        mB.ivIsPraise.setBackgroundResource(isPraise == 0 ? R.mipmap.b25 : R.mipmap.b83);
        mB.lyComplaint.setOnClickListener(this);
        shareAction = ShareTool.getInstance().shareAction(act, CloudApi.shortVideoShare + bean.getId());
        String url = bean.getUrl();
        if (!StringUtils.isEmpty(url)){
            mB.tvDesc.setVisibility(View.VISIBLE);
            mB.tvDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIHelper.startHtmlFrg(VideoDescFrg.this, "详情", bean.getUrl(), 3);
                }
            });
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_video_desc;
    }

    @Override
    protected void initView(View view) {
        setSofia(false);
        presenter = new VideoDescPresenterlmpl(this);
        mB.finish.setOnClickListener(this);
        mB.lyComment.setOnClickListener(this);
        mB.lyShare.setOnClickListener(this);
        mB.lyFabulous.setOnClickListener(this);
        mB.tvDesc.setOnClickListener(this);
        bottomFrg = new CommentBottomFrg();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_desc:
                UIHelper.startHtmlFrg(this, "详情", bean.getUrl(), 3);
                break;
            case R.id.finish:
                pop();
                break;
            case R.id.ly_comment:
                if (bottomFrg == null){
                    bottomFrg = new CommentBottomFrg();
                }
                bottomFrg.setBundle(bean.getId(), bean.getCreate_time(), bean.getPlayback(), bean.getAwc_number());
                bottomFrg.show(getChildFragmentManager(), "dialog");
                break;
            case R.id.ly_share:
                shareAction.open();
                break;
            case R.id.ly_fabulous:
                presenter.fabulous(bean.getId(), isPraise);
                break;
            case R.id.ly_complaint:
                UIHelper.startComplaintsReportFrg(this, bean.getId());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (VideoDescFrg.this != null && downTimer != null){
            downTimer.cancel();
        }
        mB.videoPlayer.release();
        mB.videoPlayer.clearCurrentCache();
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

    @Override
    public void onSuccess() {

    }

    @Override
    public void FabulousSuccessState(int state) {
        mB.ivIsPraise.setBackgroundResource(R.mipmap.b83);
        String s = mB.tvPraise.getText().toString();
        mB.tvPraise.setText((Integer.valueOf(s) + 1) + "");
        mB.lyFabulous.setEnabled(false);
        EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(mEventState, position));
    }

    @Override
    public void onPlaybackSuccess() {
        String s = mB.tvPlay.getText().toString();
        mB.tvPlay.setText("播放量：" + (bean.getPlayback() + 1));
        if (mEventState == BundleDataInEvent.mEncloureFabulous){
            mEventState = BundleDataInEvent.mEnclourePlay;
        }else if (mEventState == BundleDataInEvent.mHotFabulous){
            mEventState = BundleDataInEvent.mHotPlay;
        }
        EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(mEventState, position));
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
                .setShowFullAnimation(true)
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
                        if (bean.getRed_envelopes_state() != 0 && bean.getIsRedEnvelopesRecord() == 0){
                            downTimer.start();
                        }
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
                        presenter.ajaxVideoAddPlayback(bean.getId());
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
//        mB.videoPlayer.startPlayLogic();
    }

    /**
     *  显示红包
     * @param isDownTimer
     * @param isAutoComplete
     */
    private void showRed(final boolean isDownTimer, final boolean isAutoComplete){
        if (isDownTimer && isAutoComplete){
            DialogUtil.showRedEnvelopes(act, bean.getRed_envelopes(),bean.getId(),
                    mEventState,position,  new DialogUtil.ReceiveRedListener() {
                @Override
                public void showLoading() {
                    VideoDescFrg.this.showLoading();
                }

                @Override
                public void hideLoading() {
                    VideoDescFrg.this.isDownTimer = false;
                    VideoDescFrg.this.isAutoComplete = false;
                    VideoDescFrg.this.hideLoading();
                }

                @Override
                public void addDisposable(Disposable disposable) {
                    VideoDescFrg.this.addDisposable(disposable);
                }
            });
        }
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

    @Override
    public void onError(Throwable e) {
        hideLoading();
    }
}
