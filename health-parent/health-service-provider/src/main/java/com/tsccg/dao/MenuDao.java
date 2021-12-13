package com.tsccg.dao;

import com.github.pagehelper.Page;
import com.tsccg.pojo.Menu;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/12/08 19:04
 */
public interface MenuDao {
    /**
     * 根据角色id查询关联的顶级菜单
     * @param roleId
     * @return
     */
    LinkedHashSet<Menu> findParentMenuByRoleId(Integer roleId);

    /**
     * 根据顶级菜单Id查询对应的子菜单列表
     * @param parentId
     * @return
     */
    List<Menu> findChildrenMenuByParentId(Integer parentId);

    /**
     * 查询所有的顶级菜单
     * @return
     */
    List<Menu> findAllParentMenu();

    /**
     * 根据id查询对应的菜单信息
     * @param id
     * @return
     */
    Menu findById(Integer id);

    /**
     * 查询所有父菜单的数量
     * @return
     */
    Integer findParentMenuCount();

    /**
     * 根据上级菜单id查询已有的子菜单项数量
     * @return
     */
    Integer findChildCountByParentId(Integer parentMenuId);

    /**
     * 将参数信息添加到数据库
     * @param menu
     */
    void add(Menu menu);
    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<Menu> findByCondition(String queryString);

    /**
     * 清除关联表数据
     * @param id
     */
    void deleteConnectionById(Integer id);

    /**
     * 根据id删除菜单
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 根据父菜单id删除其所有子菜单
     * @param id 父菜单id
     */
    void deleteByParentMenuId(Integer id);

    /**
     * 编辑菜单
     * @param menu
     */
    void update(Menu menu);

    /**
     * 根据当前一级菜单id和角色id查询对应的子菜单列表
     * @param map parentMenuId   role_id
     * @return
     */
    List<Menu> findChildrenMenuByParentIdAndRoleId(Map<String, Integer> map);

    /**
     * 根据角色id查询关联的所有菜单项
     * @param roleId
     * @return
     */
    LinkedHashSet<Menu> findAllMenuByRoleId(Integer roleId);
}
