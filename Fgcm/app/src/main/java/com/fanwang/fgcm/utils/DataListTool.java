package com.fanwang.fgcm.utils;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.weight.toprightmenu.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2018/4/27.
 */

public class DataListTool {

    /**
     *  发布范围
     * @return
     */
    public static List<MenuItem> getKilometreList(){
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("一公里"));
        menuItems.add(new MenuItem("二公里"));
        menuItems.add(new MenuItem("三公里"));
        menuItems.add(new MenuItem("四公里"));
        menuItems.add(new MenuItem("五公里"));
//        menuItems.add(new MenuItem("十公里"));
        menuItems.add(new MenuItem("全省"));
        menuItems.add(new MenuItem("全市"));
        menuItems.add(new MenuItem("全区"));
        return menuItems;
    }

    /**
     *  获取数字公里
     * @return
     */
    public static List<MenuItem> getRangeist(){
        List<MenuItem> menuItems = new ArrayList<>();
        for (int i = 1; i < 10;i ++){
            menuItems.add(new MenuItem(toChinese(i + "") + "公里"));
        }
        return menuItems;
    }


    public static String toChinese(String string) {
        String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String[] s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };
        String result = "";
        int n = string.length();
        for (int i = 0; i < n; i++) {
            int num = string.charAt(i) - '0';
            if (i != n - 1 && num != 0) {
                result += s1[num] + s2[n - 2 - i];
            } else {
                result += s1[num];
            }
        }
        return result;
    }


    /**
     *  发布模式
     * @return
     */
    public static List<MenuItem> getPublishingModeList(){
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("视频"));
        menuItems.add(new MenuItem("图片"));
        menuItems.add(new MenuItem("固定字幕"));
        menuItems.add(new MenuItem("滚动字幕"));
        return menuItems;
    }

    /**
     *  广告时长
     * @return
     */
    public static List<MenuItem> getAdvertisingTimeList(){
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("5"));
        menuItems.add(new MenuItem("10"));
        menuItems.add(new MenuItem("15"));
        menuItems.add(new MenuItem("20"));
        menuItems.add(new MenuItem("30"));
        return menuItems;
    }

    /**
     *  充值
     */
    public static List<Integer> getRechargeList(){
        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(50);
        list.add(100);
        list.add(200);
        list.add(300);
        list.add(500);
        list.add(1000);
        return list;
    }

    /**
     *  支付方式
     */
    public static List<DataBean> getPayList(){
        List<DataBean> listBean = new ArrayList<>();
        DataBean alipay = new DataBean();
        alipay.setTitle("支付宝支付");
        alipay.setImg(R.mipmap.e16);
        listBean.add(alipay);
        DataBean wx = new DataBean();
        wx.setTitle("微信支付");
        wx.setImg(R.mipmap.e17);
        listBean.add(wx);
        return listBean;
    }

    /**
     *  银行卡
     */
    public static List<MenuItem> getCardList(){
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem("招商银行"));
        list.add(new MenuItem("工商银行"));
        return list;
    }

    /**
     *  设备标题
     */
    public static List<DataBean> getEqtTitleList(){
        String[] str = {"全部", "待处理", "审核通过", "审核不通过"};
        List<DataBean> list = new ArrayList<>();
        for (int i = 0;i < str.length;i++){
            DataBean bean = new DataBean();
            bean.setTitle(str[i]);
            list.add(bean);
        }
        return list;
    }

}
