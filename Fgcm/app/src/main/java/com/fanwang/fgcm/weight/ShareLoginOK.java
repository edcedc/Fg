package com.fanwang.fgcm.weight;

import android.content.Context;

import com.fanwang.fgcm.utils.SharedPreferencesTool;

/**
 * Created by yc on 2017/12/12.
 */

public class ShareLoginOK {

    private static SharedPreferencesTool share;

    private ShareLoginOK() {
    }

    private static ShareLoginOK instance = null;

    public static ShareLoginOK getInstance(Context context) {
        if (instance == null) {
            instance = new ShareLoginOK();
        }
        share = SharedPreferencesTool.getInstance(context, "account");
        return instance;
    }

    private static final String mLoginOk = "LOGIN_OK";


    public void save(boolean loginOk) {
        share.putBoolean(mLoginOk, loginOk);
        share.commit();
    }

    public boolean getLoginOk() {
        return share.getBoolean(mLoginOk, false);
    }


    public void delete() {
        share.remove(mLoginOk);
        share.commit();
    }

}
