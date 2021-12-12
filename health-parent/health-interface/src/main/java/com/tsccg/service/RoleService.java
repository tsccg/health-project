package com.tsccg.service;

import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Role;

import java.util.List;

/**
 * @Author: TSCCG
 * @Date: 2021/12/11 22:11
 * 角色服务
 */
public interface RoleService {


    /**
     * 添加角色
     * @param role
     * @param permissionIds
     * @param menuIds
     * @return
     */
    void add(Role role, Integer[] permissionIds, Integer[] menuIds);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 删除角色
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 根据id查询角色基本信息
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
     * 编辑角色
     * @param role
     * @param permissionIds
     * @param menuIds
     */
    void edit(Role role, Integer[] permissionIds, Integer[] menuIds);
}
