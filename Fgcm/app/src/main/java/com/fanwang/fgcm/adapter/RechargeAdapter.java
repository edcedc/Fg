package com.fanwang.fgcm.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2018/5/5.
 */

public class RechargeAdapter extends BaseRecyclerviewAdapter{

    private int mPosition = -1;

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public RechargeAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
          ViewHolder viewHolder = (ViewHolder) holder;
        int number = (int) listBean.get(position);
        if (number > 0){
            int[][] states = new int[][]{
                    new int[]{-android.R.attr.state_selected}, // unchecked
                    new int[]{android.R.attr.state_selected}  // checked
            };
            int[] colors = new int[]{
                    Color.parseColor("#1a1a1a"),
                    Color.parseColor("#ffffff")
            };
            viewHolder.tvText.setTextColor(new ColorStateList(states, colors));
            viewHolder.tvText.setText(number + "元");
            LinearLayout.LayoutParams topContentTextView_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);//此处我需要均分高度就在heignt处设0,1.0f即设置权重是1，页面还有其他一个控件,1：1高度就均分了
            viewHolder.tvText.setLayoutParams(topContentTextView_lp);
            if (getmPosition() == position){
                viewHolder.tvText.setSelected(true);
            }else {
                viewHolder.tvText.setSelected(false);
            }
            viewHolder.tvText.setOnClickListener(new View.OnClickListener() {
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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_region_label, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvText;

        public ViewHolder(View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_text);
        }
    }
}
