package com.fanwang.fgcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.bean.SearchListBean;
import com.fanwang.fgcm.bean.SearchUserIdBean;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.view.ui.SearchFrg;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by edison on 2018/4/25.
 */

public class SearchAdapter extends BaseRecyclerviewAdapter{

    private SearchFrg searchFrg;
    private int startType;

    public SearchAdapter(Context act, SearchFrg searchFrg, List listBean, int startType) {
        super(act, listBean);
        this.searchFrg = searchFrg;
        this.startType = startType;
    }

    private boolean isClear = false;

    public boolean isClear() {
        return isClear;
    }

    public void setClear(boolean clear) {
        isClear = clear;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = (DataBean) listBean.get(position);
        if (bean != null){
            viewHolder.tvTitle.setText(bean.getName());
            viewHolder.tvContent.setText(bean.getCity() + bean.getDistrict() + bean.getName() + bean.getBusiness());
            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchListBean searchBean = new SearchListBean();
                    searchBean.setName(bean.getName());
                    searchBean.setCity(bean.getCity());
                    searchBean.setBusiness(bean.getBusiness());
                    searchBean.setCityid(bean.getCityid());
                    searchBean.setDistrict(bean.getDistrict());
                    searchBean.setUid(bean.getUid());
                    searchBean.setLatitude(bean.getLatitude());
                    searchBean.setLongitude(bean.getLongitude());
                    searchBean.setUserId("1");
                    searchBean.save();

                    List<SearchUserIdBean> whereUserId = DataSupport.where("userId == ?", "1").find(SearchUserIdBean.class);
                    if (whereUserId.size() == 0){
                        SearchUserIdBean bean = new SearchUserIdBean();
                        bean.setUserId("1");
                        bean.getList().add(searchBean);
                        bean.save();
                    }else {
                        for (int i = 0;i < whereUserId.size();i++){
                            SearchUserIdBean idBean = whereUserId.get(i);
                            List<SearchListBean> list = idBean.getList();
                            if (list != null && list.size() != 0){
                                for (int j = 0;j < list.size();j++){
                                    SearchListBean bean1 = list.get(j);
                                    if (bean1.getUid().equals(bean.getUid())){
                                        break;
                                    }
                                }
                            }
                            idBean.getList().add(searchBean);
                            idBean.save();
                            whereUserId.add(idBean);
                            break;
                        }
                    }
                    EventBus.getDefault().post(new BundleDataInEvent(startType, searchBean));
                    searchFrg.pop();
                }
            });
        }
        if (isClear()){
            if (position == listBean.size() - 1){
                viewHolder.tvClear.setVisibility(View.VISIBLE);
            }else {
                viewHolder.tvClear.setVisibility(View.GONE);
            }
        }else {
            viewHolder.tvClear.setVisibility(View.GONE);
        }
        viewHolder.tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SearchUserIdBean> all = DataSupport.findAll(SearchUserIdBean.class, true);
                if (all != null && all.size() != 0) {
                    for (int i = 0; i < all.size(); i++) {
                        SearchUserIdBean bean1 = all.get(i);
                        if (bean1.getUserId().equals("1")) {
                            List<SearchListBean> list = bean1.getList();
                            for (int j = 0; j < list.size(); j++) {
                                DataSupport.deleteAll(SearchListBean.class, "userId = " + list.get(j).getUserId());
                            }
                        }
                    }
                    listBean.clear();
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_search, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvContent;
        TextView tvClear;
        View layout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvClear = itemView.findViewById(R.id.tv_clear);
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
