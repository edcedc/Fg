package com.fanwang.fgcm.view.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.EquipmentlAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.base.IBaseListView;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.databinding.SNotTitleRecyclerBinding;
import com.fanwang.fgcm.presenter.EquipmentlChildPresenterLmpl;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/30.
 *  设备
 */

@SuppressLint("ValidFragment")
public class EquipmentlChildFrg extends BaseFragment<SNotTitleRecyclerBinding> implements IBaseListView {

    private int mType;
    private int mPosition;
    private String url;
    private String equId;

    private List<DataBean> listBean = new ArrayList<>();
    private EquipmentlAdapter adapter;
    private EquipmentlChildPresenterLmpl presenter;

    public EquipmentlChildFrg(int type, int position, String equId){
        this.mType = type;
        this.mPosition = position;
        this.equId = equId;
        switch (mType){
            case 0:
                url = CloudApi.advPageUserADV;
                break;
            case 1:
                url = CloudApi.eqpPageUserEepADV;
                break;
            case 2:
                url = CloudApi.eqpPageUserEepADV;
                break;
        }
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.s_not_title_recycler;
    }

    @Override
    protected void initView(View view) {
        setSwipeBackEnable(false);
        presenter = new EquipmentlChildPresenterLmpl(this);
        if (adapter == null){
            adapter = new EquipmentlAdapter(act, this, listBean, mPosition);
        }
        mB.recyclerView.setLayoutManager(new LinearLayoutManager(act));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setAdapter(adapter);
        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                if (mType == 2){
                    presenter.onRequest(pagerNumber = 1, mPosition, url, equId);
                }else {
                    presenter.onRequest(pagerNumber = 1, mPosition, url);
                }
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (mType == 2){
                    presenter.onRequest(pagerNumber += 1, mPosition, url, equId);
                }else {
                    presenter.onRequest(pagerNumber += 1, mPosition, url);
                }
            }
        });
    }

    @Override
    public void hideLoading() {
        mB.refreshLayout.finishRefreshing();
        mB.refreshLayout.finishLoadmore();
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
    public void onError(Throwable e) {
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
    }
}
