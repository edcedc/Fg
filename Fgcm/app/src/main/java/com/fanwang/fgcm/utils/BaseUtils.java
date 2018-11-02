package com.fanwang.fgcm.utils;

import android.util.Base64;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yc on 2017/4/19.
 */

public class BaseUtils {

    public BaseUtils() {

    }

//    private static BaseUtils instance = null;
//
//    public static BaseUtils getInstance() {
//        if (instance == null) {
//            instance = new BaseUtils();
//        }
//        return instance;
//    }


    /**
     * 编码
     *
     * @param contont
     */
    public String encodeToString(String contont) {
        return Base64.encodeToString(contont.getBytes(), Base64.DEFAULT);
    }

    /**
     * 编码
     *
     * @param contont
     */
    public static String encode(String contont) {
        return new String(Base64.encode(contont.getBytes(), Base64.NO_WRAP));
    }

    /**
     * 解码
     * @param contont
     */
    public static String decoded(String contont) {
        if (contont == null)return "";
        if (checkBlank(contont)){
            contont = contont.replace(" ","+");
        }
        byte[] bt = null;
        try {
            sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            bt = decoder.decodeBuffer(contont);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bt);
    }

    private static boolean checkBlank(String str) {
        Pattern pattern = Pattern.compile("[\\s]+");
        Matcher matcher = pattern.matcher(str);
        boolean flag = false;
        while (matcher.find()) {
            flag = true;
        }
        return flag;
    }


}
