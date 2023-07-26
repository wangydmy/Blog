package com.wang.service;

import com.wang.domain.ResponseResult;
import com.wang.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}

