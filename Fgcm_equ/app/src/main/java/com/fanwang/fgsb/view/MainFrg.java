package com.fanwang.fgsb.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.fanwang.fgsb.MyThread;
import com.fanwang.fgsb.R;
import com.fanwang.fgsb.base.BaseFragment;
import com.fanwang.fgsb.bean.DataBean;
import com.fanwang.fgsb.controller.CloudApi;
import com.fanwang.fgsb.controller.UIHelper;
import com.fanwang.fgsb.event.GtInEvent;
import com.fanwang.fgsb.event.GtSwitchInEvent;
import com.fanwang.fgsb.gt.MyGTIntentService;
import com.fanwang.fgsb.gt.MyGTService;
import com.fanwang.fgsb.presenter.MainPresenter;
import com.fanwang.fgsb.presenter.MainPresenterlmpl;
import com.fanwang.fgsb.utils.CountDownTimer;
import com.fanwang.fgsb.utils.GlideImageUtils;
import com.fanwang.fgsb.utils.MacAddressTool;
import com.fanwang.fgsb.utils.ScreenTool;
import com.fanwang.fgsb.utils.ZXingUtils;
import com.fanwang.fgsb.utils.cache.ShareFirstSqu;
import com.fanwang.fgsb.utils.cache.ShareOffOn;
import com.fanwang.fgsb.view.impl.MainView;
import com.fanwang.fgsb.weight.AutoScrollTextView;
import com.fanwang.fgsb.weight.EmptyControlVideo;
import com.fanwang.fgsb.weight.LandLayoutVideo;
import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.igexin.sdk.PushManager;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.ListGSYVideoPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import cn.bingoogolapple.bgabanner.BGABanner;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * 作者：yc on 2018/6/20.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class MainFrg extends BaseFragment implements MainView ,BGABanner.Delegate, BGABanner.Adapter<ImageView, String>, View.OnClickListener, VideoAllCallBack {


    public static MainFrg newInstance() {
        Bundle args = new Bundle();
        MainFrg fragment = new MainFrg();
        fragment.setArguments(args);
        return fragment;
    }


    private EmptyControlVideo videoPlayer;
    private BGABanner banner1, banner2, banner3;
    private ImageView ivZxing1, ivZxing2;
    private AutoScrollTextView tvRoll;
    private TextView tvFixed, tvTime;
    private View lyMain;

    private MainPresenter presenter;

    private Timer timer;

    private List<GSYVideoModel> listVideo = new ArrayList<>();
    private List<DataBean> listImg = new ArrayList<>();
    private List<String> listImg1 = new ArrayList<>();
    private List<String> listImg2 = new ArrayList<>();
    private List<String> listImg3 = new ArrayList<>();
    private List<DataBean> listText = new ArrayList<>();

    private CountDownTimer downTimerText;//文字倒计时
    private CountDownTimer downTimerImg1;//图片1倒计时
    private CountDownTimer downTimerImg2;//图片2倒计时
    private CountDownTimer downTimerImg3;//图片3倒计时
    private CountDownTimer videoDownTimer;//视频倒计时

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        LogUtils.e(ScreenTool.ScreenOrient(act));
        if (ScreenTool.ScreenOrient(act) == 0){
            return R.layout.f_horizontal_main;
        }else {
            return R.layout.f_vertical_main;
        }
    }

    @Override
    protected void initView(View view) {
        presenter = new MainPresenterlmpl(this);
        lyMain = view.findViewById(R.id.ly_main);
        videoPlayer = view.findViewById(R.id.video_player);
        banner1 = view.findViewById(R.id.banner1);
        banner2 = view.findViewById(R.id.banner2);
        banner3 = view.findViewById(R.id.banner3);
        ivZxing1 = view.findViewById(R.id.iv_zxing1);
        ivZxing2 = view.findViewById(R.id.iv_zxing2);
        tvRoll = view.findViewById(R.id.tv_roll);
        tvFixed = view.findViewById(R.id.tv_fixed);
        tvTime = view.findViewById(R.id.tv_time);
        TextView tvTitle = view.findViewById(R.id.tv_title);

        view.findViewById(R.id.bt_set).setOnClickListener(this);

        int screenHeight = ScreenUtils.getScreenHeight();
        tvRoll.setTextSize(screenHeight / 38);
        tvFixed.setTextSize(screenHeight / 38);
        tvTitle.setTextSize(screenHeight / 38);
        tvTime.setTextSize(screenHeight / 38);

        final String macAddress = MacAddressTool.getMacAddress();
        LogUtils.e(macAddress);
        initVideo();
        initGT();

        presenter.eqpListEqpAdv(macAddress);
//        presenter.downloadCodeGet();
        showDownload(CloudApi.downloadCodeGet);

        ivZxing2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivZxing2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                try {
                    Bitmap bitmap = ZXingUtils.createQRImage(macAddress,ivZxing2.getWidth());
                    ivZxing2.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final String nowString = TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTime.setText(nowString);
                    }
                });
            }
        },1000,1000);

//        MyThread my = new MyThread();
//        Thread thread = new Thread(my);
//        thread.start();
        EventBus.getDefault().register(this);

        banner1.setAdapter(this);
        banner2.setAdapter(this);
        banner3.setAdapter(this);

        videoPlayer.setIfCurrentIsFullscreen(true);
        videoPlayer.setVideoAllCallBack(this);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);

        //默认是否开关机
        lyMain.setVisibility(ShareOffOn.getInstance(act).getISOFFON() == false ? View.GONE : View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainThreadInEvent(GtInEvent event){
        JSONObject data = (JSONObject) event.getObject();
        DataBean bean = new DataBean();
        bean.setType(data.optInt("type"));
        bean.setAdv_duration(data.optInt("adv_duration"));
        bean.setImage1(data.optString("image1"));
        bean.setImage2(data.optString("image2"));
        bean.setImage3(data.optString("image3"));
        bean.setRemark(data.optString("remark"));
        bean.setVideo_url(data.optString("video_url"));
        setTypeData(bean);
        if (listVideo.size() == 1){
            setlistVideo(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//开关机
    public void onMainSwitchThreadInEvent(GtSwitchInEvent event){
        ShareFirstSqu.getInstance(act).save(event.isSwitch());
        lyMain.setVisibility(event.isSwitch() == false ? View.GONE : View.VISIBLE);
    }

    private void initGT() {
        // 为第三方自定义推送服务
        PushManager.getInstance().initialize(act, MyGTService.class);
//         为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(act, MyGTIntentService.class);
        PushManager.getInstance().turnOnPush(act);
    }

    @Override
    public void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        videoPlayer.release();
        videoPlayer.clearCurrentCache();
        if (timer != null){
            timer.cancel();
        }
        if (videoDownTimer != null){
            videoDownTimer.cancel();
        }
    }

    @Override
    public void initVideo() {
    }

    @Override
    public void setData(Object object) {
        List<DataBean> list = (List<DataBean>) object;
        for (int i = 0;i < list.size();i++){
            DataBean bean = list.get(i);
            setTypeData(bean);
        }
        setlistVideo(0);
        setListText(textPosition = 0);
        setListImg(imgPosition = 0);
//        int g = listImg.size();
//        int every=g/3;
//        for (int i =0;i < every;i++){
//            listImg11.add(listImg.get(i));
//        }
//        for (int i =every;i < every*2;i++){
//            listImg22.add(listImg.get(i));
//        }
//        for (int i =every*2;i < g;i++){
//            listImg23.add(listImg.get(i));
//        }

    }

    @Override
    public void showDownload(final String s) {
        ivZxing1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivZxing1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                try {
                    Bitmap bitmap = ZXingUtils.createQRImage(s,ivZxing1.getWidth());
                    ivZxing1.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //设置数据
    private void setTypeData(DataBean bean){
        switch (bean.getType()){
            case 0:
                String video_url = bean.getVideo_url();
                if (!StringUtils.isEmpty(video_url) && video_url.contains("http://1256123801")){
                    listVideo.add(new GSYVideoModel(bean.getVideo_url(), "" + bean.getAdv_duration()));
                }
                break;
            case 1:
                listImg.add(bean);
                break;
            case 2:
            case 3:
                listText.add(bean);
                break;
            default:
                break;
        }
    }

    private int videoPosition = 0;
    public void setlistVideo(int position) {
        if (listVideo != null && listVideo.size() != 0 && position != listVideo.size()){
            videoPlayer.setUp(listVideo.get(position).getUrl(), true, "");
            videoPlayer.startPlayLogic();
            videoPlayer.setLooping(true);
        }else if (listVideo.size() != 0){
            setlistVideo(videoPosition = 0);
        }else {

        }
    }

    private int imgPosition = 0;//文字集合长度
    private boolean isImgPosition1 = false;//是否结束
    private boolean isImgPosition2 = false;//是否结束
    private boolean isImgPosition3 = false;//是否结束
    private void setListImg(int position) { //轮播图默认开始 默认都没开始
        if (listImg != null && listImg.size() != 0){
            if (listImg.size() >= 1){
                imgPosition = 0;
                DataBean bean1 = listImg.get(0);
                setImg(bean1, listImg1, banner1, downTimerImg1, 1);
            }
            if (listImg.size() >= 2){
                imgPosition = 1;
                DataBean bean2 = listImg.get(1);
                setImg(bean2, listImg2, banner2, downTimerImg2, 2);
            }
            if (listImg.size() >= 3){
                imgPosition = 2;
                DataBean bean3 = listImg.get(2);
                setImg(bean3, listImg3, banner3, downTimerImg3, 3);
            }
        }
    }

    private int textPosition = 0;//文字集合长度
    private void setListText(int position){
        if (listText != null && listText.size() != 0) {
            DataBean bean = listText.get(position);
            textPosition += 1;
            if (bean.getType() == 3){
                tvRoll.setVisibility(View.VISIBLE);
                tvFixed.setVisibility(View.GONE);
                tvRoll.setText(bean.getRemark());
                tvRoll.init(act.getWindowManager());
                tvRoll.startScroll();
            }else {
                tvRoll.setVisibility(View.GONE);
                tvFixed.setVisibility(View.VISIBLE);
                tvRoll.init(act.getWindowManager());
                tvRoll.stopScroll();
                tvFixed.setText(bean.getRemark());
            }
            if (downTimerText != null){
                downTimerText.cancel();
            }
            downTimerText = new CountDownTimer((bean.getAdv_duration() + 1) * 1000, 1000) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    if (textPosition != listText.size()){
                        setListText(textPosition);
                    }else {
                        setListText(textPosition = 0);
                    }
                }
            }.start();
        }
    }

    private void setImg(final DataBean bean, List<String> listImg, BGABanner banner,CountDownTimer downTimer, final int type) {
        listImg.clear();
        String image1 = bean.getImage1();
        String image2 = bean.getImage2();
        String image3 = bean.getImage3();
        if (!StringUtils.isEmpty(image1)){
            listImg.add(image1);
        }
        if (!StringUtils.isEmpty(image2)){
            listImg.add(image2);
        }
        if (!StringUtils.isEmpty(image3)){
            listImg.add(image3);
        }
        banner.setAutoPlayAble(listImg.size() > 1);
        banner.setData(listImg, new ArrayList<String>());
        if (downTimer != null){
            downTimer.cancel();
        }
        downTimer = new CountDownTimer((bean.getAdv_duration() + 1) * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                switch (type){
                    case 1:
                        isImgPosition1 = true;
                        setImageTurn(true, false, false, type);
                        break;
                    case 2:
                        isImgPosition2 = true;
                        setImageTurn(false, true, false, type);
                        break;
                    case 3:
                        isImgPosition3 = true;
                        setImageTurn(false, false, true, type);
                        break;
                }

            }
        }.start();
    }

    /**
     *  判断哪个轮播图先完成任务
     */
    private void setImageTurn(boolean b1, boolean b2, boolean b3, int type){
        imgPosition += 1;
//        LogUtils.e(type, imgPosition, listImg.size());
        if (imgPosition <= listImg.size()){
            DataBean bean = listImg.get(imgPosition - 1);
            if (b1){
                isImgPosition1 = false;
                setImg(bean, listImg1, banner1, downTimerImg1, 1);
            }else if (b2){
                isImgPosition2 = false;
                setImg(bean, listImg2, banner2, downTimerImg2, 2);
            }else if (b3){
                isImgPosition3 = false;
                setImg(bean, listImg3, banner3, downTimerImg3, 3);
            }
        }else if (isImgPosition1 && isImgPosition2 && isImgPosition3){
            isImgPosition1 = false;
            isImgPosition2 = false;
            isImgPosition3 = false;
            setListImg(imgPosition = 0);
        }else {
            LogUtils.e("3次到这就重新开始");
        }
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
        itemView.setScaleType(ImageView.ScaleType.FIT_XY);
        GlideImageUtils.load(act, CloudApi.SERVLET_URL + model, itemView, itemView.getDrawable());
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_set:
                UIHelper.startSetPwdFrg(MainFrg.this);
                break;
        }
    }

    /**
     *  准备开始
     * @param url
     * @param objects
     */
    @Override
    public void onStartPrepared(String url, Object... objects) {
//        LogUtils.e("onStartPrepared");
    }

    /**
     *  正在播放
     */
    @Override
    public void onPrepared(String url, Object... objects) {
//        LogUtils.e("onPrepared");
        if (videoDownTimer != null){
            videoDownTimer.cancel();
        }
        final GSYVideoModel bean = listVideo.get(videoPosition);
        videoDownTimer = new CountDownTimer((Integer.valueOf(bean.getTitle()) + 1) * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                LogUtils.e(millisUntilFinished, videoPosition);
//                LogUtils.e((Integer.valueOf(bean.getTitle()) + 1) * 1000, millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                videoPosition += 1;
                setlistVideo(videoPosition);
            }
        }.start();;
    }

    @Override
    public void onClickStartIcon(String url, Object... objects) {
        LogUtils.e("onClickStartIcon");
    }

    @Override
    public void onClickStartError(String url, Object... objects) {
        LogUtils.e("onClickStartError");
    }

    @Override
    public void onClickStop(String url, Object... objects) {
        LogUtils.e("onClickStop");
    }

    @Override
    public void onClickStopFullscreen(String url, Object... objects) {
        LogUtils.e("onClickStopFullscreen");
    }

    @Override
    public void onClickResume(String url, Object... objects) {
        LogUtils.e("onClickResume");
    }

    @Override
    public void onClickResumeFullscreen(String url, Object... objects) {
        LogUtils.e("onClickResumeFullscreen");
    }

    @Override
    public void onClickSeekbar(String url, Object... objects) {
        LogUtils.e("onClickSeekbar");
    }

    @Override
    public void onClickSeekbarFullscreen(String url, Object... objects) {
        LogUtils.e("onClickSeekbarFullscreen");
    }

    /**
     * 全部播放结束
     */
    @Override
    public void onAutoComplete(String url, Object... objects) {
        LogUtils.e("onAutoComplete");
        setlistVideo(0);
    }

    @Override
    public void onEnterFullscreen(String url, Object... objects) {
        LogUtils.e("onEnterFullscreen");
    }

    @Override
    public void onQuitFullscreen(String url, Object... objects) {
        LogUtils.e("onQuitFullscreen");
    }

    @Override
    public void onQuitSmallWidget(String url, Object... objects) {
        LogUtils.e("onQuitSmallWidget");
    }

    @Override
    public void onEnterSmallWidget(String url, Object... objects) {
        LogUtils.e("onEnterSmallWidget");
    }

    @Override
    public void onTouchScreenSeekVolume(String url, Object... objects) {
        LogUtils.e("onTouchScreenSeekVolume");
    }

    @Override
    public void onTouchScreenSeekPosition(String url, Object... objects) {
        LogUtils.e("onTouchScreenSeekPosition");
    }

    @Override
    public void onTouchScreenSeekLight(String url, Object... objects) {
        LogUtils.e("onTouchScreenSeekLight");
    }

    @Override
    public void onPlayError(String url, Object... objects) {
        LogUtils.e("onPlayError");
        showToast("播放失败，请重启");
    }

    /**
     *  点击开始
     * @param url
     * @param objects
     */
    @Override
    public void onClickStartThumb(String url, Object... objects) {
//        LogUtils.e("onClickStartThumb");
    }

    @Override
    public void onClickBlank(String url, Object... objects) {
        LogUtils.e("onClickBlank");
    }

    @Override
    public void onClickBlankFullscreen(String url, Object... objects) {
        LogUtils.e("onClickBlankFullscreen");
    }
}
