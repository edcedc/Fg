package com.fanwang.fgcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2018/5/5.
 */

public class PayAdapter extends BaseRecyclerviewAdapter{

    private int mPosition = -1;

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public PayAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = (DataBean) listBean.get(position);
        if (bean != null){
            viewHolder.ivImg.setBackgroundResource(bean.getImg());
            viewHolder.tvTitle.setText(bean.getTitle());
            if (getmPosition() == position){
                viewHolder.ivCb.setBackgroundResource(R.mipmap.e18);
            }else {
                viewHolder.ivCb.setBackgroundResource(R.mipmap.e19);
            }
            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.onClick(v, position);
                    }
                }
            });
        }
    }

    private OnClickListener listener;
    public interface OnClickListener{
        void onClick(View v, int position);
    }
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_pay, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        View layout;
        TextView tvTitle;
        ImageView ivImg, ivCb;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivImg = itemView.findViewById(R.id.iv_img);
            ivCb = itemView.findViewById(R.id.iv_cb);
        }
    }

}
