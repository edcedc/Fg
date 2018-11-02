package com.fanwang.fgcm.view.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.PhoneUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.MeAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.base.IBaseListView;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FCustomerBinding;
import com.fanwang.fgcm.presenter.ChangyongdeListPresenter;
import com.fanwang.fgcm.presenter.ChangyongdeListlmpl;
import com.fanwang.fgcm.utils.DialogUtil;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/5/8.
 *  客服中心
 */

public class CustomerFrg extends BaseFragment<FCustomerBinding> implements View.OnClickListener, IBaseListView, MeAdapter.OnClickListener{

    private List<DataBean> listBean = new ArrayList<>();
    private MeAdapter adapter;
    private ChangyongdeListPresenter presenter;

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_customer;
    }

    @Override
    protected void initView(View view) {
        presenter = new ChangyongdeListlmpl(this);
        setToolbarTitle(getString(R.string.top_customer));
        mB.btService.setOnClickListener(this);
        showRecyclerView();
        adapter.setOnClickListener(this);
    }

    private void showRecyclerView() {
        if (adapter == null) {
            adapter = new MeAdapter(act, listBean);
        }
        mB.recyclerView.setLayoutManager(new LinearLayoutManager(act));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setAdapter(adapter);
        showLoading();
        presenter.onRequest(pagerNumber = 1, CloudApi.informationPageIssue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_service:
                CloudApi.informationContactInformation()
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoading();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);
                            }

                            @Override
                            public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                                if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                    DataBean data = baseResponseBeanResponse.body().data;
                                    if (data != null){
                                        final String remark = data.getRemark();
                                        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            // TODO: Consider calling
                                            //    ActivityCompat#requestPermissions
                                            // here to request the missing permissions, and then overriding
                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            //                                          int[] grantResults)
                                            // to handle the case where the user grants the permission. See the documentation
                                            // for ActivityCompat#requestPermissions for more details.
                                            return;
                                        }
                                        DialogUtil.showDialogType(act, remark, DialogUtil.showDialog0, new DialogUtil.OnClickListener2() {
                                            @Override
                                            public void onClick(int position) {
                                                if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    // TODO: Consider calling
                                                    //    ActivityCompat#requestPermissions
                                                    // here to request the missing permissions, and then overriding
                                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                    //                                          int[] grantResults)
                                                    // to handle the case where the user grants the permission. See the documentation
                                                    // for ActivityCompat#requestPermissions for more details.
                                                    return;
                                                }
                                                PhoneUtils.call(remark);
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                hideLoading();
                            }
                        });
                break;
        }
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void setData(Object object) {
//        if (pagerNumber == 1) {
//            listBean.clear();
//            mB.refreshLayout.finishRefreshing();
//        } else {
//            mB.refreshLayout.finishLoadmore();
//        }
        List<DataBean> data = (List<DataBean>) object;
        for (int i = 0;i < (data.size() > 6 ? 5 : data.size());i++){
            DataBean bean = data.get(i);
            String remark = bean.getTitle();
            bean.setTitle((i + 1) + "、" + remark);
            listBean.add(bean);
        }
        DataBean bean = new DataBean();
        int i = data.size() > 6 ? 5 : data.size();
        bean.setTitle((i + 1) + "、  更多问题");
        listBean.add(listBean.size(), bean);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v, int position) {
        DataBean bean = listBean.get(position);
        if(position != listBean.size() - 1){
            UIHelper.startHtmlFrg(this, bean.getTitle(), bean.getContent(), bean.getId());
        }else {
            UIHelper.startMoreProblemsFrg(this);
        }
    }
}
