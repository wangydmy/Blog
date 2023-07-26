package com.wang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.domain.ResponseResult;
import com.wang.domain.entity.Comment;


/**
 * 评论表(WangComment)表服务接口
 *
 * @author makejava
 * @since 2023-06-22 16:23:23
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
