package com.fanwang.fgcm.utils;

import android.content.Context;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.weight.CircleImageView;
import com.fanwang.fgcm.weight.SizeImageView;

/**
 * Created by edison on 2018/4/19.
 */

public class GlideImageUtils {

    public static void load(Context act, Object url, ImageView imageView) {
        String ss = "api/";
        String s = (String) url;
        if (s.contains(ss)){
            s = s.replace(ss, "");
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.place_holder);
        Glide.with(act).load(s).apply(options).into(imageView);
    }

    public static void load(Context act, Object url, SizeImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.place_holder);
        Glide.with(act).load(url).apply(options).into(imageView);
    }

    public static void load(Context act, Object url, CircleImageView imageView, boolean isUser) {
        ld(act, url, imageView, isUser);
    }
    public static void load(Context act, Object url, CircleImageView imageView) {
        ld(act, url, imageView, false);
    }


    private static void ld(Context act, Object url, CircleImageView imageView, boolean isUser){
        String ss = "api/";
        String s = (String) url;
        if (s.contains(ss)){
            s = s.replace(ss, "");
        }
        RequestOptions options = new RequestOptions();
        if (isUser){
            options.placeholder(R.mipmap.place_holder_user);
        }else {
            options.placeholder(R.mipmap.place_holder);
        }
        Glide.with(act).load(s).apply(options).into(imageView);
    }

}
