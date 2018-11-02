package com.fanwang.fgcm.view.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.ImageAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FPhoneDetailsBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.presenter.PhoneDetailsPresenter;
import com.fanwang.fgcm.presenter.PhoneDetailsPresenterlmpl;
import com.fanwang.fgcm.utils.DataListTool;
import com.fanwang.fgcm.utils.PictureSelectorTool;
import com.fanwang.fgcm.utils.TopRightMenuTool;
import com.fanwang.fgcm.view.impl.PhoneDetailsView;
import com.fanwang.fgcm.weight.DecimalDigitsInputFilter;
import com.fanwang.fgcm.weight.FullyGridLayoutManager;
import com.fanwang.fgcm.weight.toprightmenu.MenuItem;
import com.fanwang.fgcm.weight.toprightmenu.TopRightMenu;
import com.fanwang.fgcm.weight.videorecord.utils.TXUGCPublish;
import com.fanwang.fgcm.weight.videorecord.utils.TXUGCPublishTypeDef;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/4/26.
 * 投放详情
 */

public class PhoneDetailsFrg extends BaseFragment<FPhoneDetailsBinding> implements PhoneDetailsView, View.OnClickListener,TXUGCPublishTypeDef.ITXVideoPublishListener {

    private int launchType;//选择上传类型
    private List<MenuItem> menuItems = DataListTool.getPublishingModeList();
    private ImageAdapter imageAdapter;
    private List<LocalMedia> localMediaList = new ArrayList<>();
    private int mSelectType = -1;
    private int mRedType = 1;//红包算法，1固定红包，2随机红包
    private PhoneDetailsPresenter presenter;
    private double mLatitude;
    private double mLongitude;
    private String mProvince;
    private String mCity;
    private String mDistrict;
    private String mRange;
    private int mKilometre;

    private ProgressDialog dialog;
    private TXUGCPublish mVideoPublish;

    @Override
    protected void initParms(Bundle bundle) {
        launchType = bundle.getInt("distance");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_phone_details;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.top_launch_details));
        presenter = new PhoneDetailsPresenterlmpl(this);
        mB.refreshLayout.setPureScrollModeOn();
        mB.tvAdvertisementType.setOnClickListener(this);
        mB.tvRebType.setOnClickListener(this);
        mB.btSubmit.setOnClickListener(this);
        mB.lyAddress.setOnClickListener(this);
        mB.etNumber.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});
        if (imageAdapter == null) {
            imageAdapter = new ImageAdapter(act, new ImageAdapter.onAddPicClickListener() {
                @Override
                public void onAddPicClick() {
                    if (mSelectType == 1) {
                        imageAdapter.setSelectMax(3);
                        PictureSelectorTool.PictureSelectorImage(act, localMediaList, 3, BundleDataInEvent.select_img);
                    } else if (mSelectType == 0) {
                        imageAdapter.setSelectMax(1);
                        PictureSelectorTool.PictureSelectorVideo(act, BundleDataInEvent.select_video);
                    }
                }
            });
        }
        mB.recyclerView.setLayoutManager(new FullyGridLayoutManager(act, 4, GridLayoutManager.VERTICAL, false));
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PictureSelectorTool.PictureMediaType(act, localMediaList, position);
            }
        });
        EventBusActivityScope.getDefault(act).register(this);
        EventBus.getDefault().register(this);
        mB.etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String number = editable.toString();
                String individual = mB.etIndividual.getText().toString();
                if (mRedType == 1) {
                    if (!StringUtils.isEmpty(number) && !StringUtils.isEmpty(individual)) {
                        Double iNum = Double.valueOf(number);
                        Integer iInd = Integer.valueOf(individual);
                        mB.tvNumber.setText((double) (iNum * iInd) + "");
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
        mB.etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mB.tvTextSize.setText(editable.length() + "/40");
                if (editable.length() > 40){
                    return;
                }
            }
        });
        dialog = new ProgressDialog(act);
        if (mVideoPublish == null) {
            mVideoPublish = new TXUGCPublish(act, User.getInstance().getUserId());
            mVideoPublish.setListener(this);
        }
    }

    @Subscribe
    public void onEventMainThread(BundleDataInEvent event) {
        if (event.getType() == BundleDataInEvent.select_img || event.getType() == BundleDataInEvent.select_video) {
            localMediaList.addAll(PictureSelector.obtainMultipleResult((Intent) event.getObject()));
            imageAdapter.setList(localMediaList);
            imageAdapter.notifyDataSetChanged();
        } else if (event.getType() == BundleDataInEvent.launch_details) {
            Bundle bundle = (Bundle) event.getObject();
            mLatitude = bundle.getDouble("latitude");
            mLongitude = bundle.getDouble("longitude");
            mProvince = bundle.getString("province");
            mCity = bundle.getString("city");
            mDistrict = bundle.getString("district");
            mKilometre = bundle.getInt("kilometre");
            String address = bundle.getString("address");
            mRange = bundle.getString("range");
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mB.tvRange.setText(mRange);
                }
            });
            LogUtils.e(mLatitude, mLongitude, mProvince, mCity, mDistrict, mRange);
        } else if (event.getType() == BundleDataInEvent.pay || event.getType() == BundleDataInEvent.wx_pay) {
            pop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusActivityScope.getDefault(act).unregister(this);
        EventBus.getDefault().unregister(this);
        mVideoPublish.canclePublish();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_advertisement_type://广告类型
                TopRightMenuTool.TopRightMenu(act, menuItems, mB.tvAdvertisementType, 200, new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        MenuItem item = menuItems.get(position);
                        mB.tvAdvertisementType.setText(item.getText());
                        mSelectType = position;
                        switch (position){
                            case 0:
                                mB.tvMaterialType.setText(getText(R.string.select_video));
                                mB.lyText.setVisibility(View.GONE);
                                mB.ryRecyclerView.setVisibility(View.VISIBLE);
                                localMediaList.clear();
                                imageAdapter.notifyDataSetChanged();
                                break;
                            case 1:
                                mB.tvMaterialType.setText(getText(R.string.select_img));
                                mB.lyText.setVisibility(View.GONE);
                                mB.ryRecyclerView.setVisibility(View.VISIBLE);
                                localMediaList.clear();
                                imageAdapter.notifyDataSetChanged();
                                break;
                            case 2:
                            case 3:
                                mB.tvMaterialType.setText("        素材:");
                                mB.lyText.setVisibility(View.VISIBLE);
                                mB.ryRecyclerView.setVisibility(View.GONE);
                                break;
                        }
                    }
                });
                break;
            case R.id.tv_reb_type://选择红包类型
                if (mRedType == 1) {
                    showRandomAmount();
                } else {
                    showFixedAmount();
                }
                break;
            case R.id.ly_address://选择位置
                UIHelper.startSelectAddressFrg(this);
                break;
            case R.id.bt_submit://提交
                presenter.submit(mSelectType, mB.etText.getText().toString(), localMediaList, mB.etNumber.getText().toString(),
                        mB.etIndividual.getText().toString(), mB.tvRange.getText().toString()
                        , mLongitude, mLatitude, mProvince, mCity, mDistrict, mKilometre, mRedType, act, mB.etUrl.getText().toString());
                break;
        }
    }

    @Override
    public void showFixedAmount() {
        mRedType = 1;
        mB.tvAmountType.setText(getText(R.string.set_single_amount));
        mB.tvDraw.setText(getText(R.string.set_red_Per_person_Fixed));
        mB.tvRebType.setText(getText(R.string.set_red_Fixed_amount));
        String number = mB.etNumber.getText().toString();
        String individual = mB.etIndividual.getText().toString();
        if (!StringUtils.isEmpty(number) && !StringUtils.isEmpty(individual)) {
            Integer iNum = Integer.valueOf(number);
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
    public void onSuccess(int payType, final String data) {
        if (payType == 0) {
            pay(data);
        } else {
            try {
                wxPay(new JSONObject(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSignatureGetSucces(String sigId) {
        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        // signature计算规则可参考 https://www.qcloud.com/document/product/266/9221
        param.signature = sigId;
        param.videoPath = localMediaList.get(0).getPath();
        param.enableResume = true;
        param.enableHttps = false;
        param.fileName= com.blankj.utilcode.util.TimeUtils.getNowMills() + "";
        LogUtils.e(sigId, localMediaList.get(0).getPath());
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

    @Override
    public void onError(Throwable e) {

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
            presenter.submit2(mSelectType, mB.etText.getText().toString(), localMediaList, mB.etNumber.getText().toString(),
                    mB.etIndividual.getText().toString(), mB.tvRange.getText().toString()
                    , mLongitude, mLatitude, mProvince, mCity, mDistrict, mKilometre, mRedType, act, result.videoURL, mB.etUrl.getText().toString());
        }else {
            showToast(result.descMsg);
        }
    }
}
