package com.fanwang.fgcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2018/5/8.
 */

public class IntegralRecordAdapter extends BaseRecyclerviewAdapter{
    public IntegralRecordAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = (DataBean) listBean.get(position);
        if (bean != null){
            viewHolder.tvTime.setText(bean.getCreate_time());
             switch (bean.getType()){
                 case 0:
                     viewHolder.tvName.setText("视频广告");
                     break;
                 case 1:
                     viewHolder.tvName.setText("图片广告");
                     break;
                 case 2:
                     viewHolder.tvName.setText("定格广告");
                     break;
                 case 3:
                     viewHolder.tvName.setText("滚动广告");
                     break;
             }
             viewHolder.tvPrice.setText("￥" + (bean.getState() == 0 ? "-" : "+") + bean.getPrice());
            viewHolder.tvIntegral.setText("积分：" + bean.getIntegral());
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_integral, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTime;
        TextView tvName;
        TextView tvPrice;
        TextView tvIntegral;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvIntegral = itemView.findViewById(R.id.tv_integral);
        }
    }

}
