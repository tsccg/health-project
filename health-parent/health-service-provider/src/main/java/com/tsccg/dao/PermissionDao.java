package com.tsccg.dao;

import com.github.pagehelper.Page;
import com.tsccg.pojo.Permission;

import java.util.Map;
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

    /**
     * 添加权限
     * @param permission
     */
    void add(Permission permission);

    /**
     * 分页查询
     * @return
     */
    Page<Permission> findPage(String queryString);

    /**
     * 更新权限信息
     * @param permission
     */
    void updateById(Permission permission);

    /**
     * 根据id删除权限项
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 查询当前权限项的关联关系
     * @param id 权限id
     * @return 关联记录数量
     */
    long findRoleCountByPermissionId(Integer id);

    /**
     * 根据id查询权限项
     * @param id
     * @return
     */
    Permission findById(Integer id);

    /**
     * 根据keyword查询权限项
     * @param keyword
     * @return
     */
    Integer findKeyWordCount(String keyword);
}
