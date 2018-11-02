package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.BankCardListAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.base.IBaseListView;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FBankListBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.presenter.ChangyongdeListPresenter;
import com.fanwang.fgcm.presenter.ChangyongdeListlmpl;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/5/5.
 *  我的银行卡
 */

public class BankCardListFrg extends BaseFragment<FBankListBinding> implements View.OnClickListener, IBaseListView {

    private List<DataBean> listBean = new ArrayList<>();
    private BankCardListAdapter adapter;
    private ChangyongdeListPresenter presenter;

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_bank_list;
    }

    @Override
    protected void initView(View view) {
        presenter = new ChangyongdeListlmpl(this);
        setToolbarTitleRight(getString(R.string.bank_card_management), R.mipmap.e38);
        mB.btBring.setOnClickListener(this);
        showRecyclerView();
        EventBusActivityScope.getDefault(act).register(this);
    }

    @Subscribe
    public void onMainThreadEvent(BundleDataInEvent event){
        if (event.getType() == BundleDataInEvent.BankCardListFrg){
            DataBean bean = (DataBean) event.getObject();
            listBean.add(0, bean);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void setOnRightClickListener() {
        super.setOnRightClickListener();
        UIHelper.startAddBankCardFrg(this);
    }

    private void showRecyclerView() {
        if (adapter == null) {
            adapter = new BankCardListAdapter(act, this, listBean);
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
                presenter.onRequest(pagerNumber = 1, CloudApi.userBankCardPage);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                presenter.onRequest(pagerNumber += 1, CloudApi.userBankCardPage);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_bring://提现
                if (listBean.size() != 0){
                    for (int i = 0;i < listBean.size();i++){
                        DataBean bean = listBean.get(i);
                        if (bean.getDefault_state() == 1){
                            UIHelper.startPutForwardFrg(this);
                            break;
                        }
                    }
                }else {
                    showToast(getString(R.string.empty_card));
                }
                break;
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
        EventBusActivityScope.getDefault(act).unregister(this);
    }
}
