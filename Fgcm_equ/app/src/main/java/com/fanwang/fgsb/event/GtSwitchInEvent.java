package com.fanwang.fgsb.event;

/**
 * Created by yc on 2017/11/8.
 */

public class GtSwitchInEvent {

    private boolean isSwitch;

    public boolean isSwitch() {
        return isSwitch;
    }

    public GtSwitchInEvent(boolean isSwitch) {
        this.isSwitch = isSwitch;
    }


}
