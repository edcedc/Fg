package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.EncloureHotAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.base.IBaseListView;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.databinding.SNotTitleRecyclerBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.presenter.ChangyongdeListPresenter;
import com.fanwang.fgcm.presenter.ChangyongdeListlmpl;
import com.fanwang.fgcm.weight.GridSpacingItemDecoration;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/4/13.
 * 附近
 */

public class HotFrg extends BaseFragment<SNotTitleRecyclerBinding>implements IBaseListView {

    private ChangyongdeListPresenter presenter;

    public static HotFrg newInstance() {
        Bundle args = new Bundle();
        HotFrg fragment = new HotFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    private EncloureHotAdapter adapter;
    private List<DataBean> listBean = new ArrayList<>();

    @Override
    protected int bindLayout() {
        return R.layout.s_not_title_recycler;
    }

    @Override
    protected void initView(View view) {
        presenter = new ChangyongdeListlmpl(this);
        if (adapter == null){
            adapter = new EncloureHotAdapter(act, this, listBean, BundleDataInEvent.mHotFabulous);
        }
        mB.recyclerView.setLayoutManager(new GridLayoutManager(act, 2, GridLayoutManager.VERTICAL, false));
        mB.recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, getResources().getDimensionPixelSize(R.dimen.px15), true));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setAdapter(adapter);


        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                presenter.onRequest(pagerNumber = 1, CloudApi.videoPageHot);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                presenter.onRequest(pagerNumber += 1, CloudApi.videoPageHot);
            }
        });



        EventBusActivityScope.getDefault(act).register(this);
        setSwipeBackEnable(false);
    }

    @Subscribe
    public void onPraiseState(BundleDataInEvent event){
        if (event.getType() == BundleDataInEvent.mHotFabulous){
            int po = (int) event.getObject();
            if (po != -1){
                DataBean bean = listBean.get(po);
                bean.setPraise(bean.getPraise() + 1);
                bean.setIsPraise(1);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe
    public void onPlayback(BundleDataInEvent event){
        if (event.getType() == BundleDataInEvent.mHotFabulous){
            int po = (int) event.getObject();
            if (po != -1){
                DataBean bean = listBean.get(po);
                bean.setPlayback(bean.getPlayback() + 1);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe
    public void onReb(BundleDataInEvent event){
        if (event.getType() == BundleDataInEvent.Hot_Red){
            int po = (int) event.getObject();
            if (po != -1){
                DataBean bean = listBean.get(po);
                bean.setIsRedEnvelopesRecord(1);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe
    public void onRebGone(BundleDataInEvent event){
        if (event.getType() == BundleDataInEvent.mEncloureRedState){
            int po = (int) event.getObject();
            if (po != -1){
                DataBean bean = listBean.get(po);
                int red_envelopes_number = bean.getRed_envelopes_number();
                bean.setRed_envelopes_number(red_envelopes_number - 1);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusActivityScope.getDefault(act).unregister(this);
    }

    @Override
    public void hideLoading() {
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
    public void onError(Throwable e) {
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
    }

}
