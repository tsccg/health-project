package com.tsccg.service;

import com.tsccg.pojo.OrderSetting;

import java.util.List;

/**
 * @Author: TSCCG
 * @Date: 2021/11/20 16:35
 */
public interface OrderSettingService {
    /**
     * 将预约数据批量导入数据库
     * @param orderSettingList 预约数据集合
     */
    void add(List<OrderSetting> orderSettingList);
}
