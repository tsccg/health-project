package com.tsccg.service;

import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Permission;

/**
 * @Author: TSCCG
 * @Date: 2021/12/10 15:49
 */
public interface PermissionService {
    /**
     * 添加权限
     * @param permission
     */
    Result add(Permission permission);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 根据id删除权限项
     * @param id
     */
    Result deleteById(Integer id);

    /**
     * 根据id查询权限数据
     * @param id
     */
    Permission findById(Integer id);

    /**
     * 更新权限信息
     * @param permission
     */
    void updateById(Permission permission);
}
