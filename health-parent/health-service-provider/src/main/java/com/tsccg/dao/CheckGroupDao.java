package com.tsccg.dao;

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
    void setConnection(Map map);
}
