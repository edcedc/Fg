package com.fanwang.fgcm.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.utils.DialogUtil;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/5/4.
 */

public class OnDemandAdapter extends BaseRecyclerviewAdapter{

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OnDemandAdapter(Context act, BaseFragment root, List listBean) {
        super(act, root, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = (DataBean) listBean.get(position);
        if (bean != null){
            viewHolder.tvName.setText(bean.getVideo_name());
            viewHolder.tvPrice.setText("付费：￥" + bean.getPrice() + "元");
            viewHolder.btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogUtil.showSelectEquipmentModel(act, DialogUtil.showSelectListType_2, new DialogUtil.OnClickListener() {
                        @Override
                        public void onClick(final int payPosition) {
                            CloudApi.videosOrderSave(bean.getId(), payPosition, getId())
                                    .doOnSubscribe(new Consumer<Disposable>() {
                                        @Override
                                        public void accept(Disposable disposable) throws Exception {
                                            root.showLoading();
                                        }
                                    })
                                    .subscribeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<JSONObject>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            root.addDisposable(d);
                                        }

                                        @Override
                                        public void onNext(JSONObject jsonObject) {
                                            if (jsonObject.optInt("code") == Code.CODE_SUCCESS){
                                                Bundle bundle = new Bundle();
                                                bundle.putString("data", jsonObject.optString("data"));
                                                bundle.putInt("payPosition", payPosition);
                                                bundle.putInt("position", position);
                                                EventBusActivityScope.getDefault((Activity) act).post(new BundleDataInEvent(BundleDataInEvent.OnDemand_pay, bundle));
                                            }
                                            ToastUtils.showShort(jsonObject.optString("desc"));
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            LogUtils.e(e.getMessage());
                                        }

                                        @Override
                                        public void onComplete() {
                                            root.hideLoading();
                                        }
                                    });
                        }
                    });
                }
            });
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_demand, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImg;
        TextView tvName;
        TextView tvPrice;
        View btSubmit;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btSubmit = itemView.findViewById(R.id.bt_submit);
        }
    }

}
