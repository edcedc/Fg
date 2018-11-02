package com.fanwang.fgcm.event;

import java.util.List;

/**
 * Created by edison on 2018/4/23.
 */

public class StartFrglnEvent {

    // 1 是video开启选择封面页

    private int type;
    private String url;

    public StartFrglnEvent(int type, String url) {
        this.type = type;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    private List<String> listStr;

    public int getType() {
        return type;
    }

    public List<String> getListStr() {
        return listStr;
    }

    public StartFrglnEvent(int type) {
        this.type = type;
    }

    public StartFrglnEvent(int type, List<String> listStr, String data) {
        this.type = type;
        this.listStr = listStr;
        this.url = data;
    }
}
