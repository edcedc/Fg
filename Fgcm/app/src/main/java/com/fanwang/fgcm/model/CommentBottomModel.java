package com.fanwang.fgcm.model;

 import com.fanwang.fgcm.presenter.listener.OnCommentBottomListListener;

/**
 * Created by Administrator on 2018/5/17.
 */

public interface CommentBottomModel {

    void ajaxList(int pageNumber, String id, OnCommentBottomListListener listener);

    void ajaxComment(String s, String id, OnCommentBottomListListener listener);

    void ajaxPageAWCLevel(String content, String replyUserId, String videoId,String byReplyUserId,  OnCommentBottomListListener listener);
}
