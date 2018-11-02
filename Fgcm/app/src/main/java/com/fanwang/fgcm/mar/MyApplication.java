package com.fanwang.fgcm.mar;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgcm.service.InitializeService;
import com.fanwang.fgcm.utils.Constants;
import com.nanchen.crashmanager.CrashApplication;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class MyApplication extends CrashApplication {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        //        LogUtils.getConfig().setLogSwitch(false);
        InitializeService.start(this);
        // 设置崩溃后自动重启 APP
//        UncaughtExceptionHandlerImpl.getInstance().init(this, BuildConfig.DEBUG, true, 0, MainActivity.class);
//        Fragmentation.builder()
//                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
//                .stackViewMode(Fragmentation.BUBBLE)
//                .debug(true) // 实际场景建议.debug(BuildConfig.DEBUG)
//                /**
//                 * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
//                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
//                 */
//                .handleException(new ExceptionHandler() {
//                    @Override
//                    public void onException(Exception e) {
//                        // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
//                        // Bugtags.sendException(e);
//                    }
//                })
//                .install();

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

//        registToWX();
    }

    private static IWXAPI api;
    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APPID, false);        // 将该app注册到微信
        api.registerApp(Constants.WX_APPID);
    }

    public static void handleIntent(Intent intent, IWXAPIEventHandler handler){
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            api.handleIntent(intent, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendReq(SendAuth.Req req){
        api.sendReq(req);
    }


    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

}
