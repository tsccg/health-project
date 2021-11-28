package com.tsccg.service;

import com.tsccg.pojo.User;

/**
 * @Author: TSCCG
 * @Date: 2021/11/28 21:15
 */
public interface UserService {
    /**
     * 根据用户名查询用户基本信息
     * @param username 输入的用户名
     * @return 用户信息：1.密码 2.角色 3.权限
     */
    User findByName(String username);
}
