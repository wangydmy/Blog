package com.wang.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.domain.ResponseResult;
import com.wang.domain.entity.UserRole;
import com.wang.domain.vo.PageVo;
import com.wang.domain.vo.UserInfoVo;
import com.wang.domain.vo.UserVo;
import com.wang.enums.AppHttpCodeEnum;
import com.wang.exception.SystemException;
import com.wang.mapper.UserMapper;
import com.wang.domain.entity.User;
import com.wang.service.UserRoleService;
import com.wang.service.UserService;
import com.wang.utils.BeanCopyUtils;
import com.wang.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-06-22 17:05:12
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;
    @Override
    public ResponseResult userInfo() {
        // 获取用户当前id
        Long userId = SecurityUtils.getUserId();
        // 根据用户id查询用户信息
        User user = getById(userId);
        // 封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        // 对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }


        // 对数据进行是否存在非空判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }

        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }


        if (emailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        // 对密码进行加密

        String encodePassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodePassword);
        // 存入数据库

        save(user);
        return ResponseResult.okResult();
    }


    // 查询用户列表
    @Override
    public ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(user.getUserName()), User::getUserName, user.getUserName());
        queryWrapper.eq(StringUtils.hasText(user.getStatus()), User::getStatus, user.getStatus());
        queryWrapper.like(StringUtils.hasText(user.getPhonenumber()), User::getPhonenumber, user.getPhonenumber());

        Page<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        page(page, queryWrapper);

        List<User> userList = page.getRecords();
        List<UserVo> userVos = userList.stream()
                .map(u -> BeanCopyUtils.copyBean(u, UserVo.class))
                .collect(Collectors.toList());

        PageVo pageVo = BeanCopyUtils.copyBean(userVos, PageVo.class);
        pageVo.setRows(userVos);
        pageVo.setTotal(page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName,userName))==0;

    }

    @Override
    public boolean checkPhoneUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber,user.getPhonenumber()))==0;

    }

    @Override
    public boolean checkEmailUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail,user.getEmail()))==0;
    }

    @Override
    @Transactional
    public ResponseResult addUser(User user) {
        //密码加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);

        if(user.getRoleIds()!=null&&user.getRoleIds().length>0){
            insertUserRole(user);
        }
        return ResponseResult.okResult();
    }


    /*
    更新用户信息
     */
    @Override
    public void updateUser(User user) {

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(queryWrapper);

        // 新增用户与角色管理
        insertUserRole(user);
        // 更新用户信息
        updateById(user);
    }

    private void insertUserRole(User user) {
        List<UserRole> sysUserRoles = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(sysUserRoles);
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getEmail, email);
        return count(userLambdaQueryWrapper) > 0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getNickName, nickName);
        return count(userLambdaQueryWrapper) > 0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName, userName);
        return count(userLambdaQueryWrapper) > 0;
    }
}