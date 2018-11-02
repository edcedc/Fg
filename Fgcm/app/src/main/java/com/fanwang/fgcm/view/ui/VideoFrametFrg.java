package com.fanwang.fgcm.view.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.VideoFrametAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FVideoFrameBinding;
import com.fanwang.fgcm.presenter.VideoFrametPresenterlmpl;
import com.fanwang.fgcm.utils.DialogUtil;
import com.fanwang.fgcm.utils.GlideImageUtils;
import com.fanwang.fgcm.view.impl.VideoFrametView;
import com.fanwang.fgcm.weight.videorecord.utils.TXUGCPublish;
import com.fanwang.fgcm.weight.videorecord.utils.TXUGCPublishTypeDef;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/23.
 * 获取视频帧
 */

public class VideoFrametFrg extends BaseFragment<FVideoFrameBinding> implements TXUGCPublishTypeDef.ITXVideoPublishListener, VideoFrametView {

    private ArrayList<String> listBean;
    private VideoFrametAdapter adapter;
    private String data;

    private TXUGCPublish mVideoPublish;
    private ProgressDialog dialog;
    private VideoFrametPresenterlmpl presenter;
    private String contont;

    @Override
    protected void initParms(Bundle bundle) {
        listBean = bundle.getStringArrayList("listImg");
        data = bundle.getString("videoUrl");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_video_frame;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.top_choose_cover), getString(R.string.confirm));
        presenter = new VideoFrametPresenterlmpl(this);
        listBean.remove(listBean.size() - 1);
        if (adapter == null) {
            adapter = new VideoFrametAdapter(act, listBean);
        }
        mB.recyclerView.setLayoutManager(new GridLayoutManager(act, 4, GridLayoutManager.VERTICAL, false));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new VideoFrametAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                String img = listBean.get(position);
                GlideImageUtils.load(act, img, mB.ivImg);
                adapter.setSelectPosition(position);
                adapter.notifyDataSetChanged();
            }
        });
        GlideImageUtils.load(act, listBean.get(0), mB.ivImg);
        dialog = new ProgressDialog(act);
        if (mVideoPublish == null) {
            mVideoPublish = new TXUGCPublish(act, User.getInstance().getUserId());
            mVideoPublish.setListener(this);
        }
    }

    @Override
    protected void setOnRightClickListener() {
        super.setOnRightClickListener();
        DialogUtil.showEdit(act, new DialogUtil.OnEditClickListener() {
            @Override
            public void onClick(final String text) {
                DialogUtil.showSelectEquipmentModel(act, DialogUtil.showSelectListType_1, new DialogUtil.OnClickListener() {
                    @Override
                    public void onClick(int position) {
                        contont = text;
                        if (position == 0) {
//                            LogUtils.e(listBean.get(adapter.getSelectPosition() == -1 ? 0 : adapter.getSelectPosition()));
                            presenter.signatureGet();
                        } else {
                            String img = listBean.get(adapter.getSelectPosition() == -1 ? 0 : adapter.getSelectPosition());
                            UIHelper.startSetRedEnvelopesFrg(VideoFrametFrg.this, img, data, text);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoPublish.canclePublish();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onPublishProgress(long uploadBytes, long totalBytes) {
        LogUtils.e((int) (100 * uploadBytes / totalBytes));
        dialog.setMessage("正在上传中" + (int) (100 * uploadBytes / totalBytes) + "%");
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onPublishComplete(TXUGCPublishTypeDef.TXPublishResult result) {
        LogUtils.e(result.retCode + " Msg:" + (result.retCode == 0 ? result.videoURL : result.descMsg));
        dialog.setMessage("正在请求网络中...");
        if (result.retCode == 0) {
            CloudApi.videoSave("", result.videoURL, listBean.get(adapter.getSelectPosition() == -1 ? 0 : adapter.getSelectPosition()),
                    0, -1, -1, -1, contont, act, new CloudApi.OnVideoSaveListener() {

                        @Override
                        public void addDisposable(Disposable d) {
                            VideoFrametFrg.super.addDisposable(d);
                        }

                        @Override
                        public void hideLoading() {
                            VideoFrametFrg.this.hideLoading();
                        }

                        @Override
                        public void showLoading() {
                        }

                        @Override
                        public void onComplete() {
                            VideoFrametFrg.this.hideLoading();
                            LogUtils.e("onComplete");
                            pop();
                        }

                        @Override
                        public void onComplete2(String data) {
                            VideoFrametFrg.this.hideLoading();
                            LogUtils.e("onComplete2");
                            pop();
                        }
                    });
        } else {
            showToast(result.descMsg);
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onSignatureGetSucces(String sigId) {
        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        // signature计算规则可参考 https://www.qcloud.com/document/product/266/9221
        param.signature = sigId;
        param.videoPath = data;
        param.coverPath = listBean.get(adapter.getSelectPosition() == -1 ? 0 : adapter.getSelectPosition());
        param.enableResume = true;
        param.enableHttps = false;
        param.fileName= com.blankj.utilcode.util.TimeUtils.getNowMills() + "";
        LogUtils.e(sigId, data, listBean.get(adapter.getSelectPosition() == -1 ? 0 : adapter.getSelectPosition()));
        int publishCode = mVideoPublish.publishVideo(param);
        if (publishCode != 0) {
            showToast("发布失败，错误码：" + publishCode);
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        mVideoPublish.canclePublish();
        hideLoading();
        return super.onBackPressedSupport();
    }
}
