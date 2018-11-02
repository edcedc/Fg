package com.fanwang.fgcm.bean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2018/4/25.
 */

public class SearchUserIdBean extends DataSupport{

    private int id;
    private String userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private List<SearchListBean> list = new ArrayList<>();

    public List<SearchListBean> getList() {
        return list;
    }

    public void setList(List<SearchListBean> list) {
        this.list = list;
    }
}
