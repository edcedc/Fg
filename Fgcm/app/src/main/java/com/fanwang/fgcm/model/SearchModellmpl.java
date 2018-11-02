package com.fanwang.fgcm.model;


import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.callback.JsonConvert;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.presenter.listener.OnSearchListListener;
import com.fanwang.fgcm.utils.Constants;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by edison on 2018/4/25.
 */

public class SearchModellmpl implements SearchModel {

    @Override
    public void ajaxPlaceV2(String s, final OnSearchListListener listener) {
        CloudApi.searchBaidu(s)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onAddDisposable(d);
                    }

                    @Override
                    public void onNext(String data) {
                        try {
                            JSONObject obj = new JSONObject(data);
                            if (obj.optInt("status") == 0){
                                JSONArray result = obj.optJSONArray("result");
                                if (result != null && result.length() != 0){
                                    List<DataBean> listBean = new ArrayList<>();
                                    for (int i = 0;i < result.length(); i++){
                                        JSONObject object = result.optJSONObject(i);
                                        if (object != null){
                                            DataBean bean = new DataBean();
                                            bean.setName(object.optString("name"));
                                            bean.setUid(object.optString("uid"));
                                            bean.setCity(object.optString("city"));
                                            bean.setDistrict(object.optString("district"));
                                            bean.setBusiness(object.optString("business"));
                                            bean.setCityid(object.optInt("cityid"));
                                            JSONObject location = object.optJSONObject("location");
                                            if (location != null){
                                                bean.setLatitude(location.optDouble("lat"));
                                                bean.setLongitude(location.optDouble("lng"));
                                            }
                                            listBean.add(bean);
                                        }
                                    }
                                    listener.onSearchSuccess(listBean);
                                }
                            }else {
                                ToastUtils.showShort(obj.optString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

}
