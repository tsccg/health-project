package com.tsccg.dao;

import com.tsccg.pojo.User;

/**
 * @Author: TSCCG
 * @Date: 2021/11/28 21:52
 */
public interface UserDao {
    /**
     * 根据用户名查询用户基本信息
     * @param username 用户名
     * @return 用户基本信息
     */
    User findByName(String username);
}
