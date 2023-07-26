package com.wang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.domain.entity.RoleMenu;

/**
 * 角色和菜单关联表(RoleMenu)表服务接口
 *
 * @author makejava
 * @since 2023-06-25 13:54:21
 */
public interface RoleMenuService extends IService<RoleMenu> {

    void deleteRoleMenuByRoleId(Long id);
}