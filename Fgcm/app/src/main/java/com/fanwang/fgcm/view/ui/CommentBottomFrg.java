package com.fanwang.fgcm.view.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.adapter.CommentBottomAdapter;
import com.fanwang.fgcm.base.BaseBottomSheetFrag;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.presenter.CommentBottomPresenter;
import com.fanwang.fgcm.presenter.CommentBottomPresenterlmpl;
import com.fanwang.fgcm.utils.BaseUtils;
import com.fanwang.fgcm.view.impl.CommentBottomView;
import com.fanwang.fgcm.weight.DateTool;
import com.fanwang.fgcm.weight.TextEditTextView;
import com.fanwang.fgcm.weight.WithScrollListView;
import com.flyco.roundview.RoundTextView;
import com.flyco.roundview.RoundViewDelegate;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.annotations.NonNull;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by edison on 2018/4/19.
 * 底部评论框
 */

public class CommentBottomFrg extends BaseBottomSheetFrag implements View.OnClickListener, CommentBottomView, AdapterView.OnItemClickListener {

    private TextView tvPlay;
    private TextView tvComment;
    private TextView tvTime;
    private TwinklingRefreshLayout refreshLayout;
    private WithScrollListView recyclerView;
    private NestedScrollView scrollView;

    private TextView etContent;

    private int pagerNumber = 1;
    private CommentBottomAdapter adapter;
    private List<DataBean> listBean = new ArrayList<>();

    private String id;
    private String create_time;
    private int playback;
    private int awc_number;

    private CommentBottomPresenter presenter;
    private RoundTextView btSend;

    private int commentState = 0;
    private int TwoPosition;//从第二层listView获取 主item 索引值
    private InputMethodManager imm;
    private View lyEdit;

    public void setCommentState(int commentState) {
        this.commentState = commentState;
    }

    private String videoCommentId;
    private JSONObject userExtend;

    public void setBundle(String id, String create_time, int playback, int awc_number) {
        this.id = id;
        this.create_time = create_time;
        this.playback = playback;
        this.awc_number = awc_number;
    }

    @Override
    public int bindLayout() {
        return R.layout.f_comment_bottom;
    }

    @Override
    public void initView(View view) {
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        presenter = new CommentBottomPresenterlmpl(this);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvPlay = view.findViewById(R.id.tv_play);
        tvComment = view.findViewById(R.id.tv_comment);
        tvTime = view.findViewById(R.id.tv_time);
        scrollView = view.findViewById(R.id.scrollView);
        btSend = view.findViewById(R.id.bt_send);
        etContent = view.findViewById(R.id.et_content);
        lyEdit = view.findViewById(R.id.ly_edit);
        lyEdit.setOnClickListener(this);
        tvPlay.setOnClickListener(this);
//        btSend.setOnClickListener(this);
        recyclerView.setOnItemClickListener(this);
        etContent.setFocusable(false);

        tvPlay.setText("播放量 " + playback);
        tvComment.setText("评论 " + awc_number);
        tvTime.setText(DateTool.getLatelyTime(create_time));
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0){
                     RoundViewDelegate delegate = btSend.getDelegate();
                    delegate.setBackgroundColor(R.color.violet_773FE3);
//                    delegate.setBackgroundPressColor(R.color.violet_562EA6);
                    btSend.setEnabled(true);
                }else {
                    RoundViewDelegate delegate = btSend.getDelegate();
                    delegate.setBackgroundColor(R.color.placeholder);
//                    delegate.setBackgroundPressColor(R.color.placeholder);
                    btSend.setEnabled(false);
                }
            }
        });

        try {
            Field mBehaviorField = dialog.getClass().getDeclaredField("mBehavior");
            mBehaviorField.setAccessible(true);
            final BottomSheetBehavior behavior = (BottomSheetBehavior) mBehaviorField.get(dialog);
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING:
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            break;
                        case BottomSheetBehavior.STATE_HIDDEN:
                            close(false);
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        setRecyclerView();
        JSONObject userObj = User.getInstance().getUserObj();
        userExtend = userObj.optJSONObject("userExtend");
        EventBusActivityScope.getDefault(getActivity()).register(this);
    }

    private String byReplyUserId;
    @Subscribe
    public void onThreeMainThread(BundleDataInEvent event){
        if (event.getType() == BundleDataInEvent.onTwoStageCommentEvent){
            HiddenKeyboard();
            Bundle bundle = (Bundle) event.getObject();
            TwoPosition = bundle.getInt("position");
            etContent.setText("");
            etContent.setHint(bundle.getString("nick"));
            byReplyUserId = bundle.getString("byReplyUserId");
            videoCommentId = bundle.getString("videoCommentId");
            initDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroy");
        EventBusActivityScope.getDefault(getActivity()).unregister(this);
    }

    private void setRecyclerView() {
        if (adapter == null){
            adapter = new CommentBottomAdapter(this, listBean);
        }
        recyclerView.setAdapter(adapter);
        ProgressLayout headerView = new ProgressLayout(getContext());
        refreshLayout.setHeaderView(headerView);
        refreshLayout.setEnableOverScroll(false);
        refreshLayout.startRefresh();
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                presenter.setData(pagerNumber = 1, id);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                presenter.setData(pagerNumber += 1, id);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_play:
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.bt_send:
                switch (commentState){
                    case 0:
                        presenter.setComment(etContent.getText().toString(), id);
                        break;
                    case 1:
                        presenter.setTwoStageComment(etContent.getText().toString(), videoCommentId, id);
                        break;
                    case 2:
                        presenter.setThreeComment(etContent.getText().toString(), videoCommentId, id, byReplyUserId);
                        break;
                }
                break;
            case R.id.ly_edit:
                initDialog();
                break;
        }
    }

    private void initDialog(){
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());
//                mBottomSheetDialog.getDelegate().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        View v = getLayoutInflater().inflate(R.layout.bottom_comment, null);
        final TextEditTextView editText = v.findViewById(R.id.et_content);
//        editText.setFocusable(true);
//        editText.setFocusableInTouchMode(true);
//        editText.requestFocus();
        v.findViewById(R.id.bt_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (commentState){
                    case 0:
                        presenter.setComment(editText.getText().toString(), id);
                        break;
                    case 1:
                        presenter.setTwoStageComment(editText.getText().toString(), videoCommentId, id);
                        break;
                    case 2:
                        presenter.setThreeComment(editText.getText().toString(), videoCommentId, id, byReplyUserId);
                        break;
                }
                mBottomSheetDialog.dismiss();
            }
        });
        editText.setHint(etContent.getHint());
        /*editText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imm.showSoftInput(editText, 0);
                    etContent.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
            }
        });*/
        new Timer().schedule(new TimerTask() {
            public void run() {
                imm.showSoftInput(editText, 0);
            }
        }, 200);
        mBottomSheetDialog.setContentView(v);
        mBottomSheetDialog.show();
        editText.setOnKeyBoardHideListener(new TextEditTextView.OnKeyBoardHideListener(){
            @Override
            public void onKeyHide(int keyCode, KeyEvent event) {
                mBottomSheetDialog.dismiss();
            }
        });
    }

    @Override
    public void hideLoading() {
        refreshLayout.setEnableRefresh(false);
        super.setRefreshLayout(pagerNumber, refreshLayout);
        super.hideLoading();
    }

    @Override
    public void onError(Throwable e) {
        super.setRefreshLayout(pagerNumber, refreshLayout);
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, refreshLayout);
    }

    @Override
    public void setData(Object data) {
        if (pagerNumber == 1) {
            listBean.clear();
            refreshLayout.finishRefreshing();
            refreshLayout.setEnableRefresh(false);
        } else {
            refreshLayout.finishLoadmore();
        }
        listBean.addAll((List<DataBean>) data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void AddComment(Object object) {
        commentState  = 0;
        HiddenKeyboard();
        etContent.setText("");
        etContent.setHint("");
        DataBean bean = (DataBean) object;
        DataBean.UserBean userBean = new DataBean.UserBean();
        userBean.setHead(userExtend.optString("head"));
        userBean.setUser_id(User.getInstance().getUserId());
        userBean.setNick(userExtend.optString("nick"));
//        bean.setEmojiContent(bean.getEmoji_content());
        bean.setUser(userBean);
        listBean.add(0, bean);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void AddTwoComment(Object object) {
        commentState  = 0;
        HiddenKeyboard();
        etContent.setText("");
        etContent.setHint("");
        DataBean d = (DataBean) object;
        DataBean.SvVideoCommentBean commentBean = new DataBean.SvVideoCommentBean();
        commentBean.setContent(d.getContent());
        commentBean.setEmoji_content(BaseUtils.encode(d.getEmoji_content()));
        commentBean.setHead(d.getHead());
        commentBean.setUser_id(userExtend.optString("userId"));
        commentBean.setSeries(d.getSeries());
        commentBean.setNick(d.getNick());
        commentBean.setReply_nick(d.getReply_nick());
        commentBean.setReply_user_id(d.getReply_user_id());

        if (commentState == 1){
            DataBean bean = listBean.get(twoPosition);
            List<DataBean.SvVideoCommentBean> svVideoComment = bean.getSvVideoComment();
            svVideoComment.add(0, commentBean);
        }else {
            DataBean bean = listBean.get(TwoPosition);
            List<DataBean.SvVideoCommentBean> svVideoComment = bean.getSvVideoComment();
            svVideoComment.add(0, commentBean);
        }
        adapter.notifyDataSetChanged();
    }

    private int toggleSoftInput = -1;
    //强制或者显示隐藏键盘
    private void HiddenKeyboard(){

//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);

//        if (imm != null) {
//            etContent.requestFocus();
//            imm.showSoftInput(etContent, 0);
//        }
//        LogUtils.e(imm.isActive());
//        toggleSoftInput = toggleSoftInput == 0 ? -1 : 0;
//        imm.toggleSoftInput(toggleSoftInput, InputMethodManager.HIDE_NOT_ALWAYS);

//        if (imm.isActive()){
//            etContent.requestFocus();
//            imm.showSoftInput(etContent, 0);
//        }else {
//            imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);
//
//        }
//        LogUtils.e(imm.isActive());
        /*etContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                LogUtils.e(imm.isActive());

            }
        });*/
    }

    private int twoPosition;
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DataBean bean = listBean.get(i);
        DataBean.UserBean user = bean.getUser();
        if (user.getUser_id().equals(User.getInstance().getUserId()))return;//因为后台没返回对应ID
        HiddenKeyboard();
        this.videoCommentId = bean.getId();
        etContent.setHint("回复 @" + user.getNick() + " :");
        commentState = 1;
        this.twoPosition = i;
        initDialog();
    }

}
