package com.tsccg.dao;

import com.github.pagehelper.Page;
import com.tsccg.pojo.Setmeal;

import java.util.List;
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
    /**
     * 根据套餐id删除对应关联表数据
     * @param id 套餐id
     */
    void deleteConnection(Integer id);
    /**
     * 根据套餐id删除套餐信息
     * @param id 套餐id
     */
    void deleteById(Integer id);
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
     * 更新套餐信息
     * @param setmeal 套餐信息
     */
    void update(Setmeal setmeal);
}
