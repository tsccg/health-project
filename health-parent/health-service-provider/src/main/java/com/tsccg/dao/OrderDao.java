package com.tsccg.dao;

import com.github.pagehelper.Page;
import com.tsccg.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    public void add(Order order);
    public List<Order> findByCondition(Order order);
    public Map findById4Detail(Integer id);
    public Integer findOrderCountByDate(String date);
    public Integer findOrderCountAfterDate(String date);
    public Integer findVisitsCountByDate(String date);
    public Integer findVisitsCountAfterDate(String date);
    public List<Map<String,Object>> findHotSetmeal();
    /**
     * 根据条件查询预约信息
     * @param queryString 档案号/姓名/手机号
     * @return
     */
    Page<Map<String,Object>> findOrdersPage(String queryString);

    /**
     * 修改预约状态
     * @param order 1.预约id 2.要修改的预约状态：未到诊 ；已到诊
     */
    void editOrderStatus(Order order);

    /**
     * 取消预约：根据id删除预约信息
     * @param id 预约记录id
     */
    void deleteOrderById(Integer id);

    /**
     * 取消预约：根据会员id删除预约信息
     * @param memberId 会员id
     */
    void deleteOrderByMemberId(Integer memberId);
    /**
     * 根据id查询预约基本信息
     * @param id 预约记录id
     * @return 预约基本信息
     * 1.name 2.telephone 3.orderDate 4.birthday 5.sex 6.age 7.idCard
     */
    Map<String, Object> findById(Integer id);
    /**
     * 根据预约id查询所对应的套餐id
     * @param id 预约id
     * @return 套餐id
     */
    Integer findSetmealId(Integer id);

    /**
     * 修改预约表数据
     * @param order 待修改数据
     */
    void editOrder(Order order);
}
