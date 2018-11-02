package com.fanwang.fgsb.base;

import android.os.Bundle;

import org.json.JSONObject;

public class User {

    private static class LazyHolder {
        private static final User INSTANCE = new User();
    }
    private User() {
    }
    public static final User getInstance() {
        return User.LazyHolder.INSTANCE;
    }

    private boolean login = false;
    private JSONObject userObj;
    private String userId;
    private String sessionId;
    private Bundle addressBundle;

    private JSONObject userInformation;

    public JSONObject getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(JSONObject userInformation) {
        this.userInformation = userInformation;
    }

    public void setAddressBundle(Bundle addressBundle) {
        this.addressBundle = addressBundle;
    }

    public Bundle getAddressBundle() {
        if (addressBundle == null){
            Bundle bundle = new Bundle();
            bundle.putDouble("latitude", 23.063805);
            bundle.putDouble("longitude", 113.413844);
            bundle.putString("province", "广东省");
            bundle.putString("city", "广州市");
            bundle.putString("district", "番禺区");
            return bundle;
        }
        return addressBundle;
    }

    public void setUserObj(JSONObject userObj) {
        this.userObj = userObj;
    }

    public JSONObject getUserObj() {
        return userObj;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isLogin() {
        return login;
    }

    public void setUserId(String userId) {
        if (userObj != null) {
            this.userId = userObj.optString("id");
        }
    }

    public String getUserId() {
        if (userObj == null) {
            return null;
        }
        userId = userObj.optString("id");
        return userId;
    }

    public void setSessionId(String sessionId) {
        if (userObj != null) {
            this.sessionId = userObj.optString("sessionId");
        }
    }

    public String getSessionId() {
        if (userObj == null) {
            return "";
        }
        sessionId = userObj.optString("sessionId");
        return sessionId;
    }
}
