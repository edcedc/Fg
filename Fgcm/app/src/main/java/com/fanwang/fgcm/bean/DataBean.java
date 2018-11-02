package com.fanwang.fgcm.bean;

import com.fanwang.fgcm.utils.BaseUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yc on 2017/8/17.
 */

public class DataBean implements Serializable {

    private String title;
    private String name;
    private String uid;
    private String city;//城市
    private String district;//区
    private String business;//县
    private int cityid;
    private double latitude;//纬度
    private double longitude;//经度
    private String userId;
    private boolean isSelect = false;
    private String image;
    private int img;
    private String content;
    private String video_url;//广告视频地址
    private String video_desc;//  `video_desc` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '视频描述',
    private int red_envelopes_state;//  `red_envelopes_state` int(1) NOT NULL DEFAULT '0' COMMENT '红包算法，0无红包，1固定红包，2随机红包',
    private String cover_image;//  `cover_image` char(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '封面图',
    private int praise;//点赞
    private double distance;//距离
    private int playback;//播放量
    private String head;//头像
    private double red_envelopes;//红包金额
    private int awc_number;//评论数量
    private String id;
    private String create_time;
    private int isPraise;//0未  1有
    private double lon;
    private double lat;
    private double receive_red_envelopes;//领取红包价格
    private String model_labelId;//設備型號
    private String address;
    private String eqp_ids;//设备ID
    private int seconds;//秒数
    private double price;//金额
    private String video_name;//ship
    private double balance;//余额
    private int type;//类型
    private String ic_type;//简介
    private String account;//账号
    private int default_state;//默认状态
    private int state;
    private double smallerThan;//小于
    private double greaterThan;//大于
    private double giving_balance;//赠送金额
    private int audit;//  `audit` int(1) NOT NULL DEFAULT '0' COMMENT '审核 0正在审核，1审核成功，1审核失败',
    private String order_number;//订单号
    private int adv_duration;//广告时长
    private String start_delivery_time;//开始时间
    private String end_delivery_time;//结束时间
    private String remark;//原因
    private int red_envelopes_number;//红包个数
    private int scope_type;//范围
    private String image1, image2, image3;
    private int integral;//积分
    private String version;//版本号
    private String url;
    private String qr_code;//二维码
    private int series;//判断水回复谁
    private String nick;
    private String reply_nick;
    private String reply_user_id;
    private int isRedEnvelopesRecord;
    private String address_logogram;
    private String modelName;
    private String modelLabel;
    private String province;
    private String area;
    private int count;
    private String emojiContent;
    private String emoji_content;
    private String eqpName;
    private String order_reason;//手机设备原因
    private int redEnvelopesNumber;
    private String reason;

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getReason() {
        return reason;
    }

    public int getRedEnvelopesNumber() {
        return redEnvelopesNumber;
    }

    public String getOrder_reason() {
        return order_reason;
    }

    public String getEqpName() {
        return eqpName;
    }

    public void setEmojiContent(String emojiContent) {
        this.emojiContent = emojiContent;
    }

    public String getEmoji_content() {
        return BaseUtils.decoded(emoji_content);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmojiContent() {
        return BaseUtils.decoded(emojiContent);
    }

    public int getCount() {
        return count;
    }

    public void setPlayback(int playback) {
        this.playback = playback;
    }

    public String getArea() {
        return area;
    }

    public String getProvince() {
        return province;
    }

    public String getModelLabel() {
        return modelLabel;
    }

    public String getModelName() {
        return modelName;
    }

    public String getAddress_logogram() {
        return address_logogram;
    }

    public int getIsRedEnvelopesRecord() {
        return isRedEnvelopesRecord;
    }

    public void setIsRedEnvelopesRecord(int isRedEnvelopesRecord) {
        this.isRedEnvelopesRecord = isRedEnvelopesRecord;
    }

    public String getReply_user_id() {
        return reply_user_id;
    }

    public String getReply_nick() {
        return reply_nick;
    }

    public String getNick() {
        return nick;
    }

    public int getSeries() {
        return series;
    }

    public String getQr_code() {
        return qr_code;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public int getIntegral() {
        return integral;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setModel_labelId(String model_labelId) {
        this.model_labelId = model_labelId;
    }

    public void setEqp_ids(String eqp_ids) {
        this.eqp_ids = eqp_ids;
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

    public int getScope_type() {
        return scope_type;
    }

    public int getRed_envelopes_number() {
        return red_envelopes_number;
    }

    public void setRed_envelopes_number(int red_envelopes_number) {
        this.red_envelopes_number = red_envelopes_number;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEnd_delivery_time() {
        return end_delivery_time;
    }

    public String getStart_delivery_time() {
        return start_delivery_time;
    }

    public int getAdv_duration() {
        return adv_duration;
    }

    public String getOrder_number() {
        return order_number;
    }

    public int getAudit() {
        return audit;
    }

    public double getGiving_balance() {
        return giving_balance;
    }

    public double getGreaterThan() {
        return greaterThan;
    }

    public double getSmallerThan() {
        return smallerThan;
    }

    public String getIc_type() {
        return ic_type;
    }

    public int getState() {
        return state;
    }

    public void setDefault_state(int default_state) {
        this.default_state = default_state;
    }

    public int getDefault_state() {
        return default_state;
    }

    public String getAccount() {
        return account;
    }

    public int getType() {
        return type;
    }

    public double getBalance() {
        return balance;
    }

    public String getVideo_name() {
        return video_name;
    }

    public double getPrice() {
        return price;
    }

    public int getSeconds() {
        return seconds;
    }

    public String getEqp_ids() {
        return eqp_ids;
    }

    public String getAddress() {
        return address;
    }

    public String getModel_labelId() {
        return model_labelId;
    }

    public double getReceive_red_envelopes() {
        return receive_red_envelopes;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getId() {
        return id;
    }

    public int getAwc_number() {
        return awc_number;
    }

    public double getRed_envelopes() {
        return red_envelopes;
    }

    public String getHead() {
        return head;
    }

    public int getPlayback() {
        return playback;
    }

    public double getDistance() {
        return distance;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public String getCover_image() {
        return cover_image;
    }

    public int getRed_envelopes_state() {
        return red_envelopes_state;
    }

    public void setRed_envelopes_state(int red_envelopes_state) {
        this.red_envelopes_state = red_envelopes_state;
    }

    public String getVideo_desc() {
        return video_desc;
    }

    public String getVideo_url() {
        return video_url;
    }

    public String getContent() {
        return content;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getImage() {
        return image;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean getSelect() {
        return isSelect;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean{
        private String head;
        private String nick;
        private String user_id;
        private String mobile;
        private String name;
        private String id;
        private String reply_nick;
        private String reply_user_id;

        public String getReply_user_id() {
            return reply_user_id;
        }

        public String getReply_nick() {
            return reply_nick;
        }

        public void setReply_nick(String reply_nick) {
            this.reply_nick = reply_nick;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private List<ListEqpBean> listEqp;

    public List<ListEqpBean> getListEqp() {
        return listEqp;
    }

    public class ListEqpBean{
        private String name;
        private String province;
        private String city;
        private String area;
        private String model_labelId;
        private String eqp_ids;
        private String address_logogram;
        private String modelLabel;

        public String getModelLabel() {
            return modelLabel;
        }

        public String getAddress_logogram() {
            return address_logogram;
        }

        public String getName() {
            return name;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public String getArea() {
            return area;
        }

        public String getModel_labelId() {
            return model_labelId;
        }

        public String getEqp_ids() {
            return eqp_ids;
        }
    }

    private LogoBean logo;

    public LogoBean getLogo() {
        return logo;
    }

    public class LogoBean{
        private String image;

        public String getImage() {
            return image;
        }
    }

    private IntroductionBean introduction;

    public IntroductionBean getIntroduction() {
        return introduction;
    }

    public class IntroductionBean{
        private String content;

        public String getContent() {
            return content;
        }
    }

    private List<SvVideoCommentBean> svVideoComment;

    public List<SvVideoCommentBean> getSvVideoComment() {
        return svVideoComment;
    }

    public void setSvVideoComment(List<SvVideoCommentBean> svVideoComment) {
        this.svVideoComment = svVideoComment;
    }

    public static class SvVideoCommentBean {

        private String head;
        private String reply_head;
        private String content;
        private String name;
        private String nick;
        private String user_id;
        private String id;
        private int series;
        private String reply_nick;
        private String reply_user_id;
        private String emojiContent;
        private String emoji_content;

        public void setEmoji_content(String emoji_content) {
            this.emoji_content = emoji_content;
        }

        public void setEmojiContent(String emojiContent) {
            this.emojiContent = emojiContent;
        }

        public String getEmoji_content() {
            return BaseUtils.decoded(emoji_content);
        }

        public String getEmojiContent() {
            return BaseUtils.decoded(emojiContent);
        }

        public void setReply_user_id(String reply_user_id) {
            this.reply_user_id = reply_user_id;
        }

        public String getReply_user_id() {
            return reply_user_id;
        }

        public void setReply_nick(String reply_nick) {
            this.reply_nick = reply_nick;
        }

        public String getReply_nick() {
            return reply_nick;
        }

        public void setSeries(int series) {
            this.series = series;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getSeries() {
            return series;
        }

        public String getId() {
            return id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public void setReply_head(String reply_head) {
            this.reply_head = reply_head;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getNick() {
            return nick;
        }

        public String getName() {
            return name;
        }

        public String getHead() {
            return head;
        }

        public String getReply_head() {
            return reply_head;
        }

        public String getContent() {
            return content;
        }

    }

    @Override
    public String toString() {
        return "DataBean{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", business='" + business + '\'' +
                ", cityid=" + cityid +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", userId='" + userId + '\'' +
                ", isSelect=" + isSelect +
                ", image='" + image + '\'' +
                ", img=" + img +
                ", content='" + content + '\'' +
                ", video_url='" + video_url + '\'' +
                ", video_desc='" + video_desc + '\'' +
                ", red_envelopes_state=" + red_envelopes_state +
                ", cover_image='" + cover_image + '\'' +
                ", praise=" + praise +
                ", distance='" + distance + '\'' +
                ", playback=" + playback +
                ", head='" + head + '\'' +
                ", red_envelopes=" + red_envelopes +
                ", awc_number=" + awc_number +
                ", id='" + id + '\'' +
                ", create_time='" + create_time + '\'' +
                ", isPraise=" + isPraise +
                ", lon=" + lon +
                ", lat=" + lat +
                ", receive_red_envelopes=" + receive_red_envelopes +
                ", model_labelId='" + model_labelId + '\'' +
                ", address='" + address + '\'' +
                ", eqp_ids='" + eqp_ids + '\'' +
                ", seconds=" + seconds +
                ", price=" + price +
                ", video_name='" + video_name + '\'' +
                ", balance=" + balance +
                ", type=" + type +
                ", ic_type='" + ic_type + '\'' +
                ", account='" + account + '\'' +
                ", default_state=" + default_state +
                ", state=" + state +
                ", smallerThan=" + smallerThan +
                ", greaterThan=" + greaterThan +
                ", giving_balance=" + giving_balance +
                ", audit=" + audit +
                ", order_number='" + order_number + '\'' +
                ", adv_duration=" + adv_duration +
                ", start_delivery_time='" + start_delivery_time + '\'' +
                ", end_delivery_time='" + end_delivery_time + '\'' +
                ", remark='" + remark + '\'' +
                ", red_envelopes_number=" + red_envelopes_number +
                ", scope_type=" + scope_type +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", image3='" + image3 + '\'' +
                ", integral=" + integral +
                ", version='" + version + '\'' +
                ", url='" + url + '\'' +
                ", qr_code='" + qr_code + '\'' +
                ", user=" + user +
                ", listEqp=" + listEqp +
                ", logo=" + logo +
                ", introduction=" + introduction +
                ", svVideoComment=" + svVideoComment +
                '}';
    }
}