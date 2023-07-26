package com.wang.service;

import com.wang.domain.ResponseResult;
import com.wang.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);


    ResponseResult logout();
}

