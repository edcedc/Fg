package com.fanwang.fgsb.event;

/**
 * Created by yc on 2017/11/8.
 */

public class GtInEvent {

    private boolean isSwitch;

    public boolean isSwitch() {
        return isSwitch;
    }

    public GtInEvent(boolean isSwitch) {
        this.isSwitch = isSwitch;
    }

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
