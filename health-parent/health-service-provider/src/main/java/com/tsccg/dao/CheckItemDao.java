package com.tsccg.dao;

import com.github.pagehelper.Page;
import com.tsccg.pojo.CheckItem;

/**
 * @Author: TSCCG
 * @Date: 2021/11/05 23:10
 * 持久层dao接口
 */
public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> selectByCondition(String queryString);
}
