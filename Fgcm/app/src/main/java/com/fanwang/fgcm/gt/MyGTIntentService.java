package com.fanwang.fgcm.gt;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.event.GtInEvent;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class MyGTIntentService extends GTIntentService {


    private static final String TAG = "GetuiSdkDemo";

    public MyGTIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        LogUtils.e("---------------------------------------------个推连接成功-------------------------------------------------");
//        LogUtils.e("onReceiveServicePid -> " + pid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        if (payload == null) {
        } else {
            String data = new String(payload);
            LogUtils.e(data);
            if (data != null){
                try {
                    JSONObject obj = new JSONObject(data);
                    JSONObject data1 = obj.optJSONObject("data");
                    EventBus.getDefault().post(new GtInEvent(data1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        LogUtils.e("onReceiveClientId -> " + "clientid = " + clientid);
        StringBuffer sb = new StringBuffer();
        sb.append(clientid).append(User.getInstance().getUserId());
        PushManager.getInstance().bindAlias(context, sb.toString());//个推设置别名
        CloudApi.userMobileDevices(clientid, sb.toString())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<BaseResponseBean> baseResponseBeanResponse) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
//        LogUtils.e("onReceiveOnlineState -> " + (online ? "online" : "offline"));
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
//        LogUtils.e("onReceiveCommandResult -> " + cmdMessage);
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {

    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {

    }

}
