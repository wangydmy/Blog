package com.wang.service.Impl;

import com.wang.domain.ResponseResult;
import com.wang.domain.entity.LoginUser;
import com.wang.domain.entity.User;
import com.wang.domain.vo.BlogUserLoginVo;
import com.wang.domain.vo.UserInfoVo;
import com.wang.service.BlogLoginService;
import com.wang.utils.BeanCopyUtils;
import com.wang.utils.JwtUtil;
import com.wang.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Objects;

@Service
@Transactional
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        // 把用户信息存入redis
        redisCache.setCacheObject("bloglogin:" + userId, loginUser);

        // 把token和userinfo封装 返回
        // 把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(vo);
    }

    // 退出系统
    @Override
    public ResponseResult logout() {
        // 获取token的 解析获取的userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
       // 获取的userid
        Long userId = loginUser.getUser().getId();
        // 删除redis的信息
        redisCache.deleteObject("bloglogin"+userId);
        return ResponseResult.okResult();
    }
}