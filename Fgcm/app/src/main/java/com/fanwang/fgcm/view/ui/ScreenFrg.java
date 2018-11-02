package com.fanwang.fgcm.view.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.example.library.AutoFlowLayout;
import com.example.library.FlowAdapter;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.ScreenModelTypeAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.databinding.FScreenBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.presenter.ScreenPresenter;
import com.fanwang.fgcm.presenter.ScreenPresenterlmpl;
import com.fanwang.fgcm.view.impl.ScreenView;
import com.fanwang.fgcm.weight.toprightmenu.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/5/3.
 * 筛选
 */

public class ScreenFrg extends BaseFragment<FScreenBinding> implements View.OnClickListener, ScreenView {

    private ScreenModelTypeAdapter typeAdapter;
    private ScreenPresenter presenter;

    private List<MenuItem> menuItems = new ArrayList<>();

    private List<DataBean> listProvince = new ArrayList<>();
    private List<DataBean> listCity = new ArrayList<>();
    private List<DataBean> listArea = new ArrayList<>();

    @Override
    protected void initParms(Bundle bundle) {
        Type type = new TypeToken<ArrayList<MenuItem>>() {}.getType();
        Gson gson = new Gson();
        menuItems = gson.fromJson(bundle.getString("list"), type);
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_screen;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.top_screen));
        presenter = new ScreenPresenterlmpl(this);
        mB.btReset.setOnClickListener(this);
        mB.btSubmit.setOnClickListener(this);
        presenter.onAjaxRegionListProvince();
        showRecyclerViewType();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_reset://重置  不要问为什么走两次!!!
                mB.scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mB.scrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
                mB.scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mB.scrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
                typeAdapter.setmPosition(0);
                typeAdapter.notifyDataSetChanged();
                if (listProvince != null && listProvince.size() != 0){
                    for (int i = 0; i < listProvince.size();i++){
                        DataBean bean = listProvince.get(i);
                        bean.setSelect(false);
                    }
                    showAutoFlowAdapter(mB.rvProvince,false, listProvince);
                }
                mB.rvArea.removeAllViews();
                mB.rvCity.removeAllViews();
                break;
            case R.id.bt_submit:
                MenuItem item = menuItems.get(typeAdapter.getmPosition());
                String regionId = item.getId();

                StringBuffer sp = new StringBuffer();
                StringBuffer pName = new StringBuffer();
                for (int p = 0;p < listProvince.size();p++){
                    DataBean bean = listProvince.get(p);
                    if (bean.getSelect()){
                        String id = bean.getId();
                        sp.append(id).append(",");
                        pName.append(bean.getName()).append(",");
                    }
                }

                if (pName.length() != 0){
                    pName = pName.deleteCharAt(pName.length() - 1);
                }

                StringBuffer sc = new StringBuffer();
                StringBuffer cName = new StringBuffer();
                for (int p = 0;p < listCity.size();p++){
                    DataBean bean = listCity.get(p);
                    if (bean.getSelect()){
                        String id = bean.getId();
                        sc.append(id).append(",");
                        cName.append(bean.getName()).append(",");
                    }
                }
                if (cName.length() != 0){
                    cName = cName.deleteCharAt(cName.length() - 1);
                }

                StringBuffer sa = new StringBuffer();
                StringBuffer aName = new StringBuffer();
                for (int p = 0;p < listArea.size();p++){
                    DataBean bean = listArea.get(p);
                    if (bean.getSelect()){
                        String id = bean.getId();
                        sa.append(id).append(",");
                        aName.append(bean.getName()).append(",");
                    }
                }
                if (aName.length() != 0){
                    aName = aName.deleteCharAt(aName.length() - 1);
                }

                LogUtils.e(sp.toString(), sc.toString(), sa.toString());

                if (StringUtils.isEmpty(regionId)){
                    return;
                }
                String s = sp.toString();
                String s1 = sc.toString();
                String s2 = sa.toString();
                if (s.equals(s1)){
                    s1 = "";
                }else if (s1.equals(s2)){
                    s2 = "";
                }
                Bundle bundle = new Bundle();
                bundle.putString("regionId", regionId);
                bundle.putString("provinceArray", s);
                bundle.putString("cityArray", s1);
                bundle.putString("areaArray", s2);
                bundle.putString("location", pName.toString() + cName.toString() + aName.toString());
                EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.mEquList, bundle));
                pop();
                break;
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void showRecyclerViewType() {
        if (typeAdapter == null) {
            typeAdapter = new ScreenModelTypeAdapter(act, menuItems);
        }
        mB.rvType.setLayoutManager(new LinearLayoutManager(act));
        mB.rvType.setHasFixedSize(true);
        mB.rvType.setItemAnimator(new DefaultItemAnimator());
        mB.rvType.setHasFixedSize(true);
        mB.rvType.setAdapter(typeAdapter);
        typeAdapter.setmPosition(0);
        typeAdapter.notifyDataSetChanged();
        typeAdapter.setOnClickListener(new ScreenModelTypeAdapter.OnClickListener() {
            @Override
            public void onClick(View v, int position) {
                typeAdapter.setmPosition(position);
                typeAdapter.notifyDataSetChanged();
            }
        });
    }

//    private String provinceId;//暂时这样写
    @Override
    public void showProvince(Object object) {
        listProvince = (List<DataBean>) object;
        showAutoFlowAdapter(mB.rvProvince,true, listProvince);
        mB.rvProvince.setOnItemClickListener(new AutoFlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int j, View view) {
                DataBean bean = listProvince.get(j);
                String provinceId = bean.getId();
                /*if (!bean.getSelect()) {
                    provinceId = bean.getId();
                    bean.setSelect(true);
                    presenter.onRegionListLevel(bean.getId(), 0);
                } else {
                    bean.setSelect(false);
                }*/
                if (!bean.getSelect()) {
                    bean.setSelect(true);
                } else {
                    bean.setSelect(false);
                }
                List<DataBean> listStr = new ArrayList<>();
                for (int i = 0;i < listProvince.size();i++){
                    DataBean bean1 = listProvince.get(i);
                    if (bean1.getSelect()){
                        listStr.add(bean1);
                    }
                }
                if (listStr.size() != 0){
                    if (listStr.size() > 1){
                        listCity.clear();
                        listArea.clear();
                        mB.rvCity.removeAllViews();
                        mB.rvArea.removeAllViews();
                    }else {
                        presenter.onRegionListLevel(listStr.get(0).getId(), 0);
                    }
                }else {
                    listCity.clear();
                    listArea.clear();
                    mB.rvCity.removeAllViews();
                    mB.rvArea.removeAllViews();
                }
            }
        });
    }

    @Override
    public void showCity(Object object) {
        listCity.clear();
        listCity = (List<DataBean>) object;
        mB.rvCity.removeAllViews();
        mB.rvArea.removeAllViews();
        showAutoFlowAdapter(mB.rvCity,true, listCity);
        setFOCUS_DOWN();
        mB.rvCity.setOnItemClickListener(new AutoFlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int j, View view) {
                DataBean bean = listCity.get(j);
                /*if (!bean.getSelect()) {
                    bean.setSelect(true);
                    if (!bean.getName().contains("全省")){
                        presenter.onRegionListLevel(bean.getId(), 1);
                    }else {
                        listArea.clear();
                        mB.rvArea.removeAllViews();
                    }
                } else {
                    bean.setSelect(false);
                    listArea.clear();
                    mB.rvArea.removeAllViews();
                }*/

                if (!bean.getSelect()) {
                    bean.setSelect(true);
                    if (bean.getName().contains("全省")){
                        for (int i = 0;i < listCity.size();i++){
                            DataBean bean1 = listCity.get(i);
                            if (i != 0){
                                bean1.setSelect(false);
                            }
                        }
                        showAutoFlowAdapter(mB.rvCity,true, listCity);
                        listArea.clear();
                        mB.rvArea.removeAllViews();
                        return;
                    }else if (listCity.get(0).getSelect()){
                        listCity.get(0).setSelect(false);
                        showAutoFlowAdapter(mB.rvCity,true, listCity);
                    }else {

                    }
                } else {
                    bean.setSelect(false);
                    if (bean.getName().contains("全省")){
                        for (DataBean bean1 : listCity){
                            bean1.setSelect(false);
                        }
                        listArea.clear();
                        mB.rvArea.removeAllViews();
                    }
                    showAutoFlowAdapter(mB.rvCity,true, listCity);
                }
                List<DataBean> listStr = new ArrayList<>();
                for (DataBean bean1 : listCity){
                    if (bean1.getSelect()){
                        listStr.add(bean1);
                    }
                }
                if (listStr.size() != 0){
                    if (listStr.size() > 1){
                        listCity.get(0).setSelect(false);
                        listArea.clear();
                        mB.rvArea.removeAllViews();
                    }else {
                        presenter.onRegionListLevel(bean.getId(), 1);
                    }
                }else {
                    listArea.clear();
                    mB.rvArea.removeAllViews();
                }
            }
        });
    }

    @Override
    public void showArea(Object object) {
        listArea.clear();
        listArea = (List<DataBean>) object;
        showAutoFlowAdapter(mB.rvArea,true, listArea);
        setFOCUS_DOWN();
        mB.rvArea.setOnItemClickListener(new AutoFlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int j, View view) {
                DataBean bean = listArea.get(j);
               /* if (!bean.getSelect()) {
                    bean.setSelect(true);
                } else {
                    bean.setSelect(false);
                }*/
                if (!bean.getSelect()) {
                    bean.setSelect(true);
                    if (bean.getName().contains("全市")){
                        for (int i = 0;i < listArea.size();i++){
                            DataBean bean1 = listArea.get(i);
                            if (i != 0){
                                bean1.setSelect(false);
                            }
                        }
                        showAutoFlowAdapter(mB.rvArea,true, listArea);
                        return;
                    }else if (listArea.get(0).getSelect()){
                        listArea.get(0).setSelect(false);
                        showAutoFlowAdapter(mB.rvArea,true, listArea);
                    }
                } else {
                    bean.setSelect(false);
                    if (bean.getName().contains("全市")){
                        for (DataBean bean1 : listArea){
                            bean1.setSelect(false);
                        }
                    }
                    showAutoFlowAdapter(mB.rvArea,true, listArea);
                }

            }
        });
    }

    private void showAutoFlowAdapter(AutoFlowLayout layout,boolean MultiChecked,  final List<DataBean> listBean){
        layout.removeAllViews();
        layout.setMultiChecked(MultiChecked);
        layout.setAdapter(new FlowAdapter(listBean) {
            @Override
            public View getView(int i) {
                View view = View.inflate(act, R.layout.i_region_label, null);
                TextView tvText = view.findViewById(R.id.tv_text);
                DataBean bean = listBean.get(i);
                if (bean != null){
                    tvText.setText(bean.getName());
                    int[][] states = new int[][]{
                            new int[]{-android.R.attr.state_selected}, // unchecked
                            new int[]{android.R.attr.state_selected}  // checked
                    };
                    int[] colors = new int[]{
                            Color.parseColor("#666666"),
                            Color.parseColor("#ffffff")
                    };
                    tvText.setTextColor(new ColorStateList(states, colors));
                    tvText.setSelected(bean.getSelect());
                }
                return view;
            }
        });
    }

    private void setFOCUS_DOWN(){
        //设置默认滚动到底部
        mB.scrollView.post(new Runnable() {
            @Override
            public void run() {
                mB.scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

}
