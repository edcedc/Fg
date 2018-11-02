package com.fanwang.fgcm.adapter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.bean.BaseResponseBean;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.utils.GlideImageUtils;
import com.fanwang.fgcm.view.ui.CommentBottomFrg;
import com.fanwang.fgcm.weight.CircleImageView;
import com.fanwang.fgcm.weight.DateTool;
import com.fanwang.fgcm.weight.WithScrollListView;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/4/19.
 */

public class CommentBottomAdapter extends BaseAdapter {

    private CommentBottomFrg act;
    private List<DataBean> listBean = new ArrayList<>();

    public CommentBottomAdapter(CommentBottomFrg act, List bean) {
        this.act = act;
        this.listBean = bean;
    }

    public static int itemCount = 3;
    private boolean isBt = false;

    @Override
    public int getCount() {
        return listBean.size();
    }

    @Override
    public Object getItem(int position) {
        return listBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = View.inflate(act.getContext(), R.layout.i_comment_bottom, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);
        if (bean != null){
            DataBean.UserBean user = bean.getUser();
            if (user != null){
                GlideImageUtils.load(act.getContext(), CloudApi.SERVLET_URL + user.getHead(), viewHolder.ivHead, true);
                viewHolder.tvName.setText(user.getNick());
            }
            viewHolder.tvTime.setText(DateTool.getLatelyTime(bean.getCreate_time() == null ? TimeUtils.getNowString() : bean.getCreate_time()));
            viewHolder.tvDescribe.setText(bean.getEmoji_content());
            viewHolder.ivPraise.setBackgroundResource(bean.getIsPraise() == 0 ? R.mipmap.b17 : R.mipmap.b83);

            viewHolder.fyPraise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bean.getIsPraise() == 0){
                        ajaxSavePraise(position, bean.getId(), 0);
                    }
                }
            });

            viewHolder.recyclerView.setFocusable(false);
            viewHolder.recyclerView.setFocusableInTouchMode(false);
            final List<DataBean.SvVideoCommentBean> listTwoBean = new ArrayList<>();
            final CommentAdapter adapter = new CommentAdapter(act.getContext(), listTwoBean);
            viewHolder.recyclerView.setAdapter(adapter);
            List<DataBean.SvVideoCommentBean> svVideoComment = bean.getSvVideoComment();
            if (svVideoComment != null && svVideoComment.size() != 0){
                listTwoBean.addAll(svVideoComment);
            }
            adapter.notifyDataSetChanged();
            viewHolder.recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    DataBean.SvVideoCommentBean bean1 = listTwoBean.get(i);
                    if (bean1.getReply_user_id().equals(User.getInstance().getUserId()))return;
                    act.setCommentState(2);
                    Bundle bundle = new Bundle();
                    bundle.putString("nick", "回复 @" + bean1.getReply_nick() + " :");
                    bundle.putString("videoCommentId", bean.getId());
                    bundle.putString("byReplyUserId", bean1.getReply_user_id());
                    bundle.putInt("position", position);
                    EventBusActivityScope.getDefault(act.getActivity()).post(new BundleDataInEvent(BundleDataInEvent.onTwoStageCommentEvent,
                            bundle));
                }
            });

            final ViewHolder finalViewHolder = viewHolder;
            if (listTwoBean.size() > itemCount){
                viewHolder.tvMore.setVisibility(View.VISIBLE);
                finalViewHolder.tvMore.setText("......\n \n更多");
                viewHolder.tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isBt) {
                            adapter.setShowAll(true);
                            finalViewHolder.tvMore.setText("收起");
                        }else {
                            adapter.setShowAll(false);
                            finalViewHolder.tvMore.setText("......\n \n更多");
                        }
                        isBt = !isBt;
                    }
                });
            }else {
                viewHolder.tvMore.setVisibility(View.GONE);
                finalViewHolder.tvMore.setText("收起");
            }
        }
        return convertView;
    }

    private void ajaxSavePraise(final int position, String id, int state) {
        CloudApi.commentSavePraise(state, id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        act.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        act.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean> responseBean) {
                        if (responseBean.body().code == Code.CODE_SUCCESS){
                            DataBean bean = listBean.get(position);
                            bean.setIsPraise(1);
                            notifyDataSetChanged();
                        }
                        ToastUtils.showShort(responseBean.body().desc);
                    }

                    @Override
                    public void onError(Throwable e) {
                        act.hideLoading();
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        act.hideLoading();
                    }
                });
    }

    class ViewHolder {
        CircleImageView ivHead;
        TextView tvName, tvDescribe, tvMore;
        TextView tvTime;
        WithScrollListView recyclerView;
        View fyPraise;
        ImageView ivPraise;

        public ViewHolder(View itemView) {
            ivHead = itemView.findViewById(R.id.iv_head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescribe = itemView.findViewById(R.id.tv_describe);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            tvMore = itemView.findViewById(R.id.tv_more);
            tvTime = itemView.findViewById(R.id.tv_time);
            fyPraise = itemView.findViewById(R.id.fy_praise);
            ivPraise = itemView.findViewById(R.id.iv_praise);
        }
    }

}
