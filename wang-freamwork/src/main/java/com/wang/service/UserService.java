package com.wang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.domain.ResponseResult;
import com.wang.domain.entity.User;

import java.util.List;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-06-22 17:05:12
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize);

    boolean checkUserNameUnique(String userName);

    boolean checkPhoneUnique(User user);

    boolean checkEmailUnique(User user);

    ResponseResult addUser(User user);


    void updateUser(User user);
}