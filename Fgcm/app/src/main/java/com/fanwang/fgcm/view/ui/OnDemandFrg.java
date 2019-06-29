package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.OnDemandAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.base.IBaseListView;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FOnDemandBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.presenter.OnDemandPresenter;
import com.fanwang.fgcm.presenter.OnDemandPresenterlmpl;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/4/13.
 *  点播
 */

public class OnDemandFrg extends BaseFragment<FOnDemandBinding>implements IBaseListView, View.OnClickListener{

    private OnDemandPresenter presenter;
    private OnDemandAdapter adapter;
    private List<DataBean> listBean = new ArrayList<>();

    public static OnDemandFrg newInstance() {
        Bundle args = new Bundle();
        OnDemandFrg fragment = new OnDemandFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initParms(Bundle bundle) {

    }
    @Override
    protected int bindLayout() {
        return R.layout.f_on_demand;
    }

    @Override
    protected void initView(View view) {
        presenter = new OnDemandPresenterlmpl(this);
        mB.btZXing.setOnClickListener(this);
        showRecyclerView();
        EventBusActivityScope.getDefault(act).register(this);
        EventBus.getDefault().register(this);
        setSwipeBackEnable(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        EventBusActivityScope.getDefault(act).unregister(this);
    }

    private int position;
    private String zxingId;

    @Subscribe
    public void OnMainThreadInEvent(BundleDataInEvent event){
        if (event.getType() == BundleDataInEvent.OnDemand_pay){
            Bundle bundle = (Bundle) event.getObject();
            String data = bundle.getString("data");
            position = bundle.getInt("position");
            int payPosition = bundle.getInt("payPosition");
            if (payPosition == 0){
                pay(data);
            }else {
                try {
                    wxPay(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if (event.getType() == BundleDataInEvent.pay){
//            DataBean bean = listBean.get(position);
//            listBean.remove(position);
//            listBean.add(0, bean);
//            adapter.notifyDataSetChanged();
        }else if (event.getType() == BundleDataInEvent.demand_zxing){
            mB.ivImg.setVisibility(View.GONE);
            zxingId = (String) event.getObject();
            adapter.setId(zxingId);
            adapter.notifyDataSetChanged();
            mB.refreshLayout.startRefresh();
        }
    }

    private void showRecyclerView() {
        if (adapter == null){
            adapter = new OnDemandAdapter(act,this, listBean);
        }
        mB.recyclerView.setLayoutManager(new LinearLayoutManager(act));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setAdapter(adapter);
//        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                presenter.onRequest(pagerNumber = 1, zxingId);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                presenter.onRequest(pagerNumber += 1, zxingId);
            }
        });
    }

        @Override
        public void onError(Throwable e) {
            super.setRefreshLayout(pagerNumber, mB.refreshLayout);
        }

        @Override
        public void setData(Object data) {
            if (pagerNumber == 1) {
                listBean.clear();
                mB.refreshLayout.finishRefreshing();
            } else {
                mB.refreshLayout.finishLoadmore();
            }
            listBean.addAll((List<DataBean>) data);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void setRefreshLayoutMode(int totalRow) {
            super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
        }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ZXing://二维码
                UIHelper.startZxingAct();
                break;
        }
    }
}
