package com.fanwang.fgcm.presenter;

/**
 * Created by Administrator on 2018/5/17.
 */

public interface CommentBottomPresenter {

    void setData(int pageNumber, String id);

    void setComment(String s, String id);

    void setTwoStageComment(String content, String videoCommentId, String videoId);

    void setThreeComment(String content, String videoCommentId, String id, String byReplyUserId);

}
