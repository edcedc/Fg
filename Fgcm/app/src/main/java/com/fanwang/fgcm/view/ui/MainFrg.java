package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.databinding.FMainBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.weight.buttonBar.BottomBar;
import com.fanwang.fgcm.weight.buttonBar.BottomBarTab;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by edison on 2018/4/12.
 *  首页
 */

public class MainFrg extends BaseFragment<FMainBinding> {

    public static MainFrg newInstance() {
        Bundle args = new Bundle();
        MainFrg fragment = new MainFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initParms(Bundle bundle) {

    }
    private final int FIRST = 0;
    private final int SECOND = 1;
    private final int THIRD = 2;
    private final int FOURTH = 3;
    private final int FIFTH = 4;

    private SupportFragment[] mFragments = new SupportFragment[5];

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment firstFragment = findChildFragment(VideoFrg.class);
        if (firstFragment == null) {
            mFragments[FIRST] = VideoFrg.newInstance();
            mFragments[SECOND] = LookAdvertisementFrg.newInstance();
            mFragments[THIRD] = ThrowAdvertisementFrg.newInstance();
            mFragments[FOURTH] = OnDemandFrg.newInstance();
            mFragments[FIFTH] = MeFrg.newInstance();

            loadMultipleRootFragment(R.id.fl_tab_container,
                    FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH],
                    mFragments[FIFTH]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findChildFragment(LookAdvertisementFrg.class);
            mFragments[THIRD] = findChildFragment(ThrowAdvertisementFrg.class);
            mFragments[FOURTH] = findChildFragment(OnDemandFrg.class);
            mFragments[FIFTH] = findChildFragment(MeFrg.class);
        }
        setSofia(false);
        setSwipeBackEnable(false);
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_main;
    }

    @Override
    protected void initView(View view) {
        mB.bottomBar
                .addItem(new BottomBarTab(_mActivity, R.mipmap.b13, getString(R.string.bottomBarTab_video)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.b10, getString(R.string.bottomBarTab_advertisement)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.b11, getString(R.string.bottomBarTab_throw_advertisement)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.b79, getString(R.string.bottomBarTab_on_demand)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.b12, getString(R.string.bottomBarTab_me)));
        mB.bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
                /*BottomBarTab tab = mB.bottomBar.getItem(FIRST);
                if (position == FIRST) {
                    tab.setUnreadCount(0);
                } else {
                    tab.setUnreadCount(tab.getUnreadCount() + 1);
                }*/

                if (position == SECOND){
                    EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.LookAdvertisementList));
//                    DataSupport.deleteAll(SearchUserIdBean.class);
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
//                EventBusActivityScope.getDefault(_mActivity).post(new TabSelectedEvent(position));
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        exitBy2Click();// 调用双击退出函数
//        return super.onBackPressedSupport();
        return true;
    }

    private Boolean isExit = false;

    private void exitBy2Click() {
        Handler tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            showToast("再按一次退出程序");
            tExit = new Handler();
            tExit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
            return;
        } else {
//            Cockroach.uninstall();
            act.finish();
            System.exit(0);
        }
    }


    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }

}
