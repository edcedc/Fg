package com.fanwang.fgcm.event;

/**
 * Created by edison on 2018/4/12.
 */

public class BundleDataInEvent {

    //1 搜索  2 投放详情图片  3 投放详情视频  4 我的更新资料

    public static final int select_img = 2;//2 投放详情图片
    public static final int select_video = 3;//投放详情视频
    public static final int search_1 = 4;//投广告页面进去
    public static final int search_2 = 5;//选择手机页面进去
    public static final int launch_details = 6;//投放详情
    public static final int launch_time = 7;//选择时间
    public static final int me_update = 8;//我的更新资料
    public static final int mEncloureFabulous = 9;//短视频点赞状态
    public static final int mEncloureRedState = 92;//短视频红包是否消失
    public static final int mEnclourePlay = 91;//短视频播放量
    public static final int mHotPlay = 10;//热门点赞状态改变
    public static final int mHotFabulous = 101;//热门播放量
    public static final int mWeekFabulous = 11;//周排行榜点赞状态改变
    public static final int mMonthFabulous = 12;//月排行榜点赞状态改变
    public static final int mLookMap = 13;//定位成功回调
    public static final int mVideoImg = 14;//选择封面
    public static final int mEquList = 15;//设备列表
    public static final int BankCardListFrg = 16;//银行卡刷新
    public static final int mMyVideo = 17;//我的视频展示状态
    public static final int onTwoStageCommentEvent = 18;//二级评论
    public static final int Encloure_Red = 19;//附近红包被领取
    public static final int Hot_Red = 20;//热门红包被领取
    public static final int Hot_Red_state = 201;//热门红包是否xiaoshi消失
    public static final int OnDemand_pay = 21;//点播支付
    public static final int pay = 22;//支付宝支付成功
    public static final int onPlaybackSuccess = 23;//播放视频成功
    public static final int LookAdvertisementList = 24;//看广告刷新
    public static final int reb_Advertisement = 25;//看广告领取红包
    public static final int demand_zxing = 26;//点播扫描
    public static final int wx_pay = 27;//微信支付回掉
    public static final int weekRanking = 28;//周排行榜
    public static final int weekRankingRed = 281;//周排行榜 红包是否存在
    public static final int weekRankingIsRed = 282;//周排行榜 红包是否领取
    public static final int monthRanking = 29;//月排行榜
    public static final int monthRankingRed = 291;//月排行榜 红包是否存在
    public static final int monthRankingIsRed = 292;//月排行榜 红包是否领取

    private int type;

    private String data1;

    private Object object;

    public BundleDataInEvent(String data1) {
        this.data1 = data1;
    }

    public String getData1() {
        return data1;
    }

    public int getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }

    public BundleDataInEvent(int type) {
        this.type = type;
    }

    public BundleDataInEvent(int type, Object object) {
        this.type = type;
        this.object = object;
    }

}
