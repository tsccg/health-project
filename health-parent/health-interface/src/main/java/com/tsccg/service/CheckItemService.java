package com.tsccg.service;

import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.pojo.CheckItem;

/**
 * @Author: TSCCG
 * @Date: 2021/11/05 22:46
 */
public interface CheckItemService {
    void add(CheckItem checkItem);

    PageResult pageQuery(QueryPageBean queryPageBean);
}
