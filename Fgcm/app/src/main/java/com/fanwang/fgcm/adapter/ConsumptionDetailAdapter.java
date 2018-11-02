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
 * Created by edison on 2018/5/7.
 */

public class ConsumptionDetailAdapter extends BaseRecyclerviewAdapter{
    public ConsumptionDetailAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = (DataBean) listBean.get(position);
        if (bean != null){
            viewHolder.tvName.setText(position + "");
            switch (bean.getType()){//消费类型，0红包，1广告投放，2短视频发布,3点播,4设备广告投放，5收益,6分享注册金额,,7批量设备广告投放
                case 0:
                    viewHolder.tvName.setText(act.getResources().getString(R.string.red_envelopes));
                    break;
                case 1:
                    viewHolder.tvName.setText(act.getResources().getString(R.string.advertising));
                    break;
                case 2:
                    viewHolder.tvName.setText(act.getResources().getString(R.string.short_video_release));
                    break;
                case 3:
                    viewHolder.tvName.setText(act.getResources().getString(R.string.bottomBarTab_on_demand));
                    break;
                case 4:
                    viewHolder.tvName.setText("设备广告投放");
                    break;
                case 5:
                    viewHolder.tvName.setText("收益");
                    break;
                case 6:
                    viewHolder.tvName.setText("分享注册金额");
                    break;
                case 7:
                    viewHolder.tvName.setText("批量设备广告投放");
                    break;
            }
            viewHolder.tvTime.setText(bean.getCreate_time());
            viewHolder.tvNumber.setText((bean.getState() == 0 ? "-" : "+") + bean.getPrice());
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_consumption_detailed, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvTime;
        TextView tvNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvNumber = itemView.findViewById(R.id.tv_number);
        }
    }
}
