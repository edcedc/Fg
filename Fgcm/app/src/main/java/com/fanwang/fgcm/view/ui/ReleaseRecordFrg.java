package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.MeAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.SRecyclerBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2018/5/7.
 *  投放记录
 */

public class ReleaseRecordFrg extends BaseFragment<SRecyclerBinding> implements MeAdapter.OnClickListener{

    private List<DataBean> listBean = new ArrayList<>();
    private MeAdapter adapter;

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.s_recycler;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.top_my_launch));
        showRecyclerView();
        mB.refreshLayout.setPureScrollModeOn();
        adapter.setOnClickListener(this);
    }

    private void showRecyclerView() {
        String[] strs = {getString(R.string.mobile_devices), getString(R.string.terminal_devices)};
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
    public void onClick(View v, int position) {
        UIHelper.startEquipmentlFrg(this, position);
    }
}
