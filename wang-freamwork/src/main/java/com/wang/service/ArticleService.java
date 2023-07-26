package com.wang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.domain.ResponseResult;
import com.wang.domain.dto.AddArticleDto;
import com.wang.domain.dto.ArticleDto;
import com.wang.domain.entity.Article;
import com.wang.domain.vo.ArticleVo;
import com.wang.domain.vo.PageVo;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize);

    ArticleVo getInfo(Long id);

    void edit(ArticleDto article);
}
