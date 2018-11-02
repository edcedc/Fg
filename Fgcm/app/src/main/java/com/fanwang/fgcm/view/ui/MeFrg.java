package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.MeAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FMeBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.utils.GlideImageUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/4/13.
 * 我的
 */

public class MeFrg extends BaseFragment<FMeBinding> implements MeAdapter.OnClickListener, View.OnClickListener {

    private List<DataBean> listBean = new ArrayList<>();
    private MeAdapter adapter;

    private int[] imgs = {R.mipmap.e02, R.mipmap.e03, R.mipmap.e04, R.mipmap.e05, R.mipmap.e07,
            R.mipmap.e08, R.mipmap.e09, R.mipmap.e10, R.mipmap.e11};

    public static MeFrg newInstance() {
        Bundle args = new Bundle();
        MeFrg fragment = new MeFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_me;
    }

    @Override
    protected void initView(View view) {
        showRecyclerView();
        adapter.setOnClickListener(this);
        mB.lyMe.setOnClickListener(this);
        mB.btZXing.setOnClickListener(this);
        mB.refreshLayout.setEnableLoadmore(false);
        mB.refreshLayout.startRefresh();
        setSwipeBackEnable(false);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                ajaxUserData();
            }
        });
        EventBusActivityScope.getDefault(act).register(this);
    }

    private boolean isInvestment = false;
    private void ajaxUserData() {
        CloudApi.userInformation()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        if (jsonObject.optInt("code") == Code.CODE_SUCCESS){
                            JSONObject data = jsonObject.optJSONObject("data");
                            if (data != null){
                                JSONObject userExtend = data.optJSONObject("userExtend");
                                if (userExtend != null){
                                    mB.tvName.setText(userExtend.optString("nick"));
                                    User.getInstance().setLogin(true);
                                    User.getInstance().setUserObj(data);
                                    GlideImageUtils.load(act, CloudApi.SERVLET_URL + userExtend.optString("head"), mB.ivHead);
                                    User.getInstance().setUserInformation(data);
                                    showUserData();

                                    if (!isInvestment){
                                        isInvestment = true;
                                        JSONObject userObj = User.getInstance().getUserInformation();
                                        int investment = userObj.optInt("investment");
                                        int agent = userObj.optInt("agent");
                                        int merchant = userObj.optInt("merchant");
                                        if (investment == 0 && agent == 0 && merchant == 0){
                                            listBean.remove(2);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mB.refreshLayout.finishRefreshing();
                    }
                });
    }

    @Subscribe
    public void onMainThreadInEvent(BundleDataInEvent event){
        if (event.getType() == BundleDataInEvent.me_update){
            ajaxUserData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusActivityScope.getDefault(act).unregister(this);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        showUserData();
    }

    private void showUserData() {
        JSONObject data = User.getInstance().getUserInformation();
        if (data != null){
            JSONObject userExtend = data.optJSONObject("userExtend");
            String nick = userExtend.optString("nick");
            if (!StringUtils.isEmpty(nick) && !"null".equals(nick)){
                mB.tvName.setText(nick);
                GlideImageUtils.load(act, CloudApi.SERVLET_URL + userExtend.optString("head"), mB.ivHead, true);
            }
        }
    }

    private void showRecyclerView() {
        String[] strs = {getString(R.string.wallet), getString(R.string.release_record), getString(R.string.equipment),
                getString(R.string.video_works), getString(R.string.integral_record), getString(R.string.online_service),
                getString(R.string.about_us), getString(R.string.download), getString(R.string.set)};
        for (int i = 0; i < imgs.length; i++) {
            DataBean bean = new DataBean();
            bean.setImg(imgs[i]);
            bean.setTitle(strs[i]);
            listBean.add(bean);
        }
//        listBean.remove(4);
        if (adapter == null) {
            adapter = new MeAdapter(act, listBean, true);
        }
        mB.recyclerView.setLayoutManager(new LinearLayoutManager(act));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v, int position) {
        DataBean bean = listBean.get(position);
        switch (bean.getTitle()) {
            case "钱包"://钱包
                UIHelper.startWalletFrg(this);
                break;
            case "我的投放记录"://投放记录
                UIHelper.startReleaseRecordFrg(this);
                break;
            case "我的设备"://我的设备
                UIHelper.startMyEquFrg(this);
                break;
            case "我的视频作品"://我的视频作品
                UIHelper.startMyVideoFrg(this);
                break;
            case "积分记录"://积分记录
                UIHelper.startIntegralRecordFrg(this);
                break;
            case "在线客服"://在线客服
                UIHelper.startCustomerFrg(this);
                break;
            case "关于我们"://关于我们
                UIHelper.startAboutFrg(this);
                break;
            case "下载"://下载
                UIHelper.startDownloadFrg(this);
                break;
            case "设置"://设置
                UIHelper.startSetFrg(this);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_me:
//                String url = "1";
//                Bitmap bitmap = ZXingUtils.createQRImage(url, mB.ivHead.getWidth(), mB.ivHead.getHeight());
//                mB.ivHead.setImageBitmap(bitmap);
                UIHelper.startMeUpdateFrg(this);
                break;
            case R.id.bt_ZXing:
                UIHelper.startNoticePageFrg(this);
                break;
        }
    }
}
