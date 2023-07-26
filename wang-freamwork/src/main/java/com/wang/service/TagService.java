package com.wang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.domain.ResponseResult;
import com.wang.domain.dto.TagListDto;
import com.wang.domain.entity.Tag;
import com.wang.domain.vo.PageVo;
import com.wang.domain.vo.TagVo;

import java.util.List;

/**
 * 标签(WangTag)表服务接口
 *
 * @author makejava
 * @since 2023-06-23 21:04:21
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    List<TagVo> listAllTag();
}