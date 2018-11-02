package com.fanwang.fgcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2018/5/3.
 */

public class EquipmentListAdapter extends BaseRecyclerviewAdapter {

    private boolean isType = false;
    public EquipmentListAdapter(Context act, List listBean, boolean b) {
        super(act, listBean);
        this.isType = b;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = (DataBean) listBean.get(position);
        if (bean != null) {
               viewHolder.tvName.setText("设备名称：" + bean.getName());
            viewHolder.tvAddress.setText(bean.getAddress());
              viewHolder.tvModel.setText("型号：" + bean.getModelName());
                 viewHolder.tvId.setText("ID：" + bean.getEqp_ids());

            viewHolder.cb.setChecked(bean.getSelect());
            viewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    bean.setSelect(isChecked);
                    for (int i = 0;i < listBean.size();i++){
                        DataBean b = (DataBean) listBean.get(i);
                        if (listener != null && !b.getSelect()){
                            listener.AllTrue(false);
                            return;
                        }else {
                            listener.AllTrue(true);
                        }
                    }
                }
            });
        }
        viewHolder.cb.setVisibility(isType ? View.VISIBLE : View.GONE);
    }

    public interface SelectAllListener{
        void AllTrue(boolean b);
    }
    private SelectAllListener listener;
    public void setSelectAllListener(SelectAllListener listener){
        this.listener = listener;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_equ_list, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvAddress;
        TextView tvModel;
        TextView tvId;
        CheckBox cb;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvModel = itemView.findViewById(R.id.tv_model);
            tvId = itemView.findViewById(R.id.tv_id);
            cb = itemView.findViewById(R.id.cb_);
        }
    }

}
