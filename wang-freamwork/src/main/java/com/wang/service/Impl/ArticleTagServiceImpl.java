package com.wang.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.domain.entity.ArticleTag;
import com.wang.mapper.ArticleTagMapper;
import com.wang.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(WangArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-06-24 17:42:31
 */
@Service("ArticleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}