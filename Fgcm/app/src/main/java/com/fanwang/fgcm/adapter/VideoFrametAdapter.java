package com.fanwang.fgcm.adapter;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.utils.GlideImageUtils;
import com.fanwang.fgcm.weight.SizeImageView;
import com.flyco.roundview.RoundFrameLayout;
import com.flyco.roundview.RoundViewDelegate;

import java.util.List;

/**
 * Created by edison on 2018/4/23.
 */

public class VideoFrametAdapter extends BaseRecyclerviewAdapter{

    public VideoFrametAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    private int  selectPosition = -1;

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        String bean = (String) listBean.get(position);
        if (bean != null){
            viewHolder.ivImg.setWH(1, 1, true);
            GlideImageUtils.load(act, bean, viewHolder.ivImg);
            RoundViewDelegate delegate = viewHolder.layout.getDelegate();
            if (getSelectPosition() == position){
                delegate.setStrokeColor(act.getResources().getColor(R.color.violet_562EA6));
            }else {
                delegate.setStrokeColor(0);
            }
            viewHolder.ivImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.onClick(v, position);
                    }
                }
            });
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_video_framet, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        SizeImageView ivImg;
        RoundFrameLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    private OnItemClickListener listener;
    public interface OnItemClickListener{
        void onClick(View v, int position);
    }
    public void setItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
