package com.wang.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.constants.SystemConstants;
import com.wang.mapper.MenuMapper;
import com.wang.domain.entity.Menu;
import com.wang.service.MenuService;
import com.wang.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-06-24 09:01:56
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        // 如果是管理员，返回对应权限

        if (id == 1) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(queryWrapper);
            List<String> perms = menus.stream().map(Menu::getPerms).collect(Collectors.toList());

            return perms;
        }

        // 如果是其他角色 返回其对应权限

        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        // 判断是否是管理员

        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menuList = null;
        if (SecurityUtils.isAdmin()) {
            // 如果是管理员，返回对符合要求的Menu

            menuList = menuMapper.selectAllRouterMenu();

        } else {
            // 否则 查询当前用户所具有的Menu
            menuList = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        // 构建tree
        List<Menu> menuTree = buildMenuTree(menuList, 0L);


        return menuTree;
    }


    @Override
    public boolean hasChild(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        return count(queryWrapper) != 0;
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return getBaseMapper().selectMenuListByRoleId(roleId);
    }

    // 查询所有菜单
    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()), Menu::getMenuName, menu.getMenuName());
        queryWrapper.like(StringUtils.hasText(menu.getStatus()), Menu::getStatus, menu.getStatus());
        // 排序
        queryWrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> list = list(queryWrapper);
        return list;
    }

    private List<Menu> buildMenuTree(List<Menu> menuList, Long parentId) {

        List<Menu> menuTree = menuList.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menuList)))
                .collect(Collectors.toList());

        return menuTree;
    }

    // 获取传入参数的子menu集合
    private List<Menu> getChildren(Menu menu, List<Menu> menuList) {

        List<Menu> childrenList = menuList.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menuList)))
                .collect(Collectors.toList());
        return childrenList;
    }
}