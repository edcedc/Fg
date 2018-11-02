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

import java.util.List;

/**
 * Created by edison on 2018/4/24.
 */

public class MyRedEnvelopesAdapter extends BaseRecyclerviewAdapter{

    public MyRedEnvelopesAdapter(Context act, BaseFragment root, List listBean) {
        super(act, root, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = (DataBean) listBean.get(position);
        if (bean != null){
            viewHolder.tvTitle.setText(bean.getName());
            viewHolder.tvTime.setText(bean.getCreate_time());
            viewHolder.tvNumber.setText("￥" + bean.getReceive_red_envelopes());
            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIHelper.startLooKDetailsFrg(root, bean, 1);
                }
            });
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_my_envelopes, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvTime;
        TextView tvNumber;
        View layout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvNumber = itemView.findViewById(R.id.tv_number);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
