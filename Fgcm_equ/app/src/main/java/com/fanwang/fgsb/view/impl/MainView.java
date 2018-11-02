package com.fanwang.fgsb.view.impl;

import com.fanwang.fgsb.base.IBaseView;

/**
 * 作者：yc on 2018/6/20.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface MainView extends IBaseView{

    void initVideo();

    void setData(Object object);

    void showDownload(String s);
}
