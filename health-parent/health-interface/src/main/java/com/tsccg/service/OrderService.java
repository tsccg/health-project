package com.tsccg.service;

import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;

import java.util.List;
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
    Map findById4Detail(Integer id) throws Exception;

    /**
     * 分页查询预约信息列表
     * @param queryPageBean 1.当前页码、2.每页记录数、3.查询条件
     * @return 1.total:总记录数。2.rows:当前页结果
     */
    PageResult findOrdersPage(QueryPageBean queryPageBean);

    /**
     * 修改预约状态
     * @param map 1.预约id 2.要修改的预约状态：未到诊 ；已到诊
     */
    void editOrderStatus(Map<String,String> map);

    /**
     * 取消预约：根据id删除预约信息
     * @param id 预约记录id
     */
    void deleteOrderById(Integer id);

    /**
     * 根据id查询预约基本信息
     * @param id 预约记录id
     * @return 预约基本信息
     * 1.name 2.telephone 3.orderDate 4.birthday 5.sex 6.age 7.idCard
     */
    Map<String, Object> findById(Integer id);
    /**
     * 查询当前预约所对应的套餐id
     * @param id 预约id
     * @return 套餐id
     */
    List<Integer> findSetmealIds(Integer id);

    /**
     * 修改预约数据
     * @param map 修改后的预约数据
     * @param setmealId 修改后的套餐id
     * @return
     */
    Result editOrder(Map<String, String> map,Integer setmealId) throws Exception;
}
