package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.PayAdapter;
import com.fanwang.fgcm.adapter.RechargeAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.databinding.FRechargeBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.presenter.RechargePresenter;
import com.fanwang.fgcm.presenter.RechargePresenterlmpl;
import com.fanwang.fgcm.utils.DataListTool;
import com.fanwang.fgcm.view.impl.RechargeView;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/5/5.
 * 充值
 */

public class RechargeFrg extends BaseFragment<FRechargeBinding> implements View.OnClickListener , RechargeView {

    private int mType;
    private List<Integer> listBean = new ArrayList<>();
    private RechargeAdapter adapter;

    private PayAdapter payAdapter;
    private List<DataBean> listPay = DataListTool.getPayList();

    private int mPay = 0;
    private String url;

    private List<DataBean> listInformation = new ArrayList<>();

    private RechargePresenter presenter;


    @Override
    protected void initParms(Bundle bundle) {
        mType = bundle.getInt("type");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_recharge;
    }

    @Override
    protected void initView(View view) {
        presenter = new RechargePresenterlmpl(this);
        switch (mType) {
            case 0:
                setToolbarTitle(getString(R.string.advertisement_recharge));
                url = CloudApi.payTopUp;
                presenter.onListInformation();
                break;
            case 1:
                setToolbarTitle(getString(R.string.red_recharge));
                url = CloudApi.payRedEnvelopeTopUp;
                break;
            default:
                setToolbarTitle(getString(R.string.recharge));
                break;
        }
        listBean.addAll(DataListTool.getRechargeList());
        mB.refreshLayout.setPureScrollModeOn();
        mB.btRecharge.setOnClickListener(this);
        showRecyclerView();
        showPayRecyclerView();

        mB.etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                new Handler().postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        String text = editable.toString();
                        if (!StringUtils.isEmpty(text)){
                            mB.tvText.setText("");
                            Integer integer = Integer.valueOf(text);
                            for (int i = 0;i < listInformation.size();i++){
                                DataBean bean = listInformation.get(i);
                                double smallerThan = bean.getSmallerThan();//小于
                                double greaterThan = bean.getGreaterThan();//大于
                                double giving_balance = bean.getGiving_balance();//赠送金额
                                if (integer <= smallerThan && integer >= greaterThan){
                                    if (mType == 0){
                                        mB.tvText.setText("充值：" + integer + "元   赠送" + giving_balance + "元");
                                    }else {
                                        mB.tvText.setText("充值：" + integer + "元");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }, 500);
            }
        });
        EventBusActivityScope.getDefault(act).register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusActivityScope.getDefault(act).unregister(this);
    }

    @Subscribe
    public void onMainThreadInEvent(BundleDataInEvent event){
        if (event.getType() == BundleDataInEvent.pay || event.getType() == BundleDataInEvent.wx_pay){
            String s = mB.etText.getText().toString().trim();
            JSONObject data = User.getInstance().getUserInformation();
            switch (mType){
                case 0:
                    try {
                        double balance = data.optDouble("balance");
                        data.put("balance", (balance + Double.valueOf(s)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        double balance = data.optDouble("red_envelopes");
                        data.put("red_envelopes", (balance + Double.valueOf(s)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            pop();
        }
    }

    private void showPayRecyclerView() {
        if (payAdapter == null) {
            payAdapter = new PayAdapter(act, listPay);
        }
        mB.rvPay.setLayoutManager(new LinearLayoutManager(act));
        mB.rvPay.setHasFixedSize(true);
        mB.rvPay.setItemAnimator(new DefaultItemAnimator());
        mB.rvPay.setAdapter(payAdapter);
        payAdapter.setmPosition(0);
        payAdapter.notifyDataSetChanged();
        payAdapter.setOnClickListener(new PayAdapter.OnClickListener() {
            @Override
            public void onClick(View v, int position) {
                mPay = position;
                payAdapter.setmPosition(position);
                payAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showRecyclerView() {
        if (adapter == null) {
            adapter = new RechargeAdapter(act, listBean);
        }
        mB.recyclerView.setLayoutManager(new GridLayoutManager(act, 3, GridLayoutManager.VERTICAL, false));
//        mB.recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, getResources().getDimensionPixelSize(R.dimen.px15), true));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new RechargeAdapter.OnClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (mType == 0){
                    if (listInformation.size() == 0)return;
                }
                adapter.setmPosition(position);
                adapter.notifyDataSetChanged();
                Integer integer = listBean.get(position);
                mB.etText.setText(integer + "");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_recharge:
                presenter.onPay(mPay, mB.etText.getText().toString(), url);
                break;
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void showListInformation(Object object) {
        listInformation.addAll((List<DataBean>) object);
//        List<DataBean> list = (List<DataBean>) object;
//        listPay.addAll(list);
//        payAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPaySuccess(String into, Double price) {
        if (mPay == 0){
            pay(into);
        }else {
            try {
                wxPay(new JSONObject(into));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
