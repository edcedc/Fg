package com.fanwang.fgcm.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.base.IBaseView;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.lzy.okgo.model.Response;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/5/5.
 */

public class BankCardListAdapter extends BaseRecyclerviewAdapter implements IBaseView {


    public BankCardListAdapter(Context act, BaseFragment baseFragment, List listBean) {
        super(act,baseFragment, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = (DataBean) listBean.get(position);
        if (bean != null){
            viewHolder.tvName.setText(bean.getIc_type());
            String account = bean.getAccount();
            if (!StringUtils.isEmpty(account)){
                int accountLength = account.length();
                if (accountLength > 4){
                    String s = account.substring(accountLength - 4, accountLength);
                    viewHolder.tvNumber.setText("**** **** **** " + s);
                }else {
                    viewHolder.tvNumber.setText(account);
                }
            }

            Drawable e22 = act.getResources().getDrawable(R.mipmap.e22);
             // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            e22.setBounds(0, 0, e22.getMinimumWidth(), e22.getMinimumHeight());
             Drawable e21 = act.getResources().getDrawable(R.mipmap.e21);
             // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            e21.setBounds(0, 0, e21.getMinimumWidth(), e21.getMinimumHeight());
            if (bean.getDefault_state() == 0){
                viewHolder.tvDefault.setText(act.getResources().getString(R.string.default_bank_card2));
                viewHolder.tvDefault.setTextColor(act.getResources().getColor(R.color.gray_656565));
                viewHolder.tvDefault.setCompoundDrawables(e22, null, null, null);
            }else {
                viewHolder.tvDefault.setText(act.getResources().getString(R.string.default_bank_card));
                viewHolder.tvDefault.setTextColor(act.getResources().getColor(R.color.colorTintRed));
                viewHolder.tvDefault.setCompoundDrawables(e21, null, null, null);

            }
            viewHolder.tvDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bean.getDefault_state() == 1){
                        showToast("当前是默认银行卡，不能删除");
                        return;
                    }
                    CloudApi.userBankCardDefaultState(bean.getId())
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {
                                    showLoading();
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Response<BaseResponseBean>>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    addDisposable(d);
                                }

                                @Override
                                public void onNext(Response<BaseResponseBean> baseResponseBeanResponse) {
                                    if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                        for (int i = 0;i < listBean.size();i++){
                                            DataBean bean1 = (DataBean) listBean.get(i);
                                            if (bean1.getId().equals(bean.getId())){
                                                bean1.setDefault_state(1);
                                            }else {
                                                bean1.setDefault_state(0);
                                            }
                                        }
                                        notifyDataSetChanged();
                                    }
                                    ToastUtils.showShort(baseResponseBeanResponse.body().desc);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    onError(e);
                                }

                                @Override
                                public void onComplete() {
                                    hideLoading();
                                }
                            });
                }
            });
            viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bean.getDefault_state() == 1)return;
                    CloudApi.userBankCardDelete(bean.getId())
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {
                                    showLoading();
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Response<BaseResponseBean>>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    addDisposable(d);
                                }

                                @Override
                                public void onNext(Response<BaseResponseBean> baseResponseBeanResponse) {
                                    if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                        listBean.remove(position);
                                        notifyDataSetChanged();
                                    }
                                    ToastUtils.showShort(baseResponseBeanResponse.body().desc);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    onError(e);
                                }

                                @Override
                                public void onComplete() {
                                    hideLoading();
                                }
                            });
                }
            });
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_bank_card, parent, false));
    }

    @Override
    public void showLoading() {
        root.showLoading();
    }

    @Override
    public void hideLoading() {
        root.hideLoading();
    }

    @Override
    public void addDisposable(Disposable d) {
        root.addDisposable(d);
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        showToast(e.getMessage());
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvNumber;
        TextView tvDefault;
        TextView tvDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvDefault = itemView.findViewById(R.id.tv_default);
            tvDelete = itemView.findViewById(R.id.tv_delete);

        }
    }

}
