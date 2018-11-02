package com.fanwang.fgcm.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.weight.toprightmenu.MenuItem;

import java.util.List;

/**
 * Created by edison on 2018/5/4.
 */

public class ScreenModelTypeAdapter extends BaseRecyclerviewAdapter{

    public ScreenModelTypeAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    private int mPosition = -1;

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        MenuItem bean = (MenuItem) listBean.get(position);
        if (bean != null){
            int[][] states = new int[][]{
                    new int[]{-android.R.attr.state_selected}, // unchecked
                    new int[]{android.R.attr.state_selected}  // checked
            };
            int[] colors = new int[]{
                    Color.parseColor("#1a1a1a"),
                    Color.parseColor("#ffffff")
            };
            viewHolder.tvText.setTextColor(new ColorStateList(states, colors));
            viewHolder.tvText.setText(bean.getText());
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
