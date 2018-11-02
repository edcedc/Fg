package com.fanwang.fgcm.event;

/**
 * Created by yc on 2017/11/8.
 */

public class GtInEvent {

    // 0系统通知1个人通知,2发送交换微信通知,3发送 用户拒绝交换微信 请求,4 发送 用户同意交换微信 请求,5最近访问用户通知,6心动用户通知,7互相心动用户通知
    private int type;

    private Object object;

    public Object getObject() {
        return object;
    }

    public int getType() {
        return type;
    }

    public GtInEvent(int type) {
        this.type = type;
    }

    public GtInEvent(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    public GtInEvent(Object object) {
        this.object = object;
    }
}
