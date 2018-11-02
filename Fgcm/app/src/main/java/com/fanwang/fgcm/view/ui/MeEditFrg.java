package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.databinding.FMeEditBinding;
import com.fanwang.fgcm.presenter.MeUpdatePresenterlmpl;
import com.fanwang.fgcm.view.impl.MeUpdateView;

/**
 * Created by edison on 2018/5/7.
 *  我的 修改
 */

public class MeEditFrg extends BaseFragment<FMeEditBinding> implements MeUpdateView {

    private int type;
    private MeUpdatePresenterlmpl presenter;
    private int mRB;
    private String data;

    @Override
    protected void initParms(Bundle bundle) {
        type = bundle.getInt("type");
        data = bundle.getString("data");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_me_edit;
    }

    @Override
    protected void setOnRightClickListener() {
        super.setOnRightClickListener();
        String trim = mB.etText.getText().toString().trim();
        if (trim.length() > 15)return;
        switch (type){
            case 0:
                if (data.equals(trim)){
                    pop();
                }
                if (StringUtils.isEmpty(trim)){
                    mB.etText.setError(getString(R.string.empty_text));
                    return;
                }
                presenter.onNick(trim);
                break;
            case 1:
                if (data.equals(trim)){
                    pop();
                }
                if (StringUtils.isEmpty(trim)){
                    mB.etText.setError(getString(R.string.empty_text));
                    return;
                }
                presenter.onModel(trim);
                break;
            case 2:
                if (mRB == 0){
                    showToast("请选择性别");
                    return;
                }
                presenter.onSex(mRB);
                break;
        }

    }

    @Override
    protected void initView(View view) {
        presenter = new MeUpdatePresenterlmpl(this);
        mB.refreshLayout.setPureScrollModeOn();
        switch (type){
            case 0:
                setToolbarTitle(getString(R.string.update_nick), getString(R.string.preservation));
                mB.etText.setVisibility(View.VISIBLE);
                mB.etText.setHint(getString(R.string.update_nick));
                mB.etText.setText(data);
                break;
            case 1:
                setToolbarTitle(getString(R.string.update_phone), getString(R.string.preservation));
                mB.etText.setVisibility(View.VISIBLE);
                mB.etText.setHint(getString(R.string.update_phone));
                mB.etText.setInputType(InputType.TYPE_CLASS_NUMBER);
                mB.etText.setMaxLines(11);
                mB.etText.setText(data);
                break;
            case 2:
                setToolbarTitle(getString(R.string.update_sex), getString(R.string.preservation));
                mB.lySex.setVisibility(View.VISIBLE);
                if (data.equals("1")){
                    mB.rbMan.setChecked(true);
                }else if (data.equals("2")){
                    mB.rvWoman.setChecked(true);
                }
                mB.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.rb_man:
                                mRB = 1;
                                break;
                            case R.id.rv_woman:
                                mRB = 2;
                                break;
                        }
                    }
                });
                break;
        }

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void showHead(String path) {

    }

    @Override
    public void onSuccess() {
//        EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.me_update));
        pop();
    }
}
