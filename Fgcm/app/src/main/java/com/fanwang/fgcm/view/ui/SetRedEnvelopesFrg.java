package com.fanwang.fgcm.view.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.databinding.FSetRedBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.presenter.SetRedEnvelopesPresenterlmpl;
import com.fanwang.fgcm.utils.DialogUtil;
import com.fanwang.fgcm.view.impl.SetRedEnvelopesView;
import com.fanwang.fgcm.weight.DecimalDigitsInputFilter;
import com.fanwang.fgcm.weight.videorecord.utils.TXUGCPublish;
import com.fanwang.fgcm.weight.videorecord.utils.TXUGCPublishTypeDef;
import com.flyco.roundview.RoundViewDelegate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/4/23.
 *  设置红包
 */

public class SetRedEnvelopesFrg extends BaseFragment<FSetRedBinding> implements SetRedEnvelopesView, View.OnClickListener,TXUGCPublishTypeDef.ITXVideoPublishListener{

    private SetRedEnvelopesPresenterlmpl presenter;

    private int red_envelopes_state = 1;
    private String imgUrl;
    private String videoUrl;
    private String videoDesc;
    private TXUGCPublish mVideoPublish;
    private ProgressDialog dialog;

    @Override
    protected void initParms(Bundle bundle) {
        imgUrl = bundle.getString("imgUrl");
        videoUrl = bundle.getString("videoUrl");
        videoDesc = bundle.getString("text");
//        LogUtils.e(imgUrl, videoUrl);
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_set_red;
    }

    @Override
    protected void initView(View view) {
        presenter = new SetRedEnvelopesPresenterlmpl(this);
        dialog = new ProgressDialog(act);
        mB.refreshLayout.setPureScrollModeOn();
        setToolbarTitle(getString(R.string.top_set_packet));
        mB.tvRebType.setOnClickListener(this);
        mB.btSubmit.setOnClickListener(this);
        EventBus.getDefault().register(this);
        EventBusActivityScope.getDefault(act).register(this);
        mB.etNumber.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
        mB.etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String number = editable.toString();
                String individual = mB.etIndividual.getText().toString();
                if (mRedType == 1) {
                    if (!StringUtils.isEmpty(number) && !StringUtils.isEmpty(individual)) {
                        Double iNum = Double.valueOf(number);
                        Integer iInd = Integer.valueOf(individual);
                        mB.tvNumber.setText((iNum * iInd) + "");
                    } else if (!StringUtils.isEmpty(number) && !number.equals(".")) {
                        if (Double.valueOf(number) > 0) {
                            mB.tvNumber.setText(number);
                        }
                    }else {
                        mB.tvNumber.setText("0.0");
                    }
                } else {
                    if (!StringUtils.isEmpty(number)) {
                        mB.tvNumber.setText(number);
                    }
                }
            }
        });
        mB.etIndividual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String individual = editable.toString();
                String number = mB.etNumber.getText().toString();
                if (mRedType == 1) {
                    if (!StringUtils.isEmpty(number) && !StringUtils.isEmpty(individual)) {
                        Double iNum = Double.valueOf(number);
                        Integer iInd = Integer.valueOf(individual);
                        mB.tvNumber.setText((double) (iNum * iInd) + "");
                    }else {
                        mB.tvNumber.setText(number);
                    }
                } else {
                    if (!StringUtils.isEmpty(number)) {
                        mB.tvNumber.setText(number);
                    }
                }
            }
        });
        if (mVideoPublish == null) {
            mVideoPublish = new TXUGCPublish(act, User.getInstance().getUserId());
            mVideoPublish.setListener(this);
        }

    }

    @Subscribe
    public void OnMainThreadInEvent(BundleDataInEvent event){
       if (event.getType() == BundleDataInEvent.pay || event.getType() == BundleDataInEvent.wx_pay){
            pop();
            pop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        EventBusActivityScope.getDefault(act).unregister(this);
        mVideoPublish.canclePublish();
        if (dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_reb_type:
                if (red_envelopes_state == 1){
                    red_envelopes_state = 2;
                    showRandomAmount();
                }else {
                    red_envelopes_state = 1;
                    showFixedAmount();
                }
                break;
            case R.id.bt_submit:
                DialogUtil.showSelectEquipmentModel(act, DialogUtil.showSelectListType_2, new DialogUtil.OnClickListener() {
                    @Override
                    public void onClick(int position) {
                        payType = position;
                        presenter.submit(act, red_envelopes_state, mB.etIndividual.getText().toString(), mB.etNumber.getText().toString(),
                                imgUrl,position, videoUrl, videoDesc, mB.etUrl.getText().toString());
                    }
                });
                break;
        }
    }
    private int payType = -1;

    private int mRedType = 1;
    @Override
    public void showFixedAmount() {
        mRedType = 1;
        mB.tvAmountType.setText(getText(R.string.set_single_amount));
        mB.tvDraw.setText(getText(R.string.set_red_Per_person_Fixed));
        mB.tvRebType.setText(getText(R.string.set_red_Fixed_amount));
        String number = mB.etNumber.getText().toString();
        String individual = mB.etIndividual.getText().toString();
        if (!StringUtils.isEmpty(number) && !StringUtils.isEmpty(individual)) {
            Double iNum = Double.valueOf(number);
            Integer iInd = Integer.valueOf(individual);
            mB.tvNumber.setText((iNum * iInd) + "");
        }else {
            mB.tvNumber.setText(number);
        }
    }

    @Override
    public void showRandomAmount() {
        mRedType = 2;
        mB.tvAmountType.setText(getText(R.string.set_red_Total_amount));
        mB.tvDraw.setText(getText(R.string.set_red_Random_amount));
        mB.tvRebType.setText(getText(R.string.set_red_Per_person_Random));
        String number = mB.etNumber.getText().toString();
        String individual = mB.etIndividual.getText().toString();
        mB.etNumber.setText(number);
        mB.tvNumber.setText(number);
    }

    @Override
    public void onSuccess(String into, int payType) {
        dialog.dismiss();
        if (payType == 0){
            pay(into);
        }else {
            try {
                wxPay(new JSONObject(into));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSignatureGetSucces(int red_envelopes_state, String redEnvelopesNumber, String redEnvelopes, String imgurl, int position, String videoUrl, String videoDesc, String sigId) {
        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        // signature计算规则可参考 https://www.qcloud.com/document/product/266/9221
        param.signature = sigId;
        param.videoPath = videoUrl;
        param.coverPath = imgurl;
        param.enableResume = true;
        param.enableHttps = false;
        param.fileName= com.blankj.utilcode.util.TimeUtils.getNowMills() + "";
        LogUtils.e(sigId, videoUrl, imgurl);
        int publishCode = mVideoPublish.publishVideo(param);
        if (publishCode != 0) {
            showToast("发布失败，错误码：" + publishCode);
        }
    }

    private void showSubmitEnabled(boolean isEnabled) {
        RoundViewDelegate delegate = mB.btSubmit.getDelegate();
        if (isEnabled == true){
            delegate.setBackgroundColor(getResources().getColor(R.color.violet_773FE3));
            delegate.setBackgroundPressColor(getResources().getColor(R.color.violet_562EA6));
        }else {
            delegate.setBackgroundColor(getResources().getColor(R.color.violet_B59DEE));
        }
        mB.btSubmit.setEnabled(isEnabled);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onPublishProgress(long uploadBytes, long totalBytes) {
        LogUtils.e((int) (100*uploadBytes/totalBytes));
        dialog.setMessage("正在上传中" + (int) (100*uploadBytes/totalBytes) + "%");
        if (!dialog.isShowing()){
            dialog.show();
        }
    }

    @Override
    public void onPublishComplete(TXUGCPublishTypeDef.TXPublishResult result) {
        LogUtils.e(result.retCode + " Msg:" + (result.retCode == 0 ? result.videoURL : result.descMsg));
        dialog.setMessage("正在请求网络中...");
        if (result.retCode == 0){
            presenter.submit2(act, red_envelopes_state, mB.etIndividual.getText().toString(), mB.etNumber.getText().toString(),
                    imgUrl,payType, result.videoURL, videoDesc, mB.etUrl.getText().toString());
        }else {
            showToast(result.descMsg);
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (dialog.isShowing()){
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
