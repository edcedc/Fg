package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgcm.model.CommentBottomModel;
import com.fanwang.fgcm.model.CommentBottomModellmpl;
import com.fanwang.fgcm.presenter.listener.OnCommentBottomListListener;
import com.fanwang.fgcm.view.impl.CommentBottomView;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/5/17.
 */

public class CommentBottomPresenterlmpl implements CommentBottomPresenter, OnCommentBottomListListener {

    private CommentBottomView view;
    private CommentBottomModel model;

    public CommentBottomPresenterlmpl(CommentBottomView view){
        this.view = view;
        this.model = new CommentBottomModellmpl();
    }

    @Override
    public void setData(int pageNumber, String id) {
        model.ajaxList(pageNumber, id, this);
    }

    @Override
    public void setComment(String s, String id) {
        if (StringUtils.isEmpty(s) || StringUtils.isEmpty(id)){
            return;
        }
        model.ajaxComment(s, id, this);
    }

    @Override
    public void setTwoStageComment(String content, String videoCommentId, String videoId) {
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(videoCommentId)){
            return;
        }
        model.ajaxPageAWCLevel(content, videoCommentId, videoId,null, this);
    }

    @Override
    public void setThreeComment(String content, String videoCommentId, String videoId, String byReplyUserId) {
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(byReplyUserId)){
            return;
        }
        model.ajaxPageAWCLevel(content, videoCommentId, videoId, byReplyUserId, this);
    }

    @Override
    public void showLoading() {
        view.showLoading();
    }

    @Override
    public void hideLoading() {
        view.hideLoading();
    }

    @Override
    public void onError(Throwable e) {
        view.hideLoading();
//        ToastUtils.showShort(e.getMessage().toString());
    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onSuccess(Object data) {
        view.setData(data);
    }

    @Override
    public void onRefreshLayoutMode(int totalRow) {
        view.setRefreshLayoutMode(totalRow);
    }

    @Override
    public void onCommentSuccess(Object object) {
        view.AddComment(object);
    }

    @Override
    public void onTwoCommentSuccess(Object object) {
        view.AddTwoComment(object);
    }
}
