package com.fanwang.fgcm.utils;

import android.os.Environment;

/**
 * Created by yc on 2017/9/30.
 */

public class Constants {

    public static final int pageSize = 10;

    public static final int video_length = 15 * 1000;

    public static final String mainPath = Environment.getExternalStorageDirectory() + "/fgcm/";
    public static final String fgcm_imgUrl = mainPath + "img/";

    //百度搜索列表
    public static final String baidu_service_ak = "zUBikmVEhjoqBW90PKO5OSAoVMwIneGP";

    public static final String ShareID = "5af40c61a40fa33bf700007c";

    public static final String WX_APPID = "wxbc99d18aa9fbfef9";
    public static final String WX_SECRER = "13662c22cf276e3f83b9a887c7fe9c5d";
//    public static final String WX_APPID = "wx27a2c2ce3eb1d0cb";
//    public static final String WX_SECRER = "844d29560efb51f467f66c70d84a1fb0";

    //新浪文博
    public static final String SINA_APPID = "3678838354";
    public static final String SINA_SECRER = "371b63d20c522384600a51fe1f015f6a";

    public static final int baidu_map_zoom = 16;

}
