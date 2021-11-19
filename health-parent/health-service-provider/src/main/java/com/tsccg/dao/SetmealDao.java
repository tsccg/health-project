package com.tsccg.dao;

import com.github.pagehelper.Page;
import com.tsccg.pojo.Setmeal;

import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/11/10 16:49
 */
public interface SetmealDao {
    /**
     * 添加套餐
     * @param setmeal 套餐信息
     */
    void add(Setmeal setmeal);

    /**
     * 根据套餐id添加关联信息
     * @param map 关联信息
     */
    void setConnection(Map<String,Integer> map);

    /**
     * 分页查询
     * @param queryString 查询条件
     * @return 当前页数据
     */
    Page<Setmeal> selectByCondition(String queryString);
}
