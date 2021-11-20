package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tsccg.dao.OrderSettingDao;
import com.tsccg.pojo.OrderSetting;
import com.tsccg.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                //在插入前先查询数据库中是否已存在当前日期
                long countByOrderDate = orderSettingDao.
                        findCountByOrderDate(orderSetting.getOrderDate());
                if (countByOrderDate > 0) {
                    //若已存在当前日期，则根据日期更新预约数量
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    //若不存在，则直接插入数据
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }
}
