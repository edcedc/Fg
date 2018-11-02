package com.fanwang.fgcm.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.utils.GlideImageUtils;
import com.fanwang.fgcm.weight.DateTool;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by edison on 2018/4/13.
 */

public class EncloureHotAdapter extends BaseRecyclerviewAdapter{

    private int type;
    private BaseFragment root;

    public EncloureHotAdapter(Context act,BaseFragment root,  List listBean, int type) {
        super(act, listBean);
        this.type = type;
        this.root = root;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = (DataBean) listBean.get(position);
        Drawable img = bean.getIsPraise() == 0 ? act.getResources().getDrawable(R.mipmap.b17) : act.getResources().getDrawable(R.mipmap.b83);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        Drawable b24 = act.getResources().getDrawable(R.mipmap.e33);
        b24.setBounds(0, 0, b24.getMinimumWidth(), b24.getMinimumHeight());
        switch (type){
            case BundleDataInEvent.mEncloureFabulous:
                viewHolder.tvNumber.setCompoundDrawables(null, null, null, null); //设置左图标
                double distance = bean.getDistance();
                if (distance < 1000){
                    viewHolder.tvNumber.setText("距离" + distance + "m");
                }else {
                    viewHolder.tvNumber.setText("距离" + (distance / 1000) + "km");
                }
                viewHolder.ivRed.setVisibility(bean.getRed_envelopes_number() == 0 ? View.GONE : View.VISIBLE);
                break;
            case BundleDataInEvent.mHotFabulous:
                viewHolder.tvNumber.setCompoundDrawables(img, null, null, null); //设置左图标
                viewHolder.tvNumber.setText(bean.getPraise() + "");
                viewHolder.ivRed.setVisibility(bean.getRed_envelopes_number() == 0 ? View.GONE : View.VISIBLE);
                break;
            case BundleDataInEvent.mMyVideo://我的视频作品
                viewHolder.ivRed.setVisibility(View.GONE);
                viewHolder.tvNumber.setCompoundDrawables(img, null, null, null); //设置左图标
                viewHolder.tvNumber.setText(bean.getPraise() + "");
                viewHolder.tvComment.setCompoundDrawables(b24, null, null, null);
                viewHolder.tvComment.setText(bean.getAwc_number() + "");
                viewHolder.tvTime.setVisibility(View.VISIBLE);
                viewHolder.tvTime.setText(DateTool.getLatelyTime(bean.getCreate_time()));
                break;
        }
        viewHolder.ivVideo.setVisibility(!StringUtils.isEmpty(bean.getVideo_url())? View.VISIBLE : View.GONE);
        viewHolder.tvName.setText(bean.getNick());
        GlideImageUtils.load(act, CloudApi.SERVLET_URL + bean.getCover_image(), viewHolder.ivImg);
        viewHolder.tvContent.setText(bean.getVideo_desc());

        viewHolder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.startViderDescFrg(root, type, new Gson().toJson(bean), position);
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_encloure_hot, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvNumber;
        TextView tvName;
        TextView tvContent;
        TextView tvComment;
        TextView tvTime;
        View item_view;
        ImageView ivVideo;
        ImageView ivRed;
        ImageView ivImg;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvTime = itemView.findViewById(R.id.tv_time);
            item_view = itemView.findViewById(R.id.item_view);
            ivVideo = itemView.findViewById(R.id.iv_video);
            ivRed = itemView.findViewById(R.id.iv_red);
            ivImg = itemView.findViewById(R.id.iv_img);
        }
    }

}
