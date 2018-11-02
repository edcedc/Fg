package com.fanwang.fgcm.base;

import com.fanwang.fgcm.bean.DataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2018/5/7.
 */

public class IBaseListModellmpl implements IBaseListModel {



    @Override
    public void ajaxData(int pagerNumber, IOnBaseListListener listener) {
        List<DataBean> listBean = new ArrayList<>();
        for (int i = 0;i < 10;i++){
            listBean.add(new DataBean());
        }
        listener.onSuccess(listBean);
    }

}
