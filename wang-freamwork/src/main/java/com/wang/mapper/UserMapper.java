package com.wang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wang.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-22 08:30:51
 */
@Repository(value = "userMapper")
public interface UserMapper extends BaseMapper<User> {


}