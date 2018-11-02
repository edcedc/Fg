package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.databinding.FHtmlBinding;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by edison on 2018/4/12.
 * H5
 */

public class HtmlFrg extends BaseFragment<FHtmlBinding> {

    private int type = -1;
    private String title;
    private String content;
    private String id;

    @Override
    protected void initParms(Bundle bundle) {
        type = bundle.getInt("type");
        title = bundle.getString("title");
        content = bundle.getString("content");
        id = bundle.getString("id");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_html;
    }

    @Override
    protected void initView(View view) {
        switch (type) {
            case 0://用户协议
                setToolbarTitle(getString(R.string.user_register_agreement));
                getH5(CloudApi.informationAgreement);
                break;
            case 1://任务介绍
                setToolbarTitle(getString(R.string.top_mission_introduction));
                getH5(CloudApi.informationAdvertisingTasks);
                break;
            case 2:
                String ss = "api/";
                String s = (String) CloudApi.H5Url + id;
                if (s.contains(ss)){
                    s = s.replace(ss, "");
                }
//                mB.webView.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
                mB.webView.loadUrl(s);
                LogUtils.e(s);
                setToolbarTitle(title);
                break;
            case 3:
                setToolbarTitle(title);
                mB.webView.loadUrl(content);
                break;
            default:
                setToolbarTitle(title);
                mB.webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
                break;
        }
        mB.webView.setInitialScale(100);
        mB.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView var1, int var2, String var3, String var4) {
                mB.progressBar.setVisibility(View.GONE);
                showToast("网页加载失败");
            }
        });
        //进度条
        mB.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mB.progressBar.setVisibility(View.GONE);
                    return;
                }
                mB.progressBar.setVisibility(View.VISIBLE);
                mB.progressBar.setProgress(newProgress);
            }
        });
    }

    private void getH5(String url) {
        CloudApi.getH5(url)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<BaseResponseBean<DataBean>, BaseResponseBean<DataBean>>() {
                    @Override
                    public BaseResponseBean apply(BaseResponseBean baseResponseBean) throws Exception {
                        return baseResponseBean;
                    }
                })
                .subscribe(new Observer<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(BaseResponseBean<DataBean> dataBeanBaseResponseBean) {
                        if (dataBeanBaseResponseBean.code == Code.CODE_SUCCESS) {
                            DataBean data = dataBeanBaseResponseBean.data;
                            if (data != null) {
                                String ss = "api/";
                                String s = (String) CloudApi.H5Url + data.getId();
                                if (s.contains(ss)){
                                    s = s.replace(ss, "");
                                }
                                mB.webView.loadUrl(s);
                                LogUtils.e(s);
//                                mB.webView.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        showToast(e.getMessage().toString());
                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });
    }

}
