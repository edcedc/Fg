package com.fanwang.fgcm.controller;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Formatter;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgcm.bean.BaseListBean;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.callback.JsonCallback;
import com.fanwang.fgcm.callback.JsonConvert;
import com.fanwang.fgcm.callback.NewsCallback;
import com.fanwang.fgcm.presenter.listener.OnEquipmentDetailsListener;
import com.fanwang.fgcm.presenter.listener.OnPhoneDetailsListener;
import com.fanwang.fgcm.utils.BaseUtils;
import com.fanwang.fgcm.utils.Constants;
import com.fanwang.fgcm.utils.SharedAccount;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okrx2.adapter.ObservableBody;
import com.lzy.okrx2.adapter.ObservableResponse;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CloudApi {


    private static final String url =
//                    "10.0.0.199:8080/wjcm"
            "www.feigemedia.com"
                ;
    //
    public static final String SERVLET_URL = "http://" +
            url + "/api/";

    public static final String TEST_URL = ""; //测试

    private static final String TAG = "CloudApi";

    private CloudApi() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     *  H5
     */
    public static final String H5Url = CloudApi.SERVLET_URL + "wap/information/get?id=";

    /**
     *  百度搜索列表
     */
    private static final String searchBaidu = "http://api.map.baidu.com/place/v2/suggestion";

    public static Observable<String> searchBaidu(String s){
        Bundle bundle = User.getInstance().getAddressBundle();
        return OkGo.<String>get(CloudApi.searchBaidu)
                .params("query", s)
                .params("region", "全国")
                .params("city_limit", false)
                .params("output", "json")
                .params("ak", Constants.baidu_service_ak)
                .converter(new JsonConvert<String>() {})
                .adapt(new ObservableBody<String>())
                .subscribeOn(Schedulers.io());
    }


    /**
     *  3.1.1获取验证码注册 接口(好)
     */
    public static Observable<Response<BaseResponseBean>> userGetRegisterCode(String phone){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "user/getRegisterCode")
                .params("mobile", phone)
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }

     /**
     * 3.1.12移动设备推送注册  接口(好)
     */
    public static Observable<Response<BaseResponseBean>> userMobileDevices(String clientId, String alias){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "user/mobileDevices")
                .params("clientId", clientId)
                .params("alias", alias)
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  3.1.16获取绑定手机验证码  接口(好)
     */
    public static Observable<Response<BaseResponseBean>> userGetBindingCode(String phone){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "user/getBindingCode")
                .params("mobile", phone)
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  注册
     */
    public static Observable<Response<BaseResponseBean>> userRegister(String mobile, String code, String password, String repeatPassword){
        Bundle bundle = User.getInstance().getAddressBundle();
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "user/register")
                .params("mobile", mobile)
                .params("code", code)
                .params("password", password)
                .params("repeatPassword", repeatPassword)
                .params("province", bundle.getString("province"))
                .params("city", bundle.getString("city"))
                .params("area", bundle.getString("district"))
                .params("latitude", bundle.getDouble("latitude"))
                .params("longitude", bundle.getDouble("longitude"))
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }
    /**
     *  3.1.17绑定手机号码  接口(好)
     */
    public static Observable<Response<BaseResponseBean>> userBindingobile(String mobile, String code, String sessionId){
        Bundle bundle = User.getInstance().getAddressBundle();
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "user/bindingobile")
                .params("mobile", mobile)
                .params("code", code)
                .params("sessionId", sessionId)
//                .params("province", bundle.getString("province"))
//                .params("city", bundle.getString("city"))
//                .params("area", bundle.getString("district"))
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  登陆
     */
    public static Observable<JSONObject> userLogin(String mobile, String pwd){
        return OkGo.<JSONObject>post(SERVLET_URL + "user/login")
                .params("mobile", mobile)
                .params("password", pwd)
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }


    /**
     *  获取腾讯云sig
     */
    public static Observable<JSONObject> signatureGet(){
        return OkGo.<JSONObject>post(SERVLET_URL + "app/signature/get")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  3.1.6获取登陆验证码 接口(好)
     */
    public static Observable<Response<BaseResponseBean>> userGetCodeLogin(String phone){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "user/getCodeLogin")
                .params("mobile", phone)
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  3.1.7验证码登陆 接口(好)
     */
    public static Observable<JSONObject> userCodeLogin(String mobile, String pwd){
        return OkGo.<JSONObject>post(SERVLET_URL + "user/codeLogin")
                .params("mobile", mobile)
                .params("code", pwd)
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  获取微信登陆返回值
     */
    public static Observable<JSONObject> userMicroLetterLogin(String openid, String access_token){
        return OkGo.<JSONObject>post(SERVLET_URL + "user/microLetterLogin")
                .params("access_token", access_token)
                .params("openid", openid)
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  获取微信登陆返回值
     */
    public static Observable<JSONObject> wxLogin(String openid, String access_token){
        return OkGo.<JSONObject>get("https://api.weixin.qq.com/sns/userinfo")
                .params("access_token", access_token)
                .params("openid", openid)
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }



    /**
     *  获取微信登陆返回值
     */
    public static Observable<JSONObject> wxAccess_token(String code ){
        return OkGo.<JSONObject>post("https://api.weixin.qq.com/sns/oauth2/access_token")
                .params("appid", Constants.WX_APPID)
                .params("secret", Constants.WX_SECRER)
                .params("code", code)
                .params("grant_type", "authorization_code")
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  3.1.4获取忘记密码 验证码接口(好)
     */
    public static Observable<Response<BaseResponseBean>> userGetForgetCode(String phone){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "user/getForgetCode")
                .params("mobile", phone)
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }
    /**
     *  3.1.15微信登陆  接口(好)
     */
    public static Observable<JSONObject> userWeChatId(String head, String nick, String weChatId, int sex){
//        return OkGo.<BaseResponseBean>post(SERVLET_URL + "user/weChatId")
        return OkGo.<JSONObject>post(SERVLET_URL + "user/microLetterLogin")
                .params("head", head)
                .params("nick", nick)
                .params("weChatId", weChatId)
                .params("sex", sex)
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  3.1.5忘记密码 接口(新)
     */
    public static Observable<Response<BaseResponseBean>> userUpdateForgetPassword(String phone,String code,String password,String repeatPassword){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "user/updateForgetPassword")
                .params("mobile", phone)
                .params("code", code)
                .params("password", password)
                .params("repeatPassword", repeatPassword)
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  3.1.11注册协议 接口(好)
     */
    public static final String informationAgreement = "information/agreement";

    /**
     *  5.1.6广告任务 接口(好)
     */
    public static final String informationAdvertisingTasks = "information/advertisingTasks";

    /**
     *  获取H5
     */
    public static Observable<BaseResponseBean<DataBean>> getH5(String url){
        return OkGo.<BaseResponseBean<DataBean>>get(SERVLET_URL + url)
                .converter(new JsonConvert<BaseResponseBean<DataBean>>() {})
                .adapt(new ObservableBody<BaseResponseBean<DataBean>>());
    }

    /**
     * 4.1.3评论列表  接口(好)
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> commentPageAWC(int pageNumber,String videoId){

        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "sv/video/comment/pageAWC")
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("videoId", videoId)
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 10.1.3点播支付  接口(好)
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> videosPage(int pageNumber,String Id){
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "videos/page")
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("28", Id)
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 发布评论 接口(好)
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> commentSaveAWC(String content, String videoId){
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "sv/video/comment/saveAWC")
                .params("content", content)
                .params("emojiContent", BaseUtils.encode(content))
                .params("emoji_content", BaseUtils.encode(content))
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("videoId", videoId)
                .converter(new JsonConvert<BaseResponseBean<DataBean>>() {})
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }
    /**
     * 4.1.8发布回复评论 接口(好)
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> svVideoCommentSaveAWCLevel(String videoId, String videoCommentId,
                                                                                              String byReplyUserId, String content){
        PostRequest<BaseResponseBean<DataBean>> post = OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "sv/video/comment/saveAWCLevel");
        if (!StringUtils.isEmpty(byReplyUserId)){
            post.params("byReplyUserId", byReplyUserId);
        }
        return post
                .params("content", content)
                .params("emojiContent", BaseUtils.encode(content))
                .params("emoji_content", BaseUtils.encode(content))
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("videoId", videoId)
                .params("replyUserId", User.getInstance().getUserId())
                .params("videoCommentId", videoCommentId)
                .converter(new JsonConvert<BaseResponseBean<DataBean>>() {})
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     *  4.1.6端视频 用户添加赞/踩 ,并且 赞/踩 互换 /  取消 赞/踩 接口(好)
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> savePraise(int state,String videoId){
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "sv/video/savePraise")
                .params("state", state)
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("videoId", videoId)
                .converter(new JsonConvert<BaseResponseBean<DataBean>>() {})
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 4.1.14	点击播放量 接口(好)
     */
    public static Observable<Response<BaseResponseBean>> svVideoAddPlayback(String videoId){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "sv/video/addPlayback")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("videoId", videoId)
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  4.1.9评论 用户添加赞/踩 ,并且 赞/踩 互换 /  取消 赞/踩 接口(好)
     */
    public static Observable<Response<BaseResponseBean>> commentSavePraise(int state,String videoId){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "sv/video/comment/savePraise")
                .params("state", state)
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("videoCommentId", videoId)
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }


    /**
     *  4.1.1短视频附近列表  接口(好)
     */
    public static void svVideoPageNear(int pageNumber, Observer<BaseResponseBean<BaseListBean<DataBean>>> observer){
        Bundle bundle = User.getInstance().getAddressBundle();
        if (bundle == null)return;
        OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "sv/video/pageNear")
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("latitude", bundle.getDouble("latitude"))
                .params("longitude", bundle.getDouble("longitude"))
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
//                .cacheMode(pageNumber == 1 ? CacheMode.REQUEST_FAILED_READ_CACHE : CacheMode.NO_CACHE)
                .converter(new JsonConvert<BaseResponseBean<BaseListBean<DataBean>>>() {})
                .adapt(new ObservableBody<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                    }
                })
                .map(new Function<BaseResponseBean<BaseListBean<DataBean>>, BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public BaseResponseBean<BaseListBean<DataBean>> apply(BaseResponseBean<BaseListBean<DataBean>> baseResponseBean) throws Exception {
                        return baseResponseBean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 4.1.10周赞榜列表 接口(好)
     */
    public static final String videoPageWeeks = "sv/video/pageWeeks";

    /**
     * 4.1.11月赞榜列表 接口(好)
     */
    public static final String videoPageMonth = "sv/video/pageMonth";

    /**
     * 4.1.2短视频热门列表  接口(好)
     */
    public static final String videoPageHot = "sv/video/pageHot";

    /**
     * 5.1.4我的领取红包记录 接口(好)
     */
    public static final String pageReceiveRedEnvelopes = "adv/pageReceiveRedEnvelopes";
    /**
     * 10.1.4消息列表  接口(好)
     */
    public static final String noticePage = "notice/page";

    /**
     * 10.1.2点播列表  接口(好)
     */
    public static final String videosPage = "videos/page";

    /**
     * 7.1.5银行卡列表  接口(好)
     */
    public static final String userBankCardPage = "user/bank/card/page";

    /**
     * 7.1.9充值记录列表  接口(好)
     */
    public static final String topupPageBalance = "topup/pageBalance";

    /**
     * 7.1.10红包记录列表  接口(好)
     */
    public static final String topupPageRedEnvelope = "topup/pageRedEnvelope";

    /**
     * 7.1.12	用户消费记录列表  接口(好)
     */
    public static final String consumptionPageC = "consumption/pageC";

    /**
     * 7.1.11提现列表  接口(好)
     */
    public static final String withdrawalPageWD = "withdrawal/pageWD";

    /**
     * 10.1.1我的视频作品列表  接口(好)
     */
    public static final String svVideoPageUser = "sv/video/pageUser";

    /**
     * 10.1.5积分记录列表  接口(好)
     */
    public static final String integralPage = "integral/page";

    /**
     * 11.1.1客服中心列表  接口(好)
     */
    public static final String informationPageIssue = "information/pageIssue";


    /**
     *  通用list数据
     * @param pageNumber
     * @param url
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> videoPageWeeks(int pageNumber,String url){
        String sessionId = SharedAccount.getInstance(Utils.getApp()).getSessionId();
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + url)
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *   4.1.12领取红包 接口(好)
     * @param videoId
     * @return
     */
    public static Observable<JSONObject> videoReceiveRedEnvelopes(String videoId){
        return OkGo.<JSONObject>post(SERVLET_URL + "sv/video/receiveRedEnvelopes")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("videoId", videoId)
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *   4.1.12领取红包 接口(好)  看廣告guang广告
     * @return
     */
    public static Observable<JSONObject> advReceiveRedEnvelopes(String advId){
        return OkGo.<JSONObject>post(SERVLET_URL + "adv/receiveRedEnvelopes")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("advId", advId)
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *   5.1.6	广告任务 接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> advertisingTasks() {
        return OkGo.<BaseResponseBean<DataBean>>get(SERVLET_URL + "information/advertisingTasks")
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  5.1.1看广告列表  接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> advPageADV(){
        Bundle bundle = User.getInstance().getAddressBundle();
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "adv/pageADV")
                .params("pageNumber", 1)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("lat", bundle.getDouble("latitude"))
                .params("lon", bundle.getDouble("longitude"))
                .params("province", bundle.getString("province"))
                .params("city", bundle.getString("city"))
                .params("area", bundle.getString("district"))
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }




    /**
     *  4.1.5发布视频 接口(好)  免费视频
     */
    public static void videoSave(
            final String url, final String videoUrl, final String imgUrl, final int redEnvelopesState,
            final int redEnvelopesNumber, final double redEnvelopes, final int payType, final String videoDesc, final Context act, final OnVideoSaveListener listener){
        Bundle bundle = User.getInstance().getAddressBundle();
        LogUtils.e(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
        Observable.create(new ObservableOnSubscribe<Progress>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Progress> e) throws Exception {
                Bundle bundle = User.getInstance().getAddressBundle();
                OkGo.<JSONObject> post(CloudApi.SERVLET_URL + "sv/video/save")
                        .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                        .params("video", videoUrl)
                        .params("coverImage", new File(imgUrl))
                        .params("redEnvelopesState", redEnvelopesState)
                        .params("redEnvelopesNumber", String.valueOf(redEnvelopesState == 0 ? "" : redEnvelopesNumber))
                        .params("redEnvelopes", String.valueOf(redEnvelopesState == 0 ? "" : redEnvelopes))
                        .params("payType", String.valueOf(redEnvelopesState == 0 ? "" : payType))
                        .params("videoDesc", videoDesc)
                        .params("content", videoDesc)
                        .params("emojiContent", BaseUtils.encode(videoDesc))
                        .params("lat", bundle.getDouble("latitude"))
                        .params("lon", bundle.getDouble("longitude"))
                        .params("province", bundle.getString("province"))
                        .params("city", bundle.getString("city"))
                        .params("area", bundle.getString("district"))
                        .params("address", bundle.getString("street") + bundle.getString("streetNumber"))
                        .params("url", url)
                        .execute(new JsonCallback<JSONObject>() {
                            @Override
                            public void onSuccess(Response<JSONObject> response) {
                                if (response.body().optInt("code") == Code.CODE_SUCCESS){
                                    String data = response.body().optString("data");
                                    if (redEnvelopesState == 0){
                                        listener.onComplete();
                                    }else {
                                        listener.onComplete2(data);
                                    }
                                }
                                ToastUtils.showShort(response.body().optString("desc"));
                                e.onComplete();
                            }

                            @Override
                            public void onError(Response<JSONObject> response) {
                                super.onError(response);
                                e.onError(response.getException());
                            }

                            @Override
                            public void uploadProgress(Progress progress) {
                                e.onNext(progress);
                            }

                        });
            }
        })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        LogUtils.e("正在上传中...");
                        listener.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<Progress>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        listener.addDisposable(d);
                    }

                    @Override
                    public void onNext(@NonNull Progress progress) {
//                                    LogUtils.e("uploadProgress: " + progress);
                        String downloadLength = Formatter.formatFileSize(act, progress.currentSize);
                        String totalLength = Formatter.formatFileSize(act, progress.totalSize);
                        String speed = Formatter.formatFileSize(act, progress.speed);
                        LogUtils.e(downloadLength + "/" + totalLength, String.format("%s/s", speed));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        listener.hideLoading();
                        LogUtils.e("上传出错", e.getMessage());
                        ToastUtils.showShort("网络中断，请重新上传");
                    }

                    @Override
                    public void onComplete() {
                        listener.hideLoading();
                        LogUtils.e("上传完成");
                    }

                });
        /*return OkGo.<JSONObject> post(SERVLET_URL + "sv/video/save")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("video", new File(videoUrl))
                .params("coverImage", new File(imgUrl))
                .params("redEnvelopesState", redEnvelopesState)
                .params("redEnvelopesNumber", String.valueOf(redEnvelopesState == 0 ? "" : redEnvelopesNumber))
                .params("redEnvelopes", String.valueOf(redEnvelopesState == 0 ? "" : redEnvelopes))
                .params("payType", String.valueOf(redEnvelopesState == 0 ? "" : payType))
                .params("videoDesc", videoDesc)
                .params("content", videoDesc)
                .params("emojiContent", BaseUtils.encode(videoDesc))
                .params("lat", bundle.getDouble("latitude"))
                .params("lon", bundle.getDouble("longitude"))
                .params("province", bundle.getString("province"))
                .params("city", bundle.getString("city"))
                .params("area", bundle.getString("district"))
                .params("address", bundle.getString("street") + bundle.getString("streetNumber"))
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());*/
    }

    public interface OnVideoSaveListener{
        void addDisposable(Disposable d);
        void hideLoading();
        void showLoading();
        void onComplete();
        void onComplete2(String data);
    }


    /*public static void videoSave(Context act, String videoUrl, final String imgUrl, final int redEnvelopesState,
                                 final int redEnvelopesNumber, final double redEnvelopes, final int payType,final String videoDesc,
                                 final OnVideoSaveListener listener){
        if (FileUtils.createOrExistsDir(Constants.mainPath)) {
            CloudApi.videoSave(videoUrl, imgUrl, redEnvelopesState, redEnvelopesNumber, redEnvelopes ,payType, videoDesc)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            listener.showLoading();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JSONObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            listener.onSubscribe(d);
                        }

                        @Override
                        public void onNext(JSONObject jsonObject) {
                            listener.onNext(jsonObject);
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.showShort(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            listener.onComplete();
                        }
                    });

            *//*final ProgressDialog dialog = new ProgressDialog(act);
            VideoCompressor.compress(act, videoUrl, new VideoCompressListener() {
                @Override
                public void onSuccess(final String outputFile, String filename, long duration) {
                    Worker.postMain(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e(outputFile);
//                            dialog.dismiss();
                            dialog.setMessage("请求网络中...");
                            CloudApi.videoSave(outputFile, imgUrl, redEnvelopesState, redEnvelopesNumber, redEnvelopes ,payType, videoDesc)
                                    .doOnSubscribe(new Consumer<Disposable>() {
                                        @Override
                                        public void accept(Disposable disposable) throws Exception {

                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<JSONObject>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            listener.onSubscribe(d);
                                        }

                                        @Override
                                        public void onNext(JSONObject jsonObject) {
                                            listener.onNext(jsonObject);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            ToastUtils.showShort(e.getMessage());
                                            dialog.dismiss();
                                            dialog.cancel();
                                        }

                                        @Override
                                        public void onComplete() {
                                            dialog.dismiss();
                                            dialog.cancel();
                                        }
                                    });
                        }
                    });
                }

                @Override
                public void onFail(final String reason) {
                    Worker.postMain(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e(reason);
                            ToastUtils.showShort(reason);
//                            dialog.dismiss();
                        }
                    });
                }

                @Override
                public void onProgress(final int progress) {
                    Worker.postMain(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e(progress +"%");
                            dialog.setMessage("正在处理视频" + progress +"%");
                            dialog.show();
                        }
                    });
                }
            });*//*
        }
    }*/



    /**
     *  投放广告
     * @param act
     * @param mSelectType
     * @param s
     * @param localMediaList
     * @param number
     * @param individual
     * @param range
     * @param mlongitude
     * @param mLatitude
     * @param mProvince
     * @param mCity
     * @param mDistrict
     * @param mRange
     * @param redEnvelopesState
     * @param videoUrl
     */
    public static void advSave(String url, Context act, int mSelectType, String s, List<LocalMedia> localMediaList, String number, String individual, String range,
                               double mlongitude, double mLatitude, String mProvince, String mCity, String mDistrict, int mRange,
                               final int redEnvelopesState, final int payType, String videoUrl, final Observer<JSONObject> observer,
                               final OnPhoneDetailsListener listener){
        final PostRequest<JSONObject> post = OkGo.<JSONObject>post(SERVLET_URL + "adv/save")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("type", mSelectType)
                .params("redEnvelopesState", redEnvelopesState)
                .params("redEnvelopesNumber", individual)
                .params("redEnvelopes", number)
                .params("lat", mLatitude)
                .params("lon", mlongitude)
                .params("province", mProvince)
                .params("city", mCity)
                .params("area", mDistrict)
                .params("payType", payType)
                .params("scopeType", mRange)
                .params("url", url)
                ;
        if (mSelectType == 2 || mSelectType == 3){
            post.params("remark", s);
        }else if (mSelectType == 1){
            for (int i = 0;i < localMediaList.size();i++){
                LocalMedia media = localMediaList.get(i);
                post.params("image" + (i + 1), new File(media.getCompressPath()));
            }
        }else {
            post.params("video", videoUrl);
            post.converter(new JsonConvert<JSONObject>() {})
                    .adapt(new ObservableBody<JSONObject>())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(@NonNull Disposable disposable) throws Exception {
                            listener.showLoading();
                        }
                    })
                    .map(new Function<JSONObject, JSONObject>() {
                        @Override
                        public JSONObject apply(JSONObject baseResponseBean) throws Exception {
                            return baseResponseBean;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
            /*VideoCompressor.compress(act, localMediaList.get(0).getPath(), new VideoCompressListener() {
                @Override
                public void onSuccess(final String outputFile, String filename, long duration) {
                    Worker.postMain(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e(outputFile);
                            dialog.dismiss();
                            post.params("video", new File(outputFile));
                            post.converter(new JsonConvert<JSONObject>() {})
                                    .adapt(new ObservableBody<JSONObject>())
                                    .subscribeOn(Schedulers.io())
                                    .doOnSubscribe(new Consumer<Disposable>() {
                                        @Override
                                        public void accept(@NonNull Disposable disposable) throws Exception {
                                            listener.showLoading();
                                        }
                                    })
                                    .map(new Function<JSONObject, JSONObject>() {
                                        @Override
                                        public JSONObject apply(JSONObject baseResponseBean) throws Exception {
                                            return baseResponseBean;
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(observer);
                        }
                    });
                }

                @Override
                public void onFail(final String reason) {
                    Worker.postMain(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e(reason);
                            ToastUtils.showShort(reason);
                            dialog.dismiss();
                        }
                    });
                }

                @Override
                public void onProgress(final int progress) {
                    Worker.postMain(new Runnable() {
                        @Override
                        public void run() {
//                                LogUtils.e(progress +"%");
                            dialog.setMessage("正在处理视频" + progress +"%");
                            dialog.show();
                        }
                    });
                }
            });*/
            return;
        }
        post.converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.showLoading();
                    }
                })
                .map(new Function<JSONObject, JSONObject>() {
                    @Override
                    public JSONObject apply(JSONObject baseResponseBean) throws Exception {
                        return baseResponseBean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     *  6.1.4附近设备列表 接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> eqpListScope(double latitude, double longitude){
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "eqp/listScope")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("latitude", latitude)
                .params("longitude", longitude)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  3.1.18	引导页列表  接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> guidanceList(){
        return OkGo.<BaseResponseBean<List<DataBean>>>get(SERVLET_URL + "app/guidance/list")
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  根据秒数计算设备价格
     * @param modelLabelId
     * @return
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> eqpGetEqpSystemPrice(String modelLabelId){
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "eqp/getEqpSystemPrice")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("modelLabelId", modelLabelId)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  6.1.6购买设备广告时间  接口(好)
     */
    public static void eqpSave(Context act, int mSelectType, List<LocalMedia> localMediaList, String seconds, String launchTime, String endTime,
                               String eqpId, final String remark, int payType,
                               double latitude, double longitude, boolean isMore,
                               String videoUrl, final OnEquipmentDetailsListener listener){
        String url = "eqp/saveBatch";
//        if (isMore){
//            url = "eqp/saveBatch";
//        }else {
//            url = "eqp/save";
//        }
        final ProgressDialog dialog = new ProgressDialog(act);
        final PostRequest<BaseResponseBean> post = OkGo.<BaseResponseBean>post(SERVLET_URL + url)
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("seconds", seconds)
                .params("launchTime", launchTime)
                .params("type", mSelectType)
                .params("endTime", endTime)
//                .params("payType", payType);
                .params("arrayEqpId", eqpId);
//        if (isMore){
//            post.params("arrayEqpId", eqpId);
//        }else {
//            post.params("eqpId", eqpId);
//        }
//                .params("latitude", latitude)
//                .params("longitude", longitude);
        if (mSelectType == 2 || mSelectType == 3){
            post.params("remark", remark);
        }else if (mSelectType == 1){
            for (int i = 0;i < localMediaList.size();i++){
                LocalMedia media = localMediaList.get(i);
                post.params("image" + (i + 1), new File(media.getCompressPath()));
            }
        }else {
            post.params("video", videoUrl);
            post.converter(new JsonConvert<BaseResponseBean>() {})
                    .adapt(new ObservableBody<BaseResponseBean>())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(@NonNull Disposable disposable) throws Exception {
                            listener.showLoading();
                        }
                    })
                    .map(new Function<BaseResponseBean, BaseResponseBean>() {
                        @Override
                        public BaseResponseBean apply(BaseResponseBean baseResponseBean) throws Exception {
                            return baseResponseBean;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BaseResponseBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            listener.onAddDisposable(d);
                        }

                        @Override
                        public void onNext(BaseResponseBean responseBean) {
                            listener.onSuccessEqpSave(responseBean);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            listener.hideLoading();
                        }
                    });
            /*VideoCompressor.compress(act, localMediaList.get(0).getPath(), new VideoCompressListener() {
                @Override
                public void onSuccess(final String outputFile, String filename, long duration) {

                    dialog.setMessage("请求网络中...");
                    Worker.postMain(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e(outputFile);
                            post.params("video", new File(outputFile));
                            post.converter(new JsonConvert<BaseResponseBean>() {})
                                    .adapt(new ObservableBody<BaseResponseBean>())
                                    .subscribeOn(Schedulers.io())
                                    .doOnSubscribe(new Consumer<Disposable>() {
                                        @Override
                                        public void accept(@NonNull Disposable disposable) throws Exception {

                                        }
                                    })
                                    .map(new Function<BaseResponseBean, BaseResponseBean>() {
                                        @Override
                                        public BaseResponseBean apply(BaseResponseBean baseResponseBean) throws Exception {
                                            return baseResponseBean;
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<BaseResponseBean>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            listener.onAddDisposable(d);
                                        }

                                        @Override
                                        public void onNext(BaseResponseBean responseBean) {
                                            listener.onSuccessEqpSave(responseBean);
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {
                                            dialog.dismiss();
                                            dialog.cancel();
                                        }
                                    });
                        }
                    });
                }

                @Override
                public void onFail(final String reason) {
                    Worker.postMain(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e(reason);
                            ToastUtils.showShort(reason);
                            dialog.dismiss();
                        }
                    });
                }

                @Override
                public void onProgress(final int progress) {
                    Worker.postMain(new Runnable() {
                        @Override
                        public void run() {
//                                LogUtils.e(progress +"%");
                            dialog.setMessage("正在处理视频" + progress +"%");
                            dialog.show();
                        }
                    });
                }
            });*/
            return;
        }
        dialog.setMessage("请求网络中...");
        post.converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableBody<BaseResponseBean>())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.showLoading();
                    }
                })
                .map(new Function<BaseResponseBean, BaseResponseBean>() {
                    @Override
                    public BaseResponseBean apply(BaseResponseBean baseResponseBean) throws Exception {
                        return baseResponseBean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onAddDisposable(d);
                    }

                    @Override
                    public void onNext(BaseResponseBean responseBean) {
                        listener.onSuccessEqpSave(responseBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        listener.hideLoading();
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
    }

    /**
     *  6.1.1设备型号列表 接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> labelListModelLabelId(){
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "label/listModelLabelId")
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());

    }
    /**
     *  6.1.2省市区列表 接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> regionListProvince(){
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "region/listProvince")
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());

    }
    /**
     *  6.1.3下级区域列表 接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> regionListLevel(String regionId){
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "region/listLevel")
                .params("regionId", regionId)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());

    }

    /**
     *  搜索设备列表 接口(好)
     * @param regionId
     * @param scope
     * @param latitude
     * @param longitude
     * @param pageNumber
     * @return
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> eqpSearch(String regionId, int scope, double latitude, double longitude, int pageNumber){
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "eqp/search")
                .params("regionId", regionId)
                .params("scope", (scope + 1) + "000")
                .params("latitude", latitude)
                .params("longitude", longitude)
                    .params("pageNumber", pageNumber)
                    .params("pageSize", Constants.pageSize)
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
//                .cacheMode(pageNumber == 1 ? CacheMode.REQUEST_FAILED_READ_CACHE : CacheMode.NO_CACHE)
                .converter(new JsonConvert<BaseResponseBean<BaseListBean<DataBean>>>() {})
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());

    }


    /**
     * 6.1.5	终端设备列表 接口(好)
     * @param regionId
     * @param provinceArray
     * @param cityArray
     * @param areaArray
     * @param pageNumber
     * @return
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> eqpPageTerminalEquipment(String regionId, String provinceArray, String cityArray, String areaArray, int pageNumber){
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "eqp/pageTerminalEquipment")
                .params("regionId", regionId)
                .params("provinceArray", provinceArray)
                .params("cityArray", cityArray)
                .params("areaArray", areaArray)
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .cacheMode(pageNumber == 1 ? CacheMode.REQUEST_FAILED_READ_CACHE : CacheMode.NO_CACHE)
                .converter(new JsonConvert<BaseResponseBean<BaseListBean<DataBean>>>() {})
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());

    }

    /**
     *  3.1.9用户基本信息  接口(好)
     * @return
     */
    public static Observable<JSONObject> userInformation(){
        return OkGo.<JSONObject>post(SERVLET_URL + "user/information")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  7.1.7设置默认银行卡  接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean>> userBankCardDefaultState(String userBankCardId){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "user/bank/card/defaultState")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("userBankCardId", userBankCardId)
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  7.1.8删除银行卡  接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean>> userBankCardDelete(String userBankCardId){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "user/bank/card/delete")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("userBankCardId", userBankCardId)
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }
    /**
     *  7.1.4提现  接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean>> withdrawalSvae(double price){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "withdrawal/save")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("price", price)
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }
    /**
     *  7.1.6添加银行卡  接口(好)
     * @return
     */
    public static Observable<Response<JSONObject>> userBankCardAddBankCard(String name, String account, String card){
        return OkGo.<JSONObject>post(SERVLET_URL + "user/bank/card/addBankCard")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("name", name)
                .params("account", account)
                .params("bankAddress", card)
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableResponse<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  7.1.1钱包  接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> advSystemListInformation(){
        return OkGo.<BaseResponseBean<List<DataBean>>>get(SERVLET_URL + "adv/system/listInformation")
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());

    }

    /**
     *  7.1.2充值 接口(好)
     */
    public static final String payTopUp = "pay/topUp";

    /**
     *  7.1.3红包充值  接口(好)
     */
    public static final String payRedEnvelopeTopUp = "pay/redEnvelopeTopUp";

    /**
     *  充值
     */
    public static Observable<JSONObject> pay(String url, int price, int type){
        return OkGo.<JSONObject>post(SERVLET_URL + url)
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("price", price)
                .params("type", type)
                .converter(new JsonConvert<JSONObject>(){})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  8.1.1手机设备列表  接口(好)
     */
    public static final String advPageUserADV = "adv/pageUserADV";

    /**
     *  8.1.2终端设备列表 接口(好)
     */
    public static final String eqpPageUserEepADV = "eqp/pageUserEepADV";

    /**
     *  我的  手机   设备 列表
     * @param pageNumber
     * @param url
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> pageUserEepADV(int pageNumber, int type, String url){
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + url)
                .params("pageNumber", pageNumber)
                .params("audit", type)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }
    /**
     *  我的设备查看
     * @param pageNumber
     * @param url
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> eqpPageInvestmentADV(int pageNumber, int type, String url, String eqpId){
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + url)
                .params("pageNumber", pageNumber)
                .params("audit", type)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 9.1.1我的设备列表  接口(好)
     * @param pageNumber
     * @param id
     * @return
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> eqpPageInvestment (int pageNumber, String id){
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "eqp/pageInvestment")
                .params("regionId", id)
                .params("pageNumber", pageNumber)
                .params("pageSize", Constants.pageSize)
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
//                .cacheMode(pageNumber == 1 ? CacheMode.REQUEST_FAILED_READ_CACHE : CacheMode.NO_CACHE)
                .converter(new JsonConvert<BaseResponseBean<BaseListBean<DataBean>>>() {})
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  12.1.2关于我们-简介 接口(好)
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> informationIntroduction(){
        return OkGo.<BaseResponseBean<DataBean>>get(SERVLET_URL + "information/introduction")
                .converter(new JsonConvert<BaseResponseBean<DataBean>>() {})
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }
    /**
     *  12.1.5	APP联系方式 接口(新)
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> informationContactInformation(){
        return OkGo.<BaseResponseBean<DataBean>>get(SERVLET_URL + "information/contactInformation")
                .converter(new JsonConvert<BaseResponseBean<DataBean>>() {})
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }




    /**
     *  12.1.3提交投诉/举报 接口(新)
     */
    public static Observable<Response<BaseResponseBean>> problemSave(String content, String id){
        String url;
        if (!StringUtils.isEmpty(id)){
            url = "sv/video/saveComplaints";
        }else {
            url = "problem/save";
        }
        PostRequest<BaseResponseBean> post = OkGo.<BaseResponseBean>post(SERVLET_URL + url);
        if (!StringUtils.isEmpty(id)){
            post.params("videoId", id);
        }
        return post
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("content", content)
                .params("emojiContent", BaseUtils.encode(content))
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }


    /**
     *  3.1.8用户退出接口(好)
     */
    public static Observable<Response<BaseResponseBean>> userExit(){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "user/exit")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  3.1.10修改密码  接口(好)
     */
    public static Observable<Response<BaseResponseBean>> userUpdatePassword(String originalPassword, String password, String confirmPassword){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "user/updatePassword")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("originalPassword", originalPassword)
                .params("password", password)
                .params("confirmPassword", confirmPassword)
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }
    /**
     *  10.1.3点播支付  接口(好)
     */
    public static Observable<JSONObject> videosOrderSave(String videosId, int payType, String equId){
        return OkGo.<JSONObject>post(SERVLET_URL + "videos/order/save")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("videosId", videosId)
                .params("payType", payType)
                .params("eqpIds", equId)
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  13.1.1版本更新安卓 安卓客户端 接口(好)
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> versionVersionClient(){
        return OkGo.<BaseResponseBean<DataBean>>get(SERVLET_URL + "version/versionClient")
                .converter(new JsonConvert<BaseResponseBean<DataBean>>() {})
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());

    }

    /**
     *  12.1.4获取二维码 接口(新)
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> userGetQrCode(){
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "user/getQrCode")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<BaseResponseBean<DataBean>>() {})
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());

    }
    /**
     *  测试
     */
    public static Observable<Response<JSONObject>> text(){
        return OkGo.<JSONObject>post("http://10.0.0.200:8080/qlbb/api/order/test")
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableResponse<JSONObject>())
                .subscribeOn(Schedulers.io());

    }

    /**
     *  3.1.14	更新个人基本资料  接口(好)
     */
    public static Observable<JSONObject> userupdate(String head, String nick, String mobile, int sex){
        PostRequest<JSONObject> post = OkGo.<JSONObject>post(SERVLET_URL + "user/update");
        if (!StringUtils.isEmpty(head)){
            post.params("head", new File(head));
        }
        if (!StringUtils.isEmpty(nick)){
            post.params("nick", nick);
        }
        if (!StringUtils.isEmpty(mobile)){
            post.params("mobile", mobile);
        }
        if (sex != 0){
            post.params("sex", sex);
        }
        return post
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }
     public static Observable<JSONObject> userupdate(double longitude, double latitude){
        return OkGo.<JSONObject>post(SERVLET_URL + "user/update")
                .params("sessionId", SharedAccount.getInstance(Utils.getApp()).getSessionId())
                .params("longitude", longitude)
                .params("latitude", latitude)
                .converter(new JsonConvert<JSONObject>() {})
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }



    /**
     * 13.1.2广告视频分享 接口(好)
     */
    public static final String videoShare = CloudApi.SERVLET_URL + "share/videoShare?advId=";
    /**
     * 13.1.3广告文章分享 接口(好)
     */
    public static final String articleShare = CloudApi.SERVLET_URL + "share/articleShare?advId=";
    /**
     * 13.1.4广告图片分享 接口(好)
     */
    public static final String imageShare = CloudApi.SERVLET_URL + "share/imageShare?advId=";
    /**
     * 13.1.1短视频分享 接口(好)
     */
    public static final String shortVideoShare = CloudApi.SERVLET_URL + "share/shortVideoShare?videoId=";
    /**
     * 13.1.5	下载分享二维码 接口(好)
     */
    public static final String registeredExtensionCode = CloudApi.SERVLET_URL + "share/registered?extensionCode=";

}