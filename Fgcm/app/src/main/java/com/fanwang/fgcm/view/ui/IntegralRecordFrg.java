package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.IntegralRecordAdapter;
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
 *  积分记录
 */

public class IntegralRecordFrg extends BaseFragment<SRecyclerBinding> implements IBaseListView{


    private List<DataBean> listBean = new ArrayList<>();
    private IntegralRecordAdapter adapter;
    private ChangyongdeListPresenter presenter;

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.s_recycler;
    }

    @Override
    protected void initView(View view) {
        presenter = new ChangyongdeListlmpl(this);
        setToolbarTitle(getString(R.string.top_integral_record), false);
        showRecyclerView();
    }

    private void showRecyclerView() {
        if (adapter == null){
            adapter = new IntegralRecordAdapter(act, listBean);
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
                presenter.onRequest(pagerNumber = 1, CloudApi.integralPage);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                presenter.onRequest(pagerNumber += 1, CloudApi.integralPage);
            }
        });

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
    public void hideLoading() {
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
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
