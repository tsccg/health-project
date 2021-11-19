package com.tsccg.service;

import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.pojo.CheckGroup;

import java.util.List;

/**
 * @Author: TSCCG
 * @Date: 2021/11/08 17:44
 * 检查组服务接口
 */
public interface CheckGroupService {
    /**
     * 添加检查项
     * @param checkItemIds 请求组中所有检查项的id
     * @param checkGroup 检查组基本数据
     */
    void add(Integer[] checkItemIds,CheckGroup checkGroup);

    /**
     * 分页查询
     * @param queryPageBean 分页查询条件： 1.当前页码 2.每页展示数据条数 3.查询条件
     * @return 返回分页结果：1.总记录条数 2.当前页的所有记录
     */
    PageResult pageQuery(QueryPageBean queryPageBean);

    /**
     * 删除检查组
     * @param id 检查组id
     */
    void deleteById(Integer id);
    /**
     * 根据id查询检查组数据
     * @param id 检查组id
     * @return 返回检查组数据
     */
    CheckGroup findById(Integer id);
     /**
     * 根据检查组id查询其对应的所有检查项id
     * @param id 检查组id
     * @return 查询结果：存有所有检查项id的List集合
     */
    List<Integer> findCheckItemIdsById(Integer id);

    /**
     * 编辑检查组
     * @param checkGroup 编辑后的检查组信息
     * @param checkItemIds 检查组对应检查项id
     */
    void edit(CheckGroup checkGroup, Integer[] checkItemIds);
    /**
     * 查询所有检查组
     * @return 返回装有所有检查组的List集合
     */
    List<CheckGroup> findAll();
}
