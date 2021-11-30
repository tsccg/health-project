package com.tsccg.service;

import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.pojo.Setmeal;

import java.util.List;
import java.util.Map;

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

    /**
     * 获取所有套餐信息
     * @return 保存有所有套餐信息的List集合
     */
    List<Setmeal> getAllSetmeal();

    /**
     * 根据套餐id查询套餐详细信息：
     *      1.套餐基本信息
     *      2.套餐对应的所有检查组信息
     *      3.每个检查组所对应的检查项信息
     * @param id 套餐id
     * @return 套餐详细信息
     */
    Setmeal findDetailedMessageById(Integer id);

    /**
     * 查询所有套餐预约数据
     * @return [{name:"套餐1",value:100},{name:"套餐2",value:200}...]
     */
    List<Map<String, Object>> findSetmealCount();
}
