package com.tsccg.dao;

import com.github.pagehelper.Page;
import com.tsccg.pojo.CheckItem;

import java.util.List;

/**
 * @Author: TSCCG
 * @Date: 2021/11/05 23:10
 * 持久层dao接口
 */
public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> selectByCondition(String queryString);

    long findCountCheckItemById(Integer id);

    void deleteById(Integer id);

    CheckItem findById(Integer id);

    void update(CheckItem checkItem);

    List<CheckItem> findAll();
}
