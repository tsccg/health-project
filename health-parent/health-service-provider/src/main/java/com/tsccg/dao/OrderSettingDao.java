package com.tsccg.dao;

import com.tsccg.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/11/20 16:59
 */
public interface OrderSettingDao {
    /**
     * 根据日期查询记录条数
     * @param orderDate 日期
     * @return 记录数
     */
    long findCountByOrderDate(Date orderDate);

    /**
     * 根据日期更新预约数量
     * @param orderSetting 预约数据
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 插入预约数据
     * @param orderSetting 预约数据
     */
    void add(OrderSetting orderSetting);

    /**
     * 据月份查询对应的预约设置数据
     * @param map start：2021-11-1  end：2021-11-31
     * @return 该月的预约数据集合
     */
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);
}
