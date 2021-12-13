package com.tsccg.dao;

import com.github.pagehelper.Page;
import com.tsccg.pojo.Role;
import com.tsccg.pojo.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /**
    根据用户名查询用户数量
     */
    Integer findUserCountByUsername(String username);

    /**
     * 添加用户基本信息
     * @param user
     */
    void add(User user);

    /**
     * 为用户关联角色
     */
    void addRole(Map<String,Integer> map);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<User> findPage(String queryString);

    /**
     * 更新用户信息
     * @param user
     */
    void edit(User user);

    /**
     * 根据用户id清除用户关联的角色
     * @param id
     */
    void deleteRoleByUserId(Integer id);

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    User findById(Integer id);

    /**
     * 根据用户id查询关联的所有角色id
     * @param id
     * @return
     */
    List<Integer> findRoleIds(Integer id);

    /**
     * 删除用户
     * @param id
     */
    void deleteById(Integer id);
}
