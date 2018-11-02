package com.fanwang.fgsb.utils.cache;

import android.content.Context;

/**
 * 作者：yc on 2018/7/23.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  是否开关机
 */

public class ShareOffOn {

    private static Context act;

    private static class Holder {
        private static final ShareOffOn INSTANCE = new ShareOffOn();
    }

    private ShareOffOn() {
    }

    public static final ShareOffOn getInstance(Context act) {
        ShareOffOn.act = act;
        return ShareOffOn.Holder.INSTANCE;
    }

    private final String IS_OFF_ON = "is_off_on";

    public void save(boolean is_off_on){
        ACache.get(act).put(IS_OFF_ON, is_off_on, 2592000);
    }

    public boolean getISOFFON(){
        Object asObject = ACache.get(act).getAsObject(IS_OFF_ON);
        if (asObject != null){
            return true;
        }
        return false;
    }

    public void remove(){
        ACache.get(act).remove(IS_OFF_ON);
    }

}
