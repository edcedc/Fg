package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.databinding.FPlayVideoBinding;
import com.fanwang.fgcm.utils.GlideImageUtils;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;

/**
 * 作者：yc on 2018/6/14.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  播放视频
 */

public class PlayVideoFrg extends BaseFragment<FPlayVideoBinding>{

    @Override
    protected void initParms(Bundle bundle) {
        String path = bundle.getString("path");
        loadCover("", path);
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_play_video;
    }

    @Override
    protected void initView(View view) {
        mB.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop();
            }
        });
    }

    /**
     *  设置视频封面和视频
     */
    private void loadCover(String imgUrl, String videoUrl) {
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
    public void onDestroy() {
        super.onDestroy();
        mB.videoPlayer.release();
        mB.videoPlayer.clearCurrentCache();
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
}
