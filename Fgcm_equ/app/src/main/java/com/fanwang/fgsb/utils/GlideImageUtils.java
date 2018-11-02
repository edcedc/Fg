package com.fanwang.fgsb.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fanwang.fgsb.R;

/**
 * Created by edison on 2018/4/19.
 */

public class GlideImageUtils {

    public static void load(Context act, Object url, ImageView imageView, Drawable drawable) {
        String ss = "api/";
        String s = (String) url;
        if (s.contains(ss)){
//            s = s.replace(ss, "");
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(drawable);
        Glide.with(act).load(s).apply(options).into(imageView);
    }

}
