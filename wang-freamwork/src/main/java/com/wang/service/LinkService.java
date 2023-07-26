package com.wang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.domain.ResponseResult;
import com.wang.domain.entity.Link;
import com.wang.domain.vo.PageVo;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-06-22 08:07:02
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize);
}
