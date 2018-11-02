package com.fanwang.fgsb.gt;

import android.content.Context;
import com.blankj.utilcode.util.LogUtils;
import com.fanwang.fgsb.bean.BaseResponseBean;
import com.fanwang.fgsb.controller.CloudApi;
import com.fanwang.fgsb.event.GtInEvent;
import com.fanwang.fgsb.event.GtSwitchInEvent;
import com.fanwang.fgsb.utils.MacAddressTool;
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
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

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
            try {
                JSONObject obj = new JSONObject(data);
                if (obj != null){
                    switch (obj.optInt("type")){
                        case 6:
                            JSONObject data1 = obj.optJSONObject("eqpAdv");
                            if (data1 != null){
//                                EventBusActivityScope.getDefault((Activity) context).post(new GtInEvent(data1));
                                EventBus.getDefault().post(new GtInEvent(data1));
                            }
                            break;
                        case 7:
                            JSONObject data2 = obj.optJSONObject("data");
                            if (data2 != null){
                                if (data2.optInt("type") == 0){
                                    EventBus.getDefault().post(new GtSwitchInEvent(true));
                                }else {
                                    EventBus.getDefault().post(new GtSwitchInEvent(false));
                                }
                            }
                            break;
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        String macID = MacAddressTool.getMacAddress();
//        StringBuffer sb = new StringBuffer();
//        sb.append(clientid).append(macID);
        PushManager.getInstance().bindAlias(context, macID);//个推设置别名
        LogUtils.e("clientid = " + clientid, "macID" + macID);
        CloudApi.userMobileDevices(clientid, macID)
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
