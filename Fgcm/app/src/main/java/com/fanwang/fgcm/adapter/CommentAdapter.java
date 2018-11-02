package com.fanwang.fgcm.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.bean.DataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2018/4/19.
 */

class CommentAdapter<T> extends BaseAdapter {

    private Context act;
    private List<T> listBean = new ArrayList<>();
    private boolean isShowAll = false;

    public CommentAdapter(Context act, List<T> bean) {
        this.act = act;
        this.listBean = bean;
    }

    @Override
    public int getCount() {
        if (isShowAll) {
            return listBean.size();
        } else {
            if (listBean.size() > CommentBottomAdapter.itemCount) {
                return CommentBottomAdapter.itemCount;
            } else {
                return listBean.size();
            }
        }
    }

    public void setShowAll(boolean isShowAll) {
        this.isShowAll = isShowAll;
        notifyDataSetChanged();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_comment, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DataBean.SvVideoCommentBean bean = (DataBean.SvVideoCommentBean) listBean.get(position);
        if (bean != null){
            viewHolder.tvContent.setText(bean.getEmoji_content());
            /*if (bean.getReply_nick().equals(bean.getNick())){
                viewHolder.tvName.setText(bean.getNick() + "：");
            }else {
                viewHolder.tvName.setText(bean.getReply_nick() + "回复：" + bean.getNick());
            }*/
            if (bean.getSeries() == 2){
                String str1 = bean.getReply_nick() + "<font color='#1a1a1a'> 回复 </font>" + bean.getNick() + "<font color='#1a1a1a'>：</font>";
                viewHolder.tvName.setText(Html.fromHtml(str1));
            }else {
                viewHolder.tvName.setText(bean.getNick() + "：");
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvContent, tvName;

        ViewHolder(View view) {
            tvContent = view.findViewById(R.id.tv_content);
            tvName = view.findViewById(R.id.tv_name);
        }
    }

}
