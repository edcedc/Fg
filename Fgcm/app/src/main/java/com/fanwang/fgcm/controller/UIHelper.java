package com.fanwang.fgcm.controller;


import android.os.Bundle;

import com.blankj.utilcode.util.ActivityUtils;
import com.fanwang.fgcm.MainActivity;
import com.fanwang.fgcm.SplashAct;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.view.ui.AboutFrg;
import com.fanwang.fgcm.view.ui.AddBankCardFrg;
import com.fanwang.fgcm.view.ui.BankCardListFrg;
import com.fanwang.fgcm.view.ui.BindingWXFrg;
import com.fanwang.fgcm.view.ui.CodeLoginFrg;
import com.fanwang.fgcm.view.ui.ComplaintsReportFrg;
import com.fanwang.fgcm.view.ui.ConsumptionDetailFrg;
import com.fanwang.fgcm.view.ui.CustomerFrg;
import com.fanwang.fgcm.view.ui.DetailedFrg;
import com.fanwang.fgcm.view.ui.DownloadFrg;
import com.fanwang.fgcm.view.ui.EquDetailsFrg;
import com.fanwang.fgcm.view.ui.EquipmentDetailsFrg;
import com.fanwang.fgcm.view.ui.EquipmentListDetailsFrg;
import com.fanwang.fgcm.view.ui.EquipmentListFrg;
import com.fanwang.fgcm.view.ui.EquipmentMapFrg;
import com.fanwang.fgcm.view.ui.EquipmentlFrg;
import com.fanwang.fgcm.view.ui.ForgetPwdFrg;
import com.fanwang.fgcm.view.ui.HtmlFrg;
import com.fanwang.fgcm.view.ui.IntegralRecordFrg;
import com.fanwang.fgcm.view.ui.LaunchTimeFrg;
import com.fanwang.fgcm.view.ui.LoginFrg;
import com.fanwang.fgcm.view.ui.LooKDetailsFrg;
import com.fanwang.fgcm.view.ui.MainFrg;
import com.fanwang.fgcm.view.ui.MeEditFrg;
import com.fanwang.fgcm.view.ui.MeUpdateFrg;
import com.fanwang.fgcm.view.ui.ModifyPasswordFrg;
import com.fanwang.fgcm.view.ui.MoreProblemsFrg;
import com.fanwang.fgcm.view.ui.MyEquFrg;
import com.fanwang.fgcm.view.ui.MyRedEnvelopesFrg;
import com.fanwang.fgcm.view.ui.MyVideoFrg;
import com.fanwang.fgcm.view.ui.NoticePageFrg;
import com.fanwang.fgcm.view.ui.PhoneDetailsFrg;
import com.fanwang.fgcm.view.ui.PlayVideoFrg;
import com.fanwang.fgcm.view.ui.PutForwardFrg;
import com.fanwang.fgcm.view.ui.RankingListFrg;
import com.fanwang.fgcm.view.ui.RechargeFrg;
import com.fanwang.fgcm.view.ui.ReleaseRecordFrg;
import com.fanwang.fgcm.view.ui.ReleaseVideoFrg;
import com.fanwang.fgcm.view.ui.ScreenFrg;
import com.fanwang.fgcm.view.ui.SearchFrg;
import com.fanwang.fgcm.view.ui.SelectAddressFrg;
import com.fanwang.fgcm.view.ui.SetFrg;
import com.fanwang.fgcm.view.ui.SetRedEnvelopesFrg;
import com.fanwang.fgcm.view.ui.VideoDescFrg;
import com.fanwang.fgcm.view.ui.VideoFrametFrg;
import com.fanwang.fgcm.view.ui.VideoFrg;
import com.fanwang.fgcm.view.ui.WalletFrg;
import com.fanwang.fgcm.view.ui.WeChatCaptureActivity;
import com.fanwang.fgcm.weight.toprightmenu.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */

public final class UIHelper {

    private UIHelper() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     *  main
     */
    public static void startMainAct() {
        ActivityUtils.startActivity(MainActivity.class);
    }
    /**
     *  SplashAct
     */
    public static void startSplashAct() {
        ActivityUtils.startActivity(SplashAct.class);
    }

    /**
     * 登陆
     */
    public static void startLoginFrg(BaseFragment root) {
        root.start(LoginFrg.newInstance());
    }

    /**
     * 忘记密码
     */
    public static void startForgetPwdFrg(BaseFragment root) {
        root.start(new ForgetPwdFrg());
    }

    /**
     * 验证码登陆
     */
    public static void startCodeLogin(BaseFragment root) {
        root.start(new CodeLoginFrg());
    }

    /**
     * 纯H5
     */
    public static void startHtmlFrg(BaseFragment root, int type) {
        HtmlFrg frg = new HtmlFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        frg.setArguments(bundle);
        if (type == 0) {
            root.start(frg);
        } else {
            ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
        }
    }
    public static void startHtmlFrg(BaseFragment root, String title, String content, String id) {
        HtmlFrg frg = new HtmlFrg();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putInt("type", 2);
        bundle.putString("id", id);
        frg.setArguments(bundle);
        root.start(frg);
    }
    public static void startHtmlFrg(BaseFragment root, String title, String content, int type) {
        HtmlFrg frg = new HtmlFrg();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putInt("type", type);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 绑定微信
     */
    public static void startBindingWXFrg(BaseFragment root, String wx_openid) {
        BindingWXFrg frg = new BindingWXFrg();
        Bundle bundle = new Bundle();
        bundle.putString("wx_openid", wx_openid);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * main
     */
    public static void startMainFrg(BaseFragment root) {
        root.start(MainFrg.newInstance());
    }

    /**
     * 短视频详情
     *  mstart 页面进去状态
     */
    public static void startViderDescFrg(BaseFragment root, int mStart, String bean, int position) {
        VideoDescFrg frg = new VideoDescFrg();
        Bundle bundle = new Bundle();
        bundle.putString("Bean", bean);
        bundle.putInt("position", position);
        bundle.putInt("EventState", mStart);
        frg.setArguments(bundle);
        if (mStart == BundleDataInEvent.mEncloureFabulous || mStart == BundleDataInEvent.mHotFabulous) {
            ((MainFrg) root.getParentFragment().getParentFragment()).startBrotherFragment(frg);
        }else if (mStart == BundleDataInEvent.mMyVideo){
            root.start(frg);
        }else {
            ((RankingListFrg) root.getParentFragment()).startBrotherFragment(frg);
        }
    }

    /**
     * 排行榜
     *
     * @param videoFrg
     */
    public static void startRankingListFrg(VideoFrg root) {
//        root.start(RankingListFrg.newInstance());
        ((MainFrg) root.getParentFragment()).startBrotherFragment(RankingListFrg.newInstance());
    }

    /**
     * 获取视频帧数   选择封面
     */
    public static void startVideoFrametFrg(VideoFrg root, Bundle bundle) {
        VideoFrametFrg frg = new VideoFrametFrg();
//        Bundle bundle = new Bundle();
//        bundle.putStringArrayList("list", (ArrayList<String>) list);
//        bundle.putString("data", data);
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 选择红包
     */
    public static void startSetRedEnvelopesFrg(BaseFragment root, String img, String data, String text) {
        SetRedEnvelopesFrg frg = new SetRedEnvelopesFrg();
        Bundle bundle = new Bundle();
        bundle.putString("imgUrl", img);
        bundle.putString("videoUrl", data);
        bundle.putString("text", text);
        frg.setArguments(bundle);
        root.start(frg);
//        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 发布视频
     */
    public static void startReleaseVideoFrg(VideoFrg root) {
        ((MainFrg) root.getParentFragment()).startBrotherFragment(new ReleaseVideoFrg());
    }

    /**
     * 我的红包
     */
    public static void startMyRedEnvelopesFrg(BaseFragment root) {
        ((MainFrg) root.getParentFragment()).startBrotherFragment(new MyRedEnvelopesFrg());
    }

    /**
     * 搜索列表
     */
    public static void startSearchFrg(BaseFragment root, int startType) {
        SearchFrg frg = new SearchFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("startType", startType);
        frg.setArguments(bundle);
        if (startType == BundleDataInEvent.search_1) {
            ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
        } else {
            root.start(frg);
        }
    }

    /**
     * 手机投放详情
     */
    public static void startLaunchDetailsFrg(BaseFragment root, int distance) {
        PhoneDetailsFrg frg = new PhoneDetailsFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("distance", distance);
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 选择位置与范围
     */
    public static void startSelectAddressFrg(PhoneDetailsFrg root) {
        SelectAddressFrg frg = new SelectAddressFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 地图选择模式
     *
     * @param root
     * @param mCurrentLat
     * @param mCurrentLon
     */
    public static void startEquipmentMapFrg(BaseFragment root, double mCurrentLat, double mCurrentLon, String location) {
        EquipmentMapFrg frg = new EquipmentMapFrg();
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", mCurrentLat);
        bundle.putDouble("longitude", mCurrentLon);
        bundle.putString("location", location);
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 选择设备列表模式   终端设备
     */
    public static void startEquipmentListFrg(BaseFragment root, double mCurrentLat, double mCurrentLon, String location) {
        EquipmentListFrg frg = new EquipmentListFrg();
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", mCurrentLat);
        bundle.putDouble("longitude", mCurrentLon);
        bundle.putString("location", location);
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 筛选
     */
    public static void startScreenFrg(List<MenuItem> modelItems, BaseFragment root) {
        Type type = new TypeToken<ArrayList<MenuItem>>() {}.getType();
        String json = new Gson().toJson(modelItems, type);
        ScreenFrg frg = new ScreenFrg();
        Bundle bundle = new Bundle();
        bundle.putString("list", json);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 地图 列表 投放详情
     */
    public static void startEquipmentListDetailsFrg(BaseFragment root, List<DataBean> listBean, String location, String mModelId, double latitude, double longitude) {
        EquipmentListDetailsFrg frg = new EquipmentListDetailsFrg();
        Type type = new TypeToken<ArrayList<DataBean>>() {}.getType();
        String json = new Gson().toJson(listBean, type);
        Bundle bundle = new Bundle();
        bundle.putString("list", json);
        bundle.putString("location", location);
        bundle.putString("modelId", mModelId);
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        frg.setArguments(bundle);
        root.start(frg);
    }


    /**
     * 地图   投放详情
     *  @param act
     * @param name
     * @param model
     * @param address
     * @param longitude
     * @param latitude
     * @param id
     */
    public static void startEquipmentDetailsFrg(BaseFragment root, String name, String eqpIds, String model, String address,
                                                double longitude, double latitude, String id, String model_labelId) {
        EquipmentDetailsFrg frg = new EquipmentDetailsFrg();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("eqpIds", eqpIds);
        bundle.putString("model", model);
        bundle.putString("address", address);
        bundle.putString("id", id);
        bundle.putString("model_labelId", model_labelId);
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 投放时间
     *
     * @param equipmentDetailsFrg
     */
    public static void startLaunchTimeFrg(BaseFragment root) {
        LaunchTimeFrg frg = new LaunchTimeFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 钱包
     */
    public static void startWalletFrg(BaseFragment root) {
        WalletFrg frg = new WalletFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 投放记录
     */
    public static void startReleaseRecordFrg(BaseFragment root) {
        ReleaseRecordFrg frg = new ReleaseRecordFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 我的设备
     */
    public static void startMyEquFrg(BaseFragment root) {
        MyEquFrg frg = new MyEquFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 积分记录
     */
    public static void startIntegralRecordFrg(BaseFragment root) {
        IntegralRecordFrg frg = new IntegralRecordFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }
    /**
     * 客服中心
     */
    public static void startCustomerFrg(BaseFragment root) {
        CustomerFrg frg = new CustomerFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 关于我们
     */
    public static void startAboutFrg(BaseFragment root) {
        AboutFrg frg = new AboutFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 下载
     */
    public static void startDownloadFrg(BaseFragment root) {
        DownloadFrg frg = new DownloadFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 设置
     */
    public static void startSetFrg(BaseFragment root) {
        SetFrg frg = new SetFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 我的视频作品
     */
    public static void startMyVideoFrg(BaseFragment root) {
        MyVideoFrg frg = new MyVideoFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 我的
     */
    public static void startMeUpdateFrg(BaseFragment root) {
        MeUpdateFrg frg = new MeUpdateFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 我修改
     */
    public static void startMeEditFrg(BaseFragment root, int type, String data) {
        MeEditFrg frg = new MeEditFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("data", data);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 充值
     */
    public static void startAdvertisingFrg(BaseFragment root, int type) {
        RechargeFrg frg = new RechargeFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        frg.setArguments(bundle);
        root.start(frg);
    }
    /**
     * 修改密码
     */
    public static void startModifyPasswordFrg(BaseFragment root) {
        ModifyPasswordFrg frg = new ModifyPasswordFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 播放视频
     */
    public static void startPlayVideoFrg(BaseFragment root, String path) {
        PlayVideoFrg frg = new PlayVideoFrg();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 提现
     */
    public static void startPutForwardFrg(BaseFragment root) {
        PutForwardFrg frg = new PutForwardFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }


    /**
     * 我的银行卡
     */
    public static void startBankCardListFrg(BaseFragment root) {
        BankCardListFrg frg = new BankCardListFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 消息
     */
    public static void startNoticePageFrg(BaseFragment root) {
        NoticePageFrg frg = new NoticePageFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 新增银行卡
     */
    public static void startAddBankCardFrg(BaseFragment root) {
        AddBankCardFrg frg = new AddBankCardFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 明细
     */
    public static void startDetailedFrg(BaseFragment root, int type) {
        DetailedFrg frg = new DetailedFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 消费明细
     */
    public static void startConsumptionDetailFrg(BaseFragment root) {
        ConsumptionDetailFrg frg = new ConsumptionDetailFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 手机  终端   设备  列表
     */
    public static void startEquipmentlFrg(BaseFragment root, int type) {
        EquipmentlFrg frg = new EquipmentlFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        frg.setArguments(bundle);
        root.start(frg);
    }

    public static void startEquipmentlFrg(BaseFragment root, int type, String equId) {
        EquipmentlFrg frg = new EquipmentlFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("equId", equId);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  投诉举报
     */
    public static void startComplaintsReportFrg(BaseFragment root) {
        ComplaintsReportFrg frg = new ComplaintsReportFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }
    public static void startComplaintsReportFrg(BaseFragment root, String id) {
        ComplaintsReportFrg frg = new ComplaintsReportFrg();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  11.1.1客服中心列表  接口(好)
     */
    public static void startMoreProblemsFrg(BaseFragment root) {
        MoreProblemsFrg frg = new MoreProblemsFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     *  二维码扫描
     */
    public static void startZxingAct(){
        ActivityUtils.startActivity(WeChatCaptureActivity.class);
    }

    /**
     *  广告详情
     */
    public static void startEquDetailsFrg(BaseFragment root, DataBean bean){
        EquDetailsFrg frg = new EquDetailsFrg();
        Bundle bundle = new Bundle();
        bundle.putString("Bean", new Gson().toJson(bean));
        frg.setArguments(bundle);
        ((EquipmentlFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     *  看广告详情
     * @param root
     * @param bean
     */
    public static void startLooKDetailsFrg(BaseFragment root, DataBean bean, int type) {
        LooKDetailsFrg frg = new LooKDetailsFrg();
        Bundle bundle = new Bundle();
        bundle.putString("bean", new Gson().toJson(bean));
        frg.setArguments(bundle);
        if (type == 0){
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
        } else {
            root.start(frg);
        }
    }
}