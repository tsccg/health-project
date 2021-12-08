package com.tsccg.service;

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
}
