package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.base.IBaseListView;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FPutForwardBinding;
import com.fanwang.fgcm.presenter.ChangyongdeListPresenter;
import com.fanwang.fgcm.presenter.ChangyongdeListlmpl;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/5/5.
 *  提现
 */

public class PutForwardFrg extends BaseFragment<FPutForwardBinding> implements View.OnClickListener, IBaseListView {

    private ChangyongdeListPresenter presenter;

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_put_forward;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.bring));
        presenter = new ChangyongdeListlmpl(this);
//        presenter.onRequest(pagerNumber = 1, CloudApi.userBankCardPage);
        mB.refreshLayout.setPureScrollModeOn();
        mB.btBring.setOnClickListener(this);
        mB.fyCard.setOnClickListener(this);

        JSONObject data = User.getInstance().getUserInformation();
        if (data != null){
            JSONObject userExtend = data.optJSONObject("userExtend");
            if (userExtend != null){
                mB.tvPrice.setText(userExtend.optDouble("red_envelopes") + "");
            }
        }

        mB.etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String price = mB.tvPrice.getText().toString();
                if (price != null){
                    Double i1 = Double.valueOf(price);
                    String s = editable.toString();
                    if (!StringUtils.isEmpty(s)){
                        Double i2 = Double.valueOf(s);
                        if (i2 > 0){
                            if (i2 > i1){
                                mB.btBring.setEnabled(false);
                                mB.tvPrice.setTextColor(getResources().getColor(R.color.colorTintRed));
                            }else {
                                mB.btBring.setEnabled(true);
                                mB.tvPrice.setTextColor(getResources().getColor(R.color.black_1a1a1a));
                            }
                        }else {
                            mB.btBring.setEnabled(false);
                            mB.tvPrice.setTextColor(getResources().getColor(R.color.black_1a1a1a));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_bring:
                final String s = mB.etText.getText().toString();
                if (StringUtils.isEmpty(s))return;
                String price = mB.tvPrice.getText().toString();
                if (!StringUtils.isEmpty(price)){
                    Double of = Double.valueOf(price);
                    if (of < 99){
                        showToast(getString(R.string.empty_hundred));
                        return;
                    }
                    if (Double.valueOf(s) % 100 == 0){

                    }else {
                        showToast(getString(R.string.empty_100));
                        return;
                    }
                }else {
                    return;
                }


                CloudApi.withdrawalSvae(Double.valueOf(s))
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoading();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Response<BaseResponseBean>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);
                            }

                            @Override
                            public void onNext(Response<BaseResponseBean> baseResponseBeanResponse) {
                                if(baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                    /*try {
                                        JSONObject information = User.getInstance().getUserInformation();
                                        double v1 = information.optDouble("red_envelopes") - Double.valueOf(s);
                                        information.put("red_envelopes", v1);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }*/
                                    pop();
                                }else {
                                    UIHelper.startBankCardListFrg(PutForwardFrg.this);
                                }
                                showToast(baseResponseBeanResponse.body().desc);
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
            case R.id.fy_card:
                    UIHelper.startBankCardListFrg(this);
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
        List<DataBean> data = (List<DataBean>) object;
        if (data != null && data.size() != 0){
            for (int i = 0;i < data.size();i++){
                DataBean bean = data.get(i);
                if (bean.getDefault_state() == 1){
                    String account = bean.getAccount();
                    int accountLength = account.length();
                    if (accountLength > 4){
                        String s = account.substring(accountLength - 4, accountLength);
                        mB.tvName.setText(bean.getIc_type());
                        mB.tvNumber.setText("**** **** **** " + s);
                    }else {
                        mB.tvNumber.setText(account);
                    }
                    return;
                }
            }
        }else {
            UIHelper.startBankCardListFrg(PutForwardFrg.this);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        presenter.onRequest(pagerNumber = 1, CloudApi.userBankCardPage);
    }
}
