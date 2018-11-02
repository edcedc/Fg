package com.fanwang.fgcm.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.MainViderFrgAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FVideoBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.weight.videorecord.TCVideoRecordActivity;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.ugc.TXRecordCommon;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by edison on 2018/4/13.
 *  短视频
 */

public class VideoFrg extends BaseFragment<FVideoBinding> implements View.OnClickListener {

    public static VideoFrg newInstance() {
        Bundle args = new Bundle();
        VideoFrg fragment = new VideoFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initParms(Bundle bundle) {

    }
    @Override
    protected int bindLayout() {
        return R.layout.f_video;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void initView(View view) {
        mB.btRanking.setOnClickListener(this);
        mB.btVideo.setOnClickListener(this);
        EventBus.getDefault().register(this);
        setSwipeBackEnable(false);
    }

    @Subscribe
    public void onEventMainThread(BundleDataInEvent event) {
        if (event.getType() == BundleDataInEvent.mVideoImg){
            Bundle bundle = (Bundle) event.getObject();
            UIHelper.startVideoFrametFrg(this, bundle);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static final String RECORD_CONFIG_MAX_DURATION = "record_config_max_duration";
    public static final String RECORD_CONFIG_MIN_DURATION = "record_config_min_duration";
    public static final String RECORD_CONFIG_ASPECT_RATIO = "record_config_aspect_ratio";
    public static final String RECORD_CONFIG_RECOMMEND_QUALITY = "record_config_recommend_quality";
    public static final String RECORD_CONFIG_HOME_ORIENTATION = "record_config_home_orientation";
    public static final String RECORD_CONFIG_RESOLUTION = "record_config_resolution";
    public static final String RECORD_CONFIG_BITE_RATE = "record_config_bite_rate";
    public static final String RECORD_CONFIG_FPS = "record_config_fps";
    public static final String RECORD_CONFIG_GOP = "record_config_gop";
    public static final String RECORD_CONFIG_NEED_EDITER = "record_config_go_editer";
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ranking://排行榜
                UIHelper.startRankingListFrg(this);
                break;
            case R.id.bt_video://发布视频
                Intent intent = new Intent(act, TCVideoRecordActivity.class);
                intent.putExtra(RECORD_CONFIG_MIN_DURATION, 5 * 1000);
                intent.putExtra(RECORD_CONFIG_MAX_DURATION, 60 * 1000);
                intent.putExtra(RECORD_CONFIG_ASPECT_RATIO, TXRecordCommon.VIDEO_ASPECT_RATIO_9_16);//比列
                intent.putExtra(RECORD_CONFIG_RESOLUTION, TXRecordCommon.VIDEO_RESOLUTION_720_1280);//分辨率
                intent.putExtra(RECORD_CONFIG_BITE_RATE, 12000);//码率
                intent.putExtra(RECORD_CONFIG_FPS, 20);//帧率
                intent.putExtra(RECORD_CONFIG_GOP, 3);//关键帧间隔
                intent.putExtra(RECORD_CONFIG_HOME_ORIENTATION, TXLiveConstants.VIDEO_ANGLE_HOME_DOWN); // 竖屏录制
                intent.putExtra(RECORD_CONFIG_NEED_EDITER, true);//是否进入编辑页面
                startActivity(intent);
                 break;

        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mB.viewPager.setAdapter(new MainViderFrgAdapter(getChildFragmentManager()
                , getString(R.string.topbar_enclosure), getString(R.string.topbar_hot)));
        mB.tab.setupWithViewPager(mB.viewPager);
    }

}
