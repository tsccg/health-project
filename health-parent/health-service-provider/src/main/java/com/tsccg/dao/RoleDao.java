package com.tsccg.dao;

import com.tsccg.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: TSCCG
 * @Date: 2021/11/28 21:56
 */
public interface RoleDao {
    /**
     * 根据用户Id查询关联的角色
     * @param userId 用户Id
     * @return 关联的角色，封装到Set集合中
     */
    Set<Role> findByUserId(Integer userId);

    /**
     * 为角色关联菜单
     * @param map role_id:角色id;menu_id:菜单id
     */
    void addMenu(Map<String,Integer> map);

    /**
     * 为角色关联权限
     * @param map role_id:角色id;permission_id:权限id
     */
    void addPermission(Map<String, Integer> map);

}
