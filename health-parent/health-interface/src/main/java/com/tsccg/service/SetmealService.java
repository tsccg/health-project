package com.tsccg.service;

import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.pojo.Setmeal;

import java.util.List;

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

    /**
     * 删除套餐
     * @param id 套餐id
     */
    void deleteById(Integer id,String img);
    /**
     * 根据id查询套餐信息
     * @param id 套餐id
     * @return 套餐信息
     */
    Setmeal findById(Integer id);

    /**
     * 根据套餐id查询对应检查组
     * @param id 套餐id
     * @return 存有所有检查组id的List集合
     */
    List<Integer> findCheckGroupIds(Integer id);

    /**
     * 更新套餐，同时需要更新和检查组的关联关系
     * @param setmeal 套餐信息
     * @param checkGroupIds 套餐对应检查组id
     */
    void update(Setmeal setmeal, Integer[] checkGroupIds,String oldImg);
}