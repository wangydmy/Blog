package com.wang.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.mapper.UserRoleMapper;
import com.wang.domain.entity.UserRole;
import com.wang.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-06-25 14:47:00
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}