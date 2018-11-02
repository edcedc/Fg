package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.SearchAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.bean.SearchListBean;
import com.fanwang.fgcm.bean.SearchUserIdBean;
import com.fanwang.fgcm.databinding.FSearchBinding;
import com.fanwang.fgcm.presenter.SearchPresenterlmpl;
import com.fanwang.fgcm.view.impl.SearchView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by edison on 2018/4/25.
 *  搜索地图
 */

public class SearchFrg extends BaseFragment<FSearchBinding> implements View.OnClickListener, SearchView{

    private SearchPresenterlmpl presenterl;

    private SearchAdapter adapter;
    private List<DataBean> listBean = new ArrayList<>();
    private int startType;

    @Override
    protected void initParms(Bundle bundle) {
        startType = bundle.getInt("startType");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_search;
    }

    @Override
    protected void initView(View view) {
        presenterl = new SearchPresenterlmpl(this);
        mB.fyFinish.setOnClickListener(this);
        mB.fySearch.setOnClickListener(this);
        mB.refreshLayout.setPureScrollModeOn();
        if (adapter == null){
            adapter = new SearchAdapter(act,this, listBean, startType);
        }
        mB.recyclerView.setLayoutManager(new LinearLayoutManager(act));
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setAdapter(adapter);
//        mB.refreshLayout.startRefresh();
        mB.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        if (s.length() != 0){
                            presenterl.setSearchText(s.toString());
                            adapter.setClear(false);
                            adapter.notifyDataSetChanged();
                        }else {
                            showSearchList();
                            adapter.setClear(true);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, 500);
            }
        });
        showSearchList();
    }

    private void showSearchList(){
        List<SearchUserIdBean> all = DataSupport.findAll(SearchUserIdBean.class, true);
        if (all != null && all.size() != 0) {
            for (int i = 0; i < all.size(); i++) {
                SearchUserIdBean bean = all.get(i);
                if (bean.getUserId().equals("1")) {
                    List<SearchListBean> list = bean.getList();
                    for (int j = 0; j < list.size(); j++) {
                        SearchListBean searchListBean = list.get(j);
                        DataBean bean1 = new DataBean();
                        bean1.setName(searchListBean.getName());
                        bean1.setCityid(searchListBean.getCityid());
                        bean1.setCity(searchListBean.getCity());
                        bean1.setDistrict(searchListBean.getDistrict());
                        bean1.setBusiness(searchListBean.getBusiness());
                        bean1.setLongitude(searchListBean.getLongitude());
                        bean1.setLatitude(searchListBean.getLatitude());
                        bean1.setUserId(searchListBean.getUserId());
                        listBean.add(bean1);
                    }
                }
            }
            Collections.reverse(listBean);
            adapter.setClear(true);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fy_finish:
                pop();
                break;
            case R.id.fy_search:

                break;
        }
    }

    @Override
    public void setRefresh(DataBean bean) {

    }

    @Override
    public void onSearchSuccess(List<DataBean> list) {
        listBean.clear();
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(Throwable e) {

    }

}
