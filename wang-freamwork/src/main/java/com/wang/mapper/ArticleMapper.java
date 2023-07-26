package com.wang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wang.domain.entity.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {
}
