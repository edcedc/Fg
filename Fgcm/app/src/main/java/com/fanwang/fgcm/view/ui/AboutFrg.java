package com.fanwang.fgcm.view.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.blankj.utilcode.util.PhoneUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FAboutBinding;
import com.fanwang.fgcm.presenter.AboutPresenter;
import com.fanwang.fgcm.presenter.AboutPresenterlmpl;
import com.fanwang.fgcm.utils.DialogUtil;
import com.fanwang.fgcm.utils.GlideImageUtils;
import com.fanwang.fgcm.view.impl.AboutView;
import com.lzy.okgo.model.Response;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/5/8.
 *  关于我们
 */

public class AboutFrg extends BaseFragment<FAboutBinding> implements View.OnClickListener, AboutView {

    private AboutPresenter presenter;

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_about;
    }

    @Override
    protected void initView(View view) {
        presenter = new AboutPresenterlmpl(this);
        setToolbarTitle(getString(R.string.about_us));
        mB.lyReport.setOnClickListener(this);
        mB.lyMerchant.setOnClickListener(this);
        mB.lyVersion.setOnClickListener(this);
        mB.refreshLayout.setPureScrollModeOn();
        presenter.ajaxData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_report:
                UIHelper.startComplaintsReportFrg(this);
                break;
            case R.id.ly_merchant:
                showCall();
                break;
            case R.id.ly_version:
                showToast("当前是最新版本");
                presenter.version();
                break;
        }
    }

    private void showCall(){
        CloudApi.informationContactInformation()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            DataBean data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                final String remark = data.getRemark();
                                if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                DialogUtil.showDialogType(act, remark, DialogUtil.showDialog0, new DialogUtil.OnClickListener2() {
                                    @Override
                                    public void onClick(int position) {
                                        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            // TODO: Consider calling
                                            //    ActivityCompat#requestPermissions
                                            // here to request the missing permissions, and then overriding
                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            //                                          int[] grantResults)
                                            // to handle the case where the user grants the permission. See the documentation
                                            // for ActivityCompat#requestPermissions for more details.
                                            return;
                                        }
                                        PhoneUtils.call(remark);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void showWeb(Object object) {
        DataBean bean = (DataBean) object;
        DataBean.LogoBean logo = bean.getLogo();
        if (logo != null){
            GlideImageUtils.load(act, CloudApi.SERVLET_URL + logo.getImage(), mB.ivImg);
        }
        DataBean.IntroductionBean introduction = bean.getIntroduction();
        if (introduction != null){
            mB.webView.setInitialScale(100);
//            mB.webView.loadUrl(introduction.getContent());
             mB.webView.loadDataWithBaseURL(null, introduction.getContent(), "text/html", "utf-8", null);
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
    }

    @Override
    public void showVersion(String url, String version) {
        mB.tvVersion.setText(version + "(新)");
    }
}
