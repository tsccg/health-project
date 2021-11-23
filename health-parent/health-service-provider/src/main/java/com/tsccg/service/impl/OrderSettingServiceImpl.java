package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tsccg.dao.OrderSettingDao;
import com.tsccg.pojo.OrderSetting;
import com.tsccg.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: TSCCG
 * @Date: 2021/11/20 16:57
 * 预约设置服务
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 将预约数据批量导入数据库
     * @param orderSettingList 预约数据集合
     */
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if(orderSettingList != null && orderSettingList.size() > 0) {
            //遍历集合
            for (OrderSetting orderSetting : orderSettingList) {
                //将当前数据导入数据库
                this.editNumberByDate(orderSetting);
            }
        }
    }

    /**
     * 查询数据库中是否已存在当前日期
     * @param date 当前日期
     * @return 查询记录数
     */
    private long findCountByOrderDate(Date date) {
        return orderSettingDao.findCountByOrderDate(date);
    }
    /**
     * 据月份批量查询对应的预约设置数据
     * @param date 查询条件：2021-11 yyyy-MM
     * @return 存有预约设置信息的List集合
     */
    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String date) {
        //创建查询map
        Map<String,String> map = new HashMap<>();
        //2021-11-1
        map.put("start",date + "-1");
        //2021-11-31 (无论当前月是否是31天，都设置为31)
        map.put("end",date + "-31");
        //获取当月所有的预约数据，封装到OrderSetting列表里
        List<OrderSetting> list = orderSettingDao.findOrderSettingByMonth(map);
        /*
        将查询到的数据转换为页面需要的数据格式
            页面需要数据格式：
            [
                { date: 1, number: 120, reservations: 100 },
                { date: 3, number: 120, reservations: 1 },
            ]*/
        List<Map<String,Integer>> result = new ArrayList<>();
        if(list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                Map<String,Integer> m = new HashMap<>();
                //获取日期数字(几号)
                m.put("date",orderSetting.getOrderDate().getDate());
                m.put("number",orderSetting.getNumber());
                m.put("reservations",orderSetting.getReservations());
                result.add(m);
            }
        }
        return result;
    }

    /**
     * 根据日期编辑对应的可预约人数
     * @param orderSetting 日期；可预约人数
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //在进行更新前先查询数据库中当前日期是否已设置了
        long countByOrderDate =  this.findCountByOrderDate(orderSetting.getOrderDate());
        if (countByOrderDate > 0) {
            //若当前日期已设置，则根据日期更新预约数量
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            //若当前日期未设置，则直接插入数据
            orderSettingDao.add(orderSetting);
        }
    }
}
