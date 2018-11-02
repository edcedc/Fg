package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.MeAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FWalletBinding;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2018/5/5.
 * 钱包
 */

public class WalletFrg extends BaseFragment<FWalletBinding> implements View.OnClickListener, MeAdapter.OnClickListener{

    private List<DataBean> listBean = new ArrayList<>();
    private MeAdapter adapter;

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_wallet;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.wallet));
        mB.btAdvertising.setOnClickListener(this);
        mB.btRed.setOnClickListener(this);
        mB.tvBring.setOnClickListener(this);
        mB.refreshLayout.setPureScrollModeOn();
        showRecyclerView();
        adapter.setOnClickListener(this);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        showUserData();
    }

    private void showUserData() {
        JSONObject data = User.getInstance().getUserObj();
        if (data != null){
            JSONObject userExtend = data.optJSONObject("userExtend");
            if (userExtend != null){
                DecimalFormat myformat1 = new DecimalFormat("###,###");//使用系统默认的格式
                double v = userExtend.optDouble("balance") + userExtend.optDouble("gift_balance");
                String balance = myformat1.format(v);
                mB.tvAdvertising.setText("广告币：￥" + v);
                mB.tvRed.setText("红包币：￥" + userExtend.optDouble("red_envelopes"));
            }
        }
    }

    private void showRecyclerView() {
        String[] strs = {getString(R.string.my_bank_card), getString(R.string.consumption_detail), getString(R.string.recharge_detail),
                getString(R.string.bring_detail)};
        for (int i = 0; i < strs.length; i++) {
            DataBean bean = new DataBean();
            bean.setTitle(strs[i]);
            listBean.add(bean);
        }
        if (adapter == null) {
            adapter = new MeAdapter(act, listBean);
        }
        mB.recyclerView.setLayoutManager(new LinearLayoutManager(act));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_advertising://广告币充值
                UIHelper.startAdvertisingFrg(this, 0);
                break;
            case R.id.bt_red://红包币充值
                UIHelper.startAdvertisingFrg(this, 1);
                break;
            case R.id.tv_bring://提现
                UIHelper.startPutForwardFrg(this);
                break;
        }
    }

    @Override
    public void onClick(View v, int position) {
        switch (position) {
            case 0://我的银行卡
                UIHelper.startBankCardListFrg(this);
                break;
            case 1://消费
                UIHelper.startConsumptionDetailFrg(this);
                break;
            case 2://充值
                UIHelper.startDetailedFrg(this, 0);
                break;
            case 3://提现
                UIHelper.startDetailedFrg(this, 1);
                break;
        }
    }

}
