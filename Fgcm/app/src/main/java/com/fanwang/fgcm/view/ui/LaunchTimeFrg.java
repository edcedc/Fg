package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.databinding.FLaunchTimeBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.utils.PickerUtils;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/5/2.
 *  投放时间
 */

public class LaunchTimeFrg extends BaseFragment<FLaunchTimeBinding>implements View.OnClickListener{

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_launch_time;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.top_launch_time));
        mB.refreshLayout.setPureScrollModeOn();
        mB.lyStartTime.setOnClickListener(this);
        mB.lyEndTime.setOnClickListener(this);
        mB.btSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_start_time:
                PickerUtils.setPickerData(act, new PickerUtils.OnDatePickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        mB.tvStartTime.setText(year + "-" + month + "-" + day);
                        mB.tvTime.setText("");
                    }
                });
                break;
            case R.id.ly_end_time:
                String time = mB.tvStartTime.getText().toString();
                if (StringUtils.isEmpty(time)){
                    mB.tvStartTime.setError(getString(R.string.empty_time));
                    return;
                }
                PickerUtils.setPickerData(act, new PickerUtils.OnDatePickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        mB.tvEndTime.setText(year + "-" + month + "-" + day);
                        mB.tvTime.setText(mB.tvStartTime.getText().toString() + " ~ " + mB.tvEndTime.getText().toString());
                    }
                });
                break;
            case R.id.bt_submit:
                String startTime = mB.tvStartTime.getText().toString();
                String endTime = mB.tvEndTime.getText().toString();
                if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)){
                    showToast(getString(R.string.empty_time));
                    return;
                }
                StringBuffer sb = new StringBuffer();
                sb.append(mB.tvTime.getText().toString());
                EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.launch_time, sb.toString()));
                pop();
                break;
        }
    }
}
