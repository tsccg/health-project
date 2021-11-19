package com.tsccg.dao;

import com.github.pagehelper.Page;
import com.tsccg.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/11/08 17:54
 */
public interface CheckGroupDao {
    /**
     * 添加检查项
     * @param checkGroup 检查组基本数据
     */
    void add(CheckGroup checkGroup);

    /**
     * 设置检查组和检查项的关联关系,往检查组和检查项的关联表中添加数据
     * @param map 关联表中单条检查组id和检查项id
     */
    void setConnection(Map<String,Integer> map);

    /**
     * 分页查询
     * @param queryString 分页查询条件
     * @return 返回分页对象
     */
    Page<CheckGroup> selectByCondition(String queryString);
    /**
     * 通过id删除检查组id对应的关联数据
     * @param id 待删除的检查组记录id
     */
    void deleteConnectionById(Integer id);
     /**
     * 删除检查组以及对应关联表的关联数据
     * @param id 待删除检查组id
     */
    void deleteCheckGroupById(Integer id);
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
     * 更新检查组信息
     * @param checkGroup 编辑后的检查组信息
     */
    void edit(CheckGroup checkGroup);
    /**
     * 查询所有检查组
     * @return 返回装有所有检查组的List集合
     */
    List<CheckGroup> findAll();
}
