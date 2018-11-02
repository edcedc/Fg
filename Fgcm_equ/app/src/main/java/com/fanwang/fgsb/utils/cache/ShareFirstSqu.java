package com.fanwang.fgsb.utils.cache;

import android.content.Context;

/**
 * 作者：yc on 2018/7/10.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  是否第一次启动
 */

public class ShareFirstSqu {

    private static Context act;

    private static class Holder {
        private static final ShareFirstSqu INSTANCE = new ShareFirstSqu();
    }

    private ShareFirstSqu() {
    }

    public static final ShareFirstSqu getInstance(Context act) {
        ShareFirstSqu.act = act;
        return ShareFirstSqu.Holder.INSTANCE;
    }

    private final String IS_ONE = "is_one";

    public void save(boolean isLogin){
        ACache.get(act).put(IS_ONE, isLogin, 2592000);
    }

    public boolean getIsLogin(){
        Object asObject = ACache.get(act).getAsObject(IS_ONE);
        if (asObject != null){
            return true;
        }
        return false;
    }

    public void remove(){
        ACache.get(act).remove(IS_ONE);
    }

}
