package com.tsccg.service;

import com.tsccg.pojo.CheckGroup;

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
}
