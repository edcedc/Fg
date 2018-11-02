package com.fanwang.fgcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.utils.GlideImageUtils;
import com.fanwang.fgcm.weight.CircleImageView;
import com.fanwang.fgcm.weight.DateTool;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by edison on 2018/4/16.
 */

public class RankingListAdapter extends BaseRecyclerviewAdapter{

    private int type;
    private BaseFragment root;

    public RankingListAdapter(Context act,BaseFragment root,  List listBean, int type) {
        super(act, listBean);
        this.type = type;
        this.root = root;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = (DataBean) listBean.get(position);
        if (bean != null){
            GlideImageUtils.load(act, CloudApi.SERVLET_URL + bean.getHead(), viewHolder.iv_head);
            viewHolder.tvName.setText(bean.getName());
            GlideImageUtils.load(act, CloudApi.SERVLET_URL + bean.getCover_image(), viewHolder.iv_img);
            viewHolder.tvTime.setText(DateTool.getLatelyTime(bean.getCreate_time()));
            viewHolder.ivVideo.setVisibility(!StringUtils.isEmpty(bean.getVideo_url())? View.VISIBLE : View.GONE);
            viewHolder.tvDescribe.setText(bean.getVideo_desc());
            viewHolder.tvPraise.setText(bean.getPraise() + "");
            viewHolder.tvComment.setText(bean.getAwc_number() + "");
            viewHolder.ivRed.setVisibility(bean.getRed_envelopes_state() == 0 ? View.GONE : View.VISIBLE);
            viewHolder.item_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.startViderDescFrg(root, type, new Gson().toJson(bean), position);
                }
            });
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_ranking, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        View item_view;
        CircleImageView iv_head;
        ImageView iv_img;
        ImageView ivVideo;
        TextView tvName;
        TextView tvTime;
        TextView tvPraise;
        TextView tvComment;
        TextView tvDescribe;
        LinearLayout lyPraise;
        LinearLayout lyShare;
        ImageView ivRed;

        public ViewHolder(View itemView) {
            super(itemView);
            item_view = itemView.findViewById(R.id.item_view);
            iv_head = itemView.findViewById(R.id.iv_head);
            ivVideo = itemView.findViewById(R.id.iv_video);
            iv_img = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPraise = itemView.findViewById(R.id.tv_praise);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvDescribe = itemView.findViewById(R.id.tv_describe);
            lyPraise = itemView.findViewById(R.id.ly_praise);
            lyShare = itemView.findViewById(R.id.ly_share);
            ivRed = itemView.findViewById(R.id.iv_red);
        }
    }

}
