package com.tsccg.service;

import com.tsccg.entity.Result;

import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/11/23 20:18
 * 用户短信预约服务
 */
public interface OrderService {
    /**
     * 体检预约
     * @param map 用户输入数据
     * @return 执行结果
     * @throws Exception
     */
    Result order(Map<String,String> map) throws Exception;
    /**
     * 根据预约记录id查询对应的预约信息
     * @param id 预约记录id
     * @return 预约信息
     */
    Map findById(Integer id) throws Exception;
}
