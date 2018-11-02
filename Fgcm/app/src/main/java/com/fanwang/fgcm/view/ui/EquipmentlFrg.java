package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.MyPagerAdapter;
import com.fanwang.fgcm.adapter.TabEntity;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.databinding.FEquipmentBinding;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by edison on 2018/5/7.
 *  设备
 */

public class EquipmentlFrg extends BaseFragment<FEquipmentBinding> {

    private int mType;

    private String[] mTitles = {"全部", "待处理", "审核通过", "审核不通过"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String equId;

    @Override
    protected void initParms(Bundle bundle) {
        mType = bundle.getInt("type");
        equId = bundle.getString("equId");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_equipment;
    }

    @Override
    protected void initView(View view) {
        setSwipeBackEnable(false);
        switch (mType){
            case 0:
                setToolbarTitle(getString(R.string.mobile_devices), false);
                break;
            case 1:
                setToolbarTitle(getString(R.string.terminal_devices), false);
                break;
            case 2:
                setToolbarTitle(getString(R.string.equipment), false);
                break;
        }

        mFragments.add(new EquipmentlChildFrg(mType, -1, equId));
        mFragments.add(new EquipmentlChildFrg(mType, 0, equId));
        mFragments.add(new EquipmentlChildFrg(mType, 1, equId));
        mFragments.add(new EquipmentlChildFrg(mType, 2, equId));
        mB.viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), mFragments));
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        mB.tablayout.setTabData(mTabEntities);
        mB.tablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mB.viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mB.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mB.tablayout.setCurrentTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mB.viewPager.setCurrentItem(0);
        mB.viewPager.setOffscreenPageLimit(3);
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }

}
