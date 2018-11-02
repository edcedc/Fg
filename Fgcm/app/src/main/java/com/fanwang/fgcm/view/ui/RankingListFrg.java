package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.RankingListFrgAdapter;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.databinding.FRankingListBinding;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by edison on 2018/4/16.
 * 排行榜
 */

public class RankingListFrg extends BaseFragment<FRankingListBinding> {

    public static RankingListFrg newInstance() {
        Bundle args = new Bundle();
        RankingListFrg fragment = new RankingListFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_ranking_list;
    }

    @Override
    protected void initView(View view) {
        setToolbarTitle(getString(R.string.top_ranking_list));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mB.viewPager.setAdapter(new RankingListFrgAdapter(getChildFragmentManager()
                , getString(R.string.top_ranking_week), getString(R.string.top_ranking_month)));
        mB.tab.setupWithViewPager(mB.viewPager);
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }
}
