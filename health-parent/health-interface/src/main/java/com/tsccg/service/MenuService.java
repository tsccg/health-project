package com.tsccg.service;

import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Menu;

import java.util.List;
import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/12/08 18:55
 */
public interface MenuService {

    /**
     * 获取所有顶级菜单
     * @return
     */
    List<Menu> findAllParentMenu();

    /**
     * 添加菜单
     * @param menu
     */
    Result add(Menu menu);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 根据id删除菜单
     * @param id
     */
    void deleteById(Integer id);
    /**
     * 根据菜单id查询菜单数据
     * @param id
     * @return
     */
    Menu findById(Integer id);

    /**
     * 编辑菜单
     * @param menu
     * @return
     */
    Result edit(Menu menu);
}
