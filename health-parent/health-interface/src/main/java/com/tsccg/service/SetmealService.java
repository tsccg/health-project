package com.tsccg.service;

import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.pojo.Setmeal;

/**
 * @Author: TSCCG
 * @Date: 2021/11/10 15:50
 */
public interface SetmealService {
    /**
     * 添加套餐
     * @param setmeal 套餐信息
     * @param checkgroupIds 套餐对应检查组id数组
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 分页查询
     * @param queryPageBean 查询条件
     * @return 当前页数据
     */
    PageResult findPage(QueryPageBean queryPageBean);
}
