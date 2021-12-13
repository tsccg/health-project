package com.tsccg.service;

import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.pojo.User;

import java.util.List;

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

    void add(User user, Integer[] roleIds);

    PageResult findPage(QueryPageBean queryPageBean);

    void deleteById(Integer id);

    User findById(Integer id);

    List<Integer> findRoleIds(Integer id);

    /**
     * 编辑用户信息
     * @param user
     * @param roleIds
     */
    void edit(User user, Integer[] roleIds);
}
