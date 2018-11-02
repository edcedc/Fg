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

public class DetailedAdapter extends BaseRecyclerviewAdapter{

    private int mType;

    public DetailedAdapter(Context act, List listBean, int type) {
        super(act, listBean);
        mType = type;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = (DataBean) listBean.get(position);
        if (bean != null){

            if (mType == 0){
                switch (bean.getType()){//  `type` int(1) NOT NULL COMMENT '类型，0余额，1红包,2赠送',
                    case 0:
                        viewHolder.tvName.setText("广告币充值");
                        break;
                    case 1:
                        viewHolder.tvName.setText("红包币充值");
                        break;
                    case 2:
                        viewHolder.tvName.setText("广告币充值赠送");
                        break;
                        default:
                            viewHolder.tvName.setText("邀请好友赠送红包币");
                        break;
                }
            }else {
                viewHolder.tvName.setText(act.getResources().getString(R.string.bring));
            }

            viewHolder.tvNumber.setText((mType == 1 ? "-" : "+") + bean.getPrice());
//            LogUtils.e(bean.getPrice());
            viewHolder.tvTime.setText(bean.getCreate_time());
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_detailed, parent, false));
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
