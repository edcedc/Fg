package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.NoticePageAdapter;
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
 * 作者：yc on 2018/6/7.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class NoticePageFrg extends BaseFragment<SRecyclerBinding> implements IBaseListView {

    private ChangyongdeListPresenter presenter;

    private NoticePageAdapter adapter;
    private List<DataBean> listBean = new ArrayList<>();

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.s_recycler;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.top_pager));
        presenter = new ChangyongdeListlmpl(this);
        if (adapter == null){
            adapter = new NoticePageAdapter(act, listBean);
        }
        mB.recyclerView.setLayoutManager(new LinearLayoutManager(act));
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setAdapter(adapter);
        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                presenter.onRequest(pagerNumber = 1, CloudApi.noticePage);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                presenter.onRequest(pagerNumber += 1, CloudApi.noticePage);
            }
        });
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
