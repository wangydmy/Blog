package com.wang.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.constants.SystemConstants;
import com.wang.domain.ResponseResult;
import com.wang.domain.entity.Article;
import com.wang.domain.vo.CategoryVo;
import com.wang.domain.vo.PageVo;
import com.wang.mapper.CategoryMapper;

import com.wang.domain.entity.Category;

import com.wang.service.ArticleService;
import com.wang.service.CategoryService;
import com.wang.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-06-21 15:49:13
 */
@Service("categoryService")
@Transactional
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    public ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        // 查询文章表，状态为已发布
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(queryWrapper);
        // 获取文章的分类id 去重
        Set<Long> categoryIds = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());
        // 查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream().filter(category ->
                        SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        // 封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }


    // 查询所有标签
    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> list = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return categoryVos;
    }


    // 分页查询分类列表
    @Override
    public PageVo selectCategoryPage(Category category, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(category.getName()), Category::getName, category.getName());
        queryWrapper.eq(StringUtils.hasText(category.getStatus()), Category::getStatus, category.getStatus());

        Page<Category> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNum);
        page(page, queryWrapper);

        List<Category> categories = page.getRecords();
        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(categories);
        return pageVo;
    }

    //新曾分类
}