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

public class MeAdapter extends BaseRecyclerviewAdapter{

    private boolean isImg = false;

    public MeAdapter(Context act, List listBean) {
        super(act, listBean);
    }
    public MeAdapter(Context act, List listBean, boolean isImg) {
        super(act, listBean);
        this.isImg = isImg;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = (DataBean) listBean.get(position);
        if (bean != null){
            viewHolder.tvTitle.setText(bean.getTitle() == null ? bean.getRemark() : bean.getTitle());

            if (isImg){
                viewHolder.ivImg.setVisibility(View.VISIBLE);
                viewHolder.ivImg.setBackgroundResource(bean.getImg());
                if (position == 0 || position == 6 || position == 7){
                    viewHolder.view.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.view.setVisibility(View.GONE);
                }
            }else {

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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_me, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImg;
        TextView tvTitle;
        View layout;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvTitle = itemView.findViewById(R.id.tv_title);
            layout = itemView.findViewById(R.id.layout);
            view = itemView.findViewById(R.id.view);
        }
    }

}
