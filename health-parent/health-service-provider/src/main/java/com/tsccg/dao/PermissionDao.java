package com.tsccg.dao;

import com.tsccg.pojo.Permission;

import java.util.Set;

/**
 * @Author: TSCCG
 * @Date: 2021/11/28 22:07
 */
public interface PermissionDao {
    /**
     * 根据角色id查询关联的权限
     * @param roleId 角色id
     * @return 角色关联的权限
     */
    Set<Permission> findByRoleId(Integer roleId);
}
