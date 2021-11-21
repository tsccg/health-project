package com.tsccg.service;

import com.tsccg.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

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

    /**
     * 据月份查询对应的预约设置数据
     * @param date 查询条件：2021-11 yyyy-MM
     * @return 存有预约设置信息的List集合
     */
    List<Map<String, Integer>> getOrderSettingByMonth(String date);

    /**
     * 根据日期编辑对应的可预约人数
     * @param orderSetting 日期；可预约人数
     */
    void editNumberByDate(OrderSetting orderSetting);
}
