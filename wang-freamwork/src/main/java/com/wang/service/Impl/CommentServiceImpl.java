package com.wang.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.constants.SystemConstants;
import com.wang.domain.ResponseResult;
import com.wang.domain.entity.Comment;
import com.wang.domain.vo.CommentVo;
import com.wang.domain.vo.PageVo;
import com.wang.enums.AppHttpCodeEnum;
import com.wang.exception.SystemException;
import com.wang.mapper.CommentMapper;

import com.wang.service.CommentService;

import com.wang.service.UserService;
import com.wang.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(WangComment)表服务实现类
 *
 * @author makejava
 * @since 2023-06-22 16:23:23
 */
@Service("CommentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserService userService;
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        //根评论 rootId为-1
        queryWrapper.eq(Comment::getRootId,-1);

        //评论类型
        queryWrapper.eq(Comment::getType,commentType);

        //分页查询
        Page<Comment> page = new Page(pageNum,pageSize);
        page(page,queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }

        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    @Override
    //添加评论
    public ResponseResult addComment(Comment comment) {
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);

        }
        save(comment);

        return ResponseResult.okResult();
    }

    //查询根评论下的子评论
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getRootId,id);
        commentLambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> comments = list(commentLambdaQueryWrapper);
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }

    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历集合
        for (CommentVo commentVo : commentVos) {

            //通过crateBy查询用户的昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //通过toCommentUserID查询用户的昵称并赋值
            //如果toCommentUserID不为-1才进行查询
            if(commentVo.getToCommentUserId()!=-1){
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);

            }
        }


        return commentVos;
    }
}