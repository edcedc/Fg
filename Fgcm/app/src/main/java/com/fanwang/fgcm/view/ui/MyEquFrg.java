package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.MyEquAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.databinding.FMyEquBinding;
import com.fanwang.fgcm.presenter.MyEquPresenter;
import com.fanwang.fgcm.presenter.MyEquPresenterlmpl;
import com.fanwang.fgcm.utils.TopRightMenuTool;
import com.fanwang.fgcm.view.impl.MyEquView;
import com.fanwang.fgcm.weight.toprightmenu.MenuItem;
import com.fanwang.fgcm.weight.toprightmenu.TopRightMenu;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2018/5/7.
 *  我的设备
 */

public class MyEquFrg extends BaseFragment<FMyEquBinding> implements View.OnClickListener, MyEquView {

    private List<DataBean> listBean = new ArrayList<>();
    private MyEquAdapter adapter;
    private MyEquPresenter presenter;

    private List<MenuItem> modelItems = new ArrayList<>();

    private String mModelId = "-1";

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_my_equ;
    }

    @Override
    protected void initView(View view) {
        presenter = new MyEquPresenterlmpl(this);
        setToolbarTitle(getString(R.string.equipment), false);
        mB.tvEquType.setOnClickListener(this);
        showRecyclerView();
        presenter.onLabelListModelLabelId();
    }

    private void showRecyclerView() {
        if (adapter == null){
            adapter = new MyEquAdapter(act,this,  listBean);
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
                presenter.onRequest(pagerNumber = 1, mModelId);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                presenter.onRequest(pagerNumber += 1, mModelId);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_equ_type:
                if (modelItems.size() == 0)return;
                TopRightMenuTool.TopRightMenu(act, modelItems, mB.tvEquType, 150, new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        MenuItem item = modelItems.get(position);
                        mModelId = item.getId();
                        mB.tvEquType.setText("设备型号：" + item.getText());
                        mB.refreshLayout.startRefresh();
                    }
                });
                break;
        }
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
    }

    public void onError(Throwable e) {
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
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
    public void setModelLabelIdList(Object object) {
        List<DataBean> list = (List<DataBean>) object;
        for (int i = 0;i < list.size();i++){
            DataBean bean = list.get(i);
            MenuItem item = new MenuItem();
            item.setText(bean.getName());
            item.setId(bean.getId());
            modelItems.add(item);
        }
    }
}
