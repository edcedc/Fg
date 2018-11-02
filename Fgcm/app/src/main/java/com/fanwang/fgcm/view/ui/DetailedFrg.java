package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.DetailedAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.base.IBaseListView;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.databinding.SRecyclerBinding;
import com.fanwang.fgcm.presenter.ChangyongdeListPresenter;
import com.fanwang.fgcm.presenter.ChangyongdeListlmpl;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2018/5/7.
 *  消费明细 和  充值明细
 */

public class DetailedFrg extends BaseFragment<SRecyclerBinding> implements IBaseListView {

    private int mType;
    private DetailedAdapter adapter;
    private List<DataBean> listBean = new ArrayList<>();
    private ChangyongdeListPresenter presenter;
    private String url;

    @Override
    protected void initParms(Bundle bundle) {
        mType = bundle.getInt("type");
    }

    @Override
    protected int bindLayout() {
        return R.layout.s_recycler;
    }

    @Override
    protected void initView(View view) {
        switch (mType){
            case 0:
                url = CloudApi.topupPageBalance;
                setToolbarTitle(getString(R.string.top_recharge_detailed));
                break;
            case 1:
                url = CloudApi.withdrawalPageWD;
                setToolbarTitle(getString(R.string.top_forward_detailed));
                break;
        }
        presenter = new ChangyongdeListlmpl(this);
        showRecyclerView();
    }

    private void showRecyclerView() {
        if (adapter == null) {
            adapter = new DetailedAdapter(act, listBean, mType);
        }
        mB.recyclerView.setLayoutManager(new LinearLayoutManager(act));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setAdapter(adapter);
        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                presenter.onRequest(pagerNumber = 1, url);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                presenter.onRequest(pagerNumber += 1, url);
            }
        });
    }


    @Override
    public void hideLoading() {
        mB.refreshLayout.finishRefreshing();
        mB.refreshLayout.finishLoadmore();
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
}
