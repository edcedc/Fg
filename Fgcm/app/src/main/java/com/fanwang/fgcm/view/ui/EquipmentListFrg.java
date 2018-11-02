package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.EquipmentListAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FEquipmentListBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.presenter.EquipmentListPresenter;
import com.fanwang.fgcm.presenter.EquipmentListPresenterlmpl;
import com.fanwang.fgcm.utils.DataListTool;
import com.fanwang.fgcm.utils.TopRightMenuTool;
import com.fanwang.fgcm.view.impl.EquipmentListView;
import com.fanwang.fgcm.weight.toprightmenu.MenuItem;
import com.fanwang.fgcm.weight.toprightmenu.TopRightMenu;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/5/2.
 *  设备列表模式
 */

public class EquipmentListFrg extends BaseFragment<FEquipmentListBinding>implements EquipmentListView, View.OnClickListener{

    private double longitude;
    private double latitude;
    private String location;

    private List<DataBean> listBean = new ArrayList<>();
    private List<MenuItem> rangeItems = DataListTool.getRangeist();
    private List<MenuItem> modelItems = new ArrayList<>();

    private EquipmentListAdapter adapter;
    private EquipmentListPresenter presenter;
    private String mModelId;
    private int mRange = -1;

    private String mRegionId;
    private String mProvinceArray;
    private String mCityArray;
    private String mAreaArray;

    @Override
    protected void initParms(Bundle bundle) {
        longitude = bundle.getDouble("longitude");
        latitude = bundle.getDouble("latitude");
        location = bundle.getString("location");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_equipment_list;
    }

    @Override
    protected void initView(View view) {
        presenter = new EquipmentListPresenterlmpl(this);
        setToolbarTitle(getString(R.string.top_terminal_equipment), false);
        mB.btSubmit.setOnClickListener(this);
        mB.lyCb.setOnClickListener(this);
        mB.tvEquRange.setOnClickListener(this);
        mB.tvEquType.setOnClickListener(this);
        mB.tvScreen.setOnClickListener(this);
        mB.tvLocation.setText(location);
        showRecyclerView();
        presenter.onLabelListModelLabelId();
        EventBusActivityScope.getDefault(act).register(this);
    }

    private boolean isAjaxType = true;
    @Subscribe
    public void onMainThreadEvent(BundleDataInEvent event){
        if (event.getType() == BundleDataInEvent.mEquList){
            isAjaxType = false;
            listBean.clear();
            adapter.notifyDataSetChanged();
            Bundle bundle = (Bundle) event.getObject();
            String location = bundle.getString("location");
            if (location.contains("全市")){
                location = location.replace("全市", "");
            }
            if (location.contains("全省")){
                location = location.replace("全省", "");
            }
            mB.tvLocation.setText(location);
            mRegionId = bundle.getString("regionId");
            mProvinceArray = bundle.getString("provinceArray");
            mCityArray = bundle.getString("cityArray");
            mAreaArray = bundle.getString("areaArray");


            presenter.onPageTerminalEquipment(mRegionId,mProvinceArray , mCityArray, mAreaArray, pagerNumber = 1);
        }
    }

    private void showRecyclerView() {
        if (adapter == null){
            adapter = new EquipmentListAdapter(act, listBean, true);
        }
        mB.recyclerView.setLayoutManager(new LinearLayoutManager(act));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setAdapter(adapter);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (isAjaxType){
                    listBean.clear();
                    adapter.notifyDataSetChanged();
                    mB.tvLocation.setText(location);
                    presenter.onRequest(mRange, mModelId, pagerNumber = 1, latitude, longitude);
                }else{
                    presenter.onPageTerminalEquipment(mRegionId,mProvinceArray , mCityArray, mAreaArray, pagerNumber = 1);
                }
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (isAjaxType){
                    presenter.onRequest(mRange, mModelId, pagerNumber += 1, latitude, longitude);
                }else{
                    presenter.onPageTerminalEquipment(mRegionId,mProvinceArray , mCityArray, mAreaArray, pagerNumber += 1);
                }
            }
        });
        adapter.setSelectAllListener(new EquipmentListAdapter.SelectAllListener() {
            @Override
            public void AllTrue(boolean b) {
                mB.ivCb.setBackgroundResource(b == false ? R.mipmap.d22 : R.mipmap.d21);
            }
        });
    }

    @Override
    public void setRefresh(Object object) {
        if (pagerNumber == 1) {
            listBean.clear();
            mB.refreshLayout.finishRefreshing();
        } else {
            mB.refreshLayout.finishLoadmore();
        }
        listBean.addAll((List<DataBean>)object);
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

    @Override
    public void setModelLabelIdList(Object data) {
        List<DataBean> list = (List<DataBean>) data;
        for (int i = 0;i < list.size();i++){
            DataBean bean = list.get(i);
            MenuItem item = new MenuItem();
            item.setText(bean.getName());
            item.setId(bean.getId());
            modelItems.add(item);
        }
    }

    @Override
    public void onRefresh() {
//        listBean.clear();
//        adapter.notifyDataSetChanged();
    }

    private boolean isChecked = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_equ_range://设备范围
                TopRightMenuTool.TopRightMenu(act, rangeItems, mB.tvEquRange, mB.tvEquRange.getWidth(),0, new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        MenuItem item = rangeItems.get(position);
                        mB.tvEquRange.setText(item.getText());
                        mRange = position;
                        if (mRange != -1 && !StringUtils.isEmpty(mModelId)){
                            isAjaxType = true;
                            mB.refreshLayout.startRefresh();
                        }
                    }
                });
                break;
            case R.id.tv_equ_type://设备类型
                if (modelItems.size() == 0)return;
                TopRightMenuTool.TopRightMenu(act, modelItems, mB.tvEquType, 150, new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        MenuItem item = modelItems.get(position);
                        mModelId = item.getId();
                        mB.tvEquType.setText(item.getText());
                        if (mRange != -1 && !StringUtils.isEmpty(mModelId)){
                            isAjaxType = true;
                            mB.refreshLayout.startRefresh();
                        }
                    }
                });
                break;
            case R.id.tv_screen://筛选
                UIHelper.startScreenFrg(modelItems, this);
                break;
            case R.id.ly_cb://全选
                if (listBean.size() == 0)return;
                mB.ivCb.setBackgroundResource(!isChecked ? R.mipmap.d21 : R.mipmap.d22);
                isChecked = !isChecked;
                for (int i = 0;i < listBean.size();i++){
                    DataBean bean = listBean.get(i);
                    bean.setSelect(isChecked);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.bt_submit://提交
                for (int i = 0;i < listBean.size();i++){
                    DataBean bean = listBean.get(i);
                    if (bean.getSelect()){
                        UIHelper.startEquipmentListDetailsFrg(this, listBean, mB.tvLocation.getText().toString(),mModelId, latitude, longitude);
                        break;
                    }
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
    public void onDestroy() {
        super.onDestroy();
        EventBusActivityScope.getDefault(act).unregister(this);
    }
}
