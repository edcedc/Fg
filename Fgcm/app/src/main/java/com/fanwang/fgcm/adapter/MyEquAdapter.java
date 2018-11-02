package com.fanwang.fgcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.UIHelper;
import com.flyco.roundview.RoundTextView;

import java.util.List;

/**
 * Created by edison on 2018/5/8.
 */

public class MyEquAdapter extends BaseRecyclerviewAdapter{

    public MyEquAdapter(Context act, BaseFragment root, List listBean) {
        super(act,root, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = (DataBean) listBean.get(position);
        if (bean != null){
            viewHolder.tvName.setText("设备名称：" + bean.getName());
            viewHolder.tvAddress.setText("设备地址：" + bean.getAddress());
            viewHolder.tvModel.setText("型号：" + bean.getModelName());
            viewHolder.tvId.setText("ID：" + bean.getEqp_ids());

            viewHolder.btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIHelper.startEquipmentlFrg(root, 2, bean.getId());
                }
            });
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_my_eqt, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvAddress;
        TextView tvModel;
        TextView tvId;
        ImageView ivImg;
        RoundTextView btSubmit;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvModel = itemView.findViewById(R.id.tv_model);
            tvId = itemView.findViewById(R.id.tv_id);
            ivImg = itemView.findViewById(R.id.iv_img);
            btSubmit = itemView.findViewById(R.id.bt_submit);
        }
    }

}
