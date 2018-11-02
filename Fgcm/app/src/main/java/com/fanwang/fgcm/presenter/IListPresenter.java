package com.fanwang.fgcm.presenter;

/**
 * Created by edison on 2018/4/13.
 */

public interface IListPresenter {

    void onRefresh(int pagerNumber, Object data);

    void onLoadMore(int pagerNumber, Object data);



    void onRefresh(String url, int pagerNumber, Object data);

    void onLoadMore(String url, int pagerNumber, Object data);

    void onRefresh(String url, int pagerNumber);

    void onLoadMore(String url, int pagerNumber);

}
