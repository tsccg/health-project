package com.tsccg.dao;

import com.github.pagehelper.Page;
import com.tsccg.pojo.CheckGroup;

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
     * 通过id删除检查项id对应的关联数据
     * @param id 待删除的检查项记录id
     */
    void deleteConnectionById(Integer id);
     /**
     * 删除检查项以及对应关联表的关联数据
     * @param id 待删除检查项id
     */
    void deleteCheckGroupById(Integer id);

}
