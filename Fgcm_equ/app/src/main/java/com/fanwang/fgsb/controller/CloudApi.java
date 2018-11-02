package com.fanwang.fgsb.controller;

import com.fanwang.fgsb.bean.BaseResponseBean;
import com.fanwang.fgsb.bean.DataBean;
import com.fanwang.fgsb.callback.JsonConvert;
import com.fanwang.fgsb.callback.NewsCallback;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：yc on 2018/6/20.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class CloudApi {

    private static final String url =
//            "10.0.0.199:8080/wjcm"
            "www.feigemedia.com"
                   ;

    public static final String SERVLET_URL = "http://" +
            url + "/";

    public static final String TEST_URL = ""; //测试

    private static final String TAG = "CloudApi";

    private CloudApi() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 3.1.12移动设备推送注册  接口(好)
     */
    public static Observable<Response<BaseResponseBean>> userMobileDevices(String clientId, String alias){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "api/user/eqpDevices")
                .params("clientId", clientId)
                .params("alias", alias)
                .params("eqpIds", alias)
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  6.1.2省市区列表 接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> regionListProvince(){
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "api/region/listProvince")
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());

    }
    /**
     *  6.1.3下级区域列表 接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> regionListLevel(String regionId){
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "api/region/listLevel")
                .params("regionId", regionId)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }
    /**
     *  16.1.4设备型号列表 接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> labelListModelLabelId(){
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + "api/label/listModelLabelId")
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 16.1.3广告机设备 保存设备 列表 接口(好)
     */
    public static Observable<Response<BaseResponseBean>> eqpListEqpAdv(String name, String id, String province, String city, String area, String address, String lat, String lot, String modelLabelid){
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "eqp/save")
                .params("name", name)
                .params("eqpIds", id)
                .params("province", province)
                .params("city", city)
                .params("area", area)
                .params("addressLogogram", address)
                .params("lat", lat)
                .params("lon", lot)
                .params("modelLabelid", modelLabelid)
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  16.1.2广告机设备轮播列表 接口(好)
     * @return
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> userMobileDevices(String userMobileDevices){
        return OkGo.<BaseResponseBean<List<DataBean>>>get(SERVLET_URL + "eqp/listEqpAdv")
                .params("eqpIds", userMobileDevices)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取下载二维码地址
     */
    public static final String downloadCodeGet = SERVLET_URL + "download/qr/code/get";
    public static Observable<Response<BaseResponseBean>> downloadCodeGet(){
        return OkGo.<BaseResponseBean>get(SERVLET_URL + "download/qr/code/get")
                .converter(new JsonConvert<BaseResponseBean>() {})
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }
}
