package com.fanwang.fgsb.bean;

import com.fanwang.fgsb.controller.CloudApi;

import java.io.Serializable;

/**
 * Created by yc on 2017/8/17.
 */

public class DataBean implements Serializable {

    private String name;
    private String id;
    private int type;//  `type` int(1) NOT NULL COMMENT '0视频广告，1图片广告，2定格广告,3滚动广告',
    private String remark;
    private int adv_duration;//  `adv_duration` int(10) NOT NULL COMMENT '广告时间',
    private String video_url;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setAdv_duration(int adv_duration) {
        this.adv_duration = adv_duration;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getVideo_url() {
        return video_url;
    }

    private String image, image1, image2, image3;

    public String getImage() {
        return image;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

    public int getAdv_duration() {
        return adv_duration;
    }

    public String getRemark() {
        return remark;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}