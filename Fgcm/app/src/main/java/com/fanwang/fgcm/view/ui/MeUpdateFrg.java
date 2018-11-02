package com.fanwang.fgcm.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FMeUpdateBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.presenter.MeUpdatePresenterlmpl;
import com.fanwang.fgcm.utils.GlideImageUtils;
import com.fanwang.fgcm.utils.PictureSelectorTool;
import com.fanwang.fgcm.view.impl.MeUpdateView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/5/7.
 *  我的
 */

public class MeUpdateFrg extends BaseFragment<FMeUpdateBinding> implements View.OnClickListener, MeUpdateView {

    private MeUpdatePresenterlmpl presenter;
    private JSONObject information;

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_me_update;
    }

    @Override
    protected void initView(View view) {
        presenter = new MeUpdatePresenterlmpl(this);
        setToolbarTitle(getString(R.string.top_me_));
        mB.refreshLayout.setPureScrollModeOn();
        mB.ivHead.setOnClickListener(this);
        mB.lyNick.setOnClickListener(this);
        mB.lyPhone.setOnClickListener(this);
        mB.lyMe.setOnClickListener(this);
        mB.lySex.setOnClickListener(this);
        EventBusActivityScope.getDefault(act).register(this);
    }

    @Subscribe
    public void onEventMainThread(BundleDataInEvent event) {
        if (event.getType() == BundleDataInEvent.me_update){

        }else if (event.getType() == BundleDataInEvent.select_img){
            List<LocalMedia> list = PictureSelector.obtainMultipleResult((Intent) event.getObject());
            presenter.onHead(list);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusActivityScope.getDefault(act).unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_head:
                PictureSelectorTool.PictureSelectorImage(act, BundleDataInEvent.select_img);
                break;
            case R.id.ly_nick:
                UIHelper.startMeEditFrg(this, 0, mB.tvNick.getText().toString());
                break;
            case R.id.ly_phone:
                UIHelper.startMeEditFrg(this, 1, mB.tvPhone.getText().toString());
                break;
            case R.id.ly_sex:
                UIHelper.startMeEditFrg(this, 2, mB.tvSex.getText().toString());
                break;
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        showUserData();
    }

    private void showUserData() {
        information = User.getInstance().getUserInformation();
        if (information != null){
            JSONObject userExtend = information.optJSONObject("userExtend");
            String head = userExtend.optString("head");
            if (!StringUtils.isEmpty(head) && !head.equals("null")){
                GlideImageUtils.load(act, CloudApi.SERVLET_URL + head, mB.ivHead, true);
            }
            mB.tvId.setText(userExtend.optString("user_id"));
            mB.tvIntegral.setText(userExtend.optString("integral"));
            String nick = userExtend.optString("nick");
            if (!StringUtils.isEmpty(nick) && !nick.equals("null")){
                mB.tvNick.setText(nick);
                mB.tvName.setText(nick);
            }
            String mobile = information.optString("username");
            if (!StringUtils.isEmpty(mobile) && !mobile.equals("null")){
                mB.tvPhone.setText(mobile);
            }
            if (userExtend.optInt("sex") != 0){
                mB.tvSex.setText(userExtend.optInt("sex") == 1 ? "男" : "女");
            }
        }
    }

    @Override
    public void showHead(String path) {
        GlideImageUtils.load(act, CloudApi.SERVLET_URL + path, mB.ivHead, true);
    }

    @Override
    public void onSuccess() {

    }

}
