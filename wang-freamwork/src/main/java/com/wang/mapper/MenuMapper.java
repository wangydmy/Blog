package com.wang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wang.domain.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-24 09:01:56
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Long> selectMenuListByRoleId(Long roleId);
}