package com.wang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.domain.ResponseResult;
import com.wang.domain.entity.Category;
import com.wang.domain.vo.CategoryVo;
import com.wang.domain.vo.PageVo;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-06-21 15:49:12
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    PageVo selectCategoryPage(Category category, Long pageNum, Long pageSize);


}