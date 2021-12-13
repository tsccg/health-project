package com.tsccg.dao;

import com.github.pagehelper.Page;
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
    Set<Role> findRolesByUserId(Integer userId);

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

    /**
     * 根据关键字查询角色
     * @param keyword
     * @return
     */
    Role findByKeyword(String keyword);

    /**
     * 添加角色
     */
    void add(Role role);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<Role> findByCondition(String queryString);

    /**
     * 根据角色id查询关联数据
     * @param roleId 角色id
     * @return
     */
    Integer findConnection(Integer roleId);

    /**
     * 根据id删除角色
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 清除菜单关联
     * @param id
     */
    void deleteMenus(Integer id);

    /**
     * 清除权限关联
     * @param id
     */
    void deletePermissions(Integer id);

    /**
     * 根据id查询角色信息
     * @param id
     * @return
     */
    Role findById(Integer id);
    /**
     * 根据id查询所关联的权限id列表
     */
    List<Integer> findPermissionIdsById(Integer id);
    /**
     * 根据id查询所关联的菜单id列表
     */
    List<Integer> findMenuIdsById(Integer id);

    /**
     * 编辑角色信息
     * @param role
     */
    void edit(Role role);

    /**
     * 根据关键字查询角色记录数量
     * @param keyword
     * @return
     */
    Integer findRoleCountByKeyword(String keyword);

}
