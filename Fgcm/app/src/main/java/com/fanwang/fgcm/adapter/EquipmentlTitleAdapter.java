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
import com.flyco.roundview.RoundTextView;
import com.flyco.roundview.RoundViewDelegate;

import java.util.List;

/**
 * Created by edison on 2018/5/7.
 */

public class EquipmentlTitleAdapter extends BaseRecyclerviewAdapter{

    private int mPosition = -1;

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public EquipmentlTitleAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = (DataBean) listBean.get(position);
        if (bean != null){
            viewHolder.tvTitle.setText(bean.getTitle());
            RoundViewDelegate delegate = viewHolder.tvTitle.getDelegate();
            if (getmPosition() == position){
                delegate.setCornerRadius(30);
                delegate.setBackgroundColor(act.getResources().getColor(R.color.violet_562EA6));
            }else {
                delegate.setCornerRadius(0);
                delegate.setBackgroundColor(0);
            }
            viewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.onClick(v, position);
                    }
                }
            });
        }
    }

    private OnItemClickListener listener;
    public interface OnItemClickListener{
        void onClick(View v, int position);
    }
    public void setItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_equ_title, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RoundTextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
