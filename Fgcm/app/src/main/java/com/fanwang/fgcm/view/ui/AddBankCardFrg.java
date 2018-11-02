package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.view.View;

import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.databinding.FAddBankCardBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.presenter.AddBankCardPresenter;
import com.fanwang.fgcm.presenter.AddBankCardPresenterlmpl;
import com.fanwang.fgcm.utils.DataListTool;
import com.fanwang.fgcm.utils.TopRightMenuTool;
import com.fanwang.fgcm.view.impl.AddBankCardView;
import com.fanwang.fgcm.weight.toprightmenu.MenuItem;
import com.fanwang.fgcm.weight.toprightmenu.TopRightMenu;

import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/5/7.
 *  新增银行卡
 */

public class AddBankCardFrg extends BaseFragment<FAddBankCardBinding> implements View.OnClickListener, AddBankCardView {

    private AddBankCardPresenter presenterlmpl;

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_add_bank_card;
    }

    @Override
    protected void initView(View view) {
        presenterlmpl = new AddBankCardPresenterlmpl(this);
        setToolbarTitle(getString(R.string.top_add_card));
        mB.tvOpeningBank.setOnClickListener(this);
        mB.btSubmit.setOnClickListener(this);
        mB.refreshLayout.setPureScrollModeOn();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_opening_bank://选择银行
//                presenterlmpl.openingBank(mB.etName.getText().toString(), mB.etCardNumber.getText().toString());
                break;
            case R.id.bt_submit:
                presenterlmpl.submit(mB.etName.getText().toString(), mB.etCardNumber.getText().toString(), mB.tvOpeningBank.getText().toString(), mB.cbSelected.isChecked());
                break;
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    private int mSelectBank;
    @Override
    public void openingBank() {
        final List<MenuItem> menuItems = DataListTool.getCardList();
        TopRightMenuTool.TopRightMenu(act, menuItems, mB.tvOpeningBank, 0, new TopRightMenu.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position) {
                MenuItem item = menuItems.get(position);
                mB.tvOpeningBank.setText(item.getText());
                mSelectBank = position;
            }
        });
    }

    @Override
    public void onSuccess(DataBean data) {
        EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.BankCardListFrg, data));
        pop();
    }
}
