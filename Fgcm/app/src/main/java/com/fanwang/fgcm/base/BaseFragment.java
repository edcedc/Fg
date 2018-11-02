/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fanwang.fgcm.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.pay.PayResult;
import com.fanwang.fgcm.utils.Constants;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.sofia.Sofia;

import org.json.JSONObject;

import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * 若把初始化内容放到initData实现,就是采用Lazy方式加载的Fragment
 * 若不需要Lazy加载则initData方法内留空,初始化内容放到initViews即可
 * -
 * -注1: 如果是与ViewPager一起使用，调用的是setUserVisibleHint。
 * ------可以调用mViewPager.setOffscreenPageLimit(size),若设置了该属性 则viewpager会缓存指定数量的Fragment
 * -注2: 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
 * -注3: 针对初始就show的Fragment 为了触发onHiddenChanged事件 达到lazy效果 需要先hide再show
 */

/**
 * ================================================
 * 作    者：yc）
 * 版    本：1.0
 * 创建日期：16/9/11
 * 描    述：
 * 修订历史：
 * ================================================
 */
public abstract class BaseFragment<VB extends ViewDataBinding> extends SwipeBackFragment {

    private boolean isVisible = true;                  //是否可见状态
    private boolean isPrepared;                 //标志位，View已经初始化完成。
    private boolean isFirstLoad = true;         //是否第一次加载
    protected LayoutInflater inflater;
    protected Activity act;
    private View view;

    protected VB mB;

    protected int pagerNumber = 1;//网络请求默认第一页
    protected int mTotalPage;//网络请求当前几页
    protected int TOTAL_COUNTER;//网络请求一共有几页

    protected boolean isTopFrg = false;//记录是否onResum导航栏

    protected IWXAPI api;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.d("onCreateView");
        this.inflater = inflater;
        isFirstLoad = true;

        View rootView = getLayoutInflater().inflate(this.bindLayout(), null, false);
        mB = DataBindingUtil.bind(rootView);
        isPrepared = true;

        // 初始化参数
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        initParms(bundle);

        this.view = rootView;
        initView(rootView);
        api = WXAPIFactory.createWXAPI(act, Constants.WX_APPID);
        return attachToSwipeBack(rootView);
    }

    protected void setSofia(boolean isFullScreen) {
        if (!isFullScreen){
           Sofia.with(act)
                .statusBarLightFont()
                   .invasionStatusBar()
                   .statusBarBackgroundAlpha(0)
                .statusBarBackground(ContextCompat.getColor(act, R.color.violet_562EA6))
           ;
        }else {
            Sofia.with(act)
                    .invasionStatusBar()
                    .invasionNavigationBar()
                    .statusBarBackgroundAlpha(0)
            ;
        }
    }


    protected abstract void initParms(Bundle bundle);

    protected abstract int bindLayout();

    protected abstract void initView(View view);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("onCreate");
        act = getActivity();
    }


    protected void showToast(final String str){
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLong(str);
            }
        });
    }

    private CompositeDisposable compositeDisposable;

    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    private void dispose() {
        if (compositeDisposable != null) compositeDisposable.dispose();
    }

    private ProgressDialog dialog;

    public void showLoading() {
        mHandler.sendEmptyMessage(handler_load);
    }

    public void hideLoading() {
        mHandler.sendEmptyMessage(handler_hide);
    }

    private final int handler_load = 0;
    private final int handler_hide = 1;
    private final int SDK_PAY_FLAG = 2;

    protected int mFrgName = 0;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case handler_load:
                    if (dialog != null && dialog.isShowing()) return;
                    dialog = new ProgressDialog(act);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setMessage("请求网络中...");
                    dialog.show();
                    break;
                case handler_hide:
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showToast("支付成功");
                        EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.pay));
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToast("支付失败");
                    }
                    break;
            }
        }
    };

    //支付宝支付
    protected void pay(final String info){
        new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(act);
                Map<String, String> result = alipay.payV2(info,true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    protected void wxPay(final JSONObject data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT) {
                    PayReq req = new PayReq();
                    req.appId = data.optString("appid");
                    req.partnerId = data.optString("partnerid");
                    req.prepayId = data.optString("prepayid");
                    req.nonceStr = data.optString("noncestr");
                    req.timeStamp = data.optString("timestamp");
                    req.packageValue = data.optString("package");
                    req.sign = data.optString("sign");
                    req.extData = "app data"; // optional
                    api.sendReq(req);
                }else {
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast(getResources().getString(R.string.not_wx_pay));
                        }
                    });
                }
            }
        }).start();
    }

    protected void setToolbarTitle(String title) {
        title(title, 0, null, true, 0);
    }
    protected void setToolbarTitle(String title, boolean isView) {
        title(title, 0, null, isView, 0);
    }

    // 0   1
//    protected void setToolbarTitleCentre(String title) {
//        title(title, 0, null, true, 0);
//    }

    protected void setToolbarTitle(String title, String rightText) {
        title(title, 0, rightText, true, 0);
    }
    protected void setToolbarTitleRight(String title, int img) {
        title(title, 3, null, true, img);
    }

    protected void setToolbarTitle(String title, int img){
        title(title, 2, null, true, img);
    }

    private void title(String title, int type, String rightText, boolean isToolbarBackground, int img) {
//        setSofia(false);
        final AppCompatActivity mAppCompatActivity = (AppCompatActivity) act;
//        Sofia.with(act).statusBarLightFont();

        setSofia(false);
//        StatusView mStatusView = view.findViewById(R.id.status_view);
//        mStatusView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.violet_562EA6));

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        TextView topTitle = view.findViewById(R.id.top_title);
        TextView topRight = view.findViewById(R.id.top_right);
        FrameLayout topRightFy = view.findViewById(R.id.top_right_fy);
        ImageView topRightImg = view.findViewById(R.id.iv_right);
        //需要调用该函数才能设置toolbar的信息
        mAppCompatActivity.setSupportActionBar(toolbar);
        view.findViewById(R.id.view).setVisibility(isToolbarBackground == true ? View.VISIBLE : View.GONE);
        switch (type){
            case 0:
                mAppCompatActivity.getSupportActionBar().setTitle(title);
                topTitle.setVisibility(View.GONE);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        act.onBackPressed();
                    }
                });
                if (!StringUtils.isEmpty(rightText)){
                    topRightFy.setVisibility(View.VISIBLE);
                    topRight.setText(rightText);
                    topRightFy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setOnRightClickListener();
                        }
                    });
                }else {
                    topRightFy.setVisibility(View.GONE);
                }
                break;
            case 1:
                mAppCompatActivity.getSupportActionBar().setTitle("");
                topTitle.setVisibility(View.VISIBLE);
                topTitle.setText(title);
                toolbar.setNavigationIcon(null);
                break;
            case 2:
                topTitle.setVisibility(View.VISIBLE);
                topTitle.setText(title);
                toolbar.setNavigationIcon(null);
                mAppCompatActivity.getSupportActionBar().setTitle("");
                topRight.setVisibility(View.GONE);
                topRightFy.setVisibility(View.VISIBLE);
                topRightImg.setVisibility(View.VISIBLE);
                topRightImg.setBackgroundResource(img);
                topRightFy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setOnRightClickListener();
                    }
                });
                break;
            case 3:
                mAppCompatActivity.getSupportActionBar().setTitle(title);
                topTitle.setVisibility(View.GONE);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        act.onBackPressed();
                    }
                });
                topRightImg.setVisibility(View.VISIBLE);
                topRightFy.setVisibility(View.VISIBLE);
                topRight.setVisibility(View.GONE);
                topRightImg.setBackgroundResource(img);
                topRightFy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setOnRightClickListener();
                    }
                });
                break;
        }


    }

    protected void setOnRightClickListener() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.d("onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d("onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy");
        dispose();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.d("onDetach");
    }

    protected void setRefreshLayout(TwinklingRefreshLayout refreshLayout, RefreshListenerAdapter listener){
        ProgressLayout headerView = new ProgressLayout(act);
        refreshLayout.setHeaderView(headerView);
        refreshLayout.setOverScrollRefreshShow(false);
        refreshLayout.setOnRefreshListener(listener);
    }

    protected void setRefreshLayoutMode(int listSize, int totalRow, TwinklingRefreshLayout refreshLayout){
        if (listSize == totalRow) {
            refreshLayout.setEnableLoadmore(false);
        } else {
            refreshLayout.setEnableLoadmore(true);
        }
    }

    protected void setRefreshLayout(final int pagerNumber, final TwinklingRefreshLayout refreshLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pagerNumber == 1) {
                    refreshLayout.finishRefreshing();
                } else {
                    refreshLayout.finishLoadmore();
                }            }
        },500);

    }

}
