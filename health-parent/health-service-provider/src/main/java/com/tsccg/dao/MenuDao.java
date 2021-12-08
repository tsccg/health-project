package com.tsccg.dao;

import com.tsccg.pojo.Menu;

import java.util.LinkedHashSet;
import java.util.List;

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
}
