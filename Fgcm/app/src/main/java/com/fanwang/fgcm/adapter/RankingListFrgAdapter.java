package com.fanwang.fgcm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanwang.fgcm.view.ui.EncloureFrg;
import com.fanwang.fgcm.view.ui.HotFrg;
import com.fanwang.fgcm.view.ui.RankingMonthFrg;
import com.fanwang.fgcm.view.ui.RankingWeekFrg;


/**
 * Created by YoKeyword on 16/6/5.
 */
public class RankingListFrgAdapter extends FragmentPagerAdapter {
    private String[] mTitles;

    public RankingListFrgAdapter(FragmentManager fm, String... titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RankingWeekFrg.newInstance();
            default:
                return RankingMonthFrg.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
