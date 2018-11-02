package com.fanwang.fgcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.UIHelper;
import com.flyco.roundview.RoundTextView;

import java.util.List;

/**
 * Created by edison on 2018/5/7.
 */

public class EquipmentlAdapter extends BaseRecyclerviewAdapter{


    private int mType;

    public EquipmentlAdapter(Context act, BaseFragment root, List listBean, int mPosition) {
        super(act, root, listBean);
        this.mType = mPosition;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = (DataBean) listBean.get(position);
        if (bean != null){
            switch (bean.getAudit()){
                case 0:
                    viewHolder.tvState.setText(act.getResources().getString(R.string.treated));
                    break;
                case 1:
                    viewHolder.tvState.setText(act.getResources().getString(R.string.review_through));
                    break;
                case 2:
                    viewHolder.tvState.setText(act.getResources().getString(R.string.review_no_through));
                    break;
            }
            viewHolder.tvNumber.setText("订单号：" + bean.getOrder_number());
            viewHolder.tvTime.setText("投放时间：" + bean.getCreate_time());
            String type = "广告类型：";
            switch (bean.getType()){
                case 0:
                    viewHolder.tvType.setText(type + "视频广告");
                    break;
                case 1:
                    viewHolder.tvType.setText(type + "图片广告");
                    break;
                case 2:
                    viewHolder.tvType.setText(type + "定格广告");
                    break;
                case 3:
                    viewHolder.tvType.setText(type + "滚动广告");
                    break;
            }
            viewHolder.tvPrice.setText("广告金额：￥" + bean.getPrice());

            viewHolder.btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.startEquDetailsFrg(root, bean);
                }
            });
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_equ, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvNumber;
        TextView tvState;
        TextView tvTime;
        TextView tvType;
        TextView tvPrice;
        RoundTextView btSubmit;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvState = itemView.findViewById(R.id.tv_state);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvType = itemView.findViewById(R.id.tv_type);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btSubmit = itemView.findViewById(R.id.bt_submit);
        }
    }
}
