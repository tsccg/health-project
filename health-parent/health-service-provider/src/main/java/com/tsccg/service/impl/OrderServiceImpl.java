package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tsccg.constant.MessageConstant;
import com.tsccg.dao.MemberDao;
import com.tsccg.dao.OrderDao;
import com.tsccg.dao.OrderSettingDao;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Member;
import com.tsccg.pojo.Order;
import com.tsccg.pojo.OrderSetting;
import com.tsccg.service.OrderService;
import com.tsccg.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/11/23 20:35
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 体检预约
     * @param map 用户输入数据
     * @return 执行结果
     * @throws Exception
     */
    @Override
    public Result order(Map<String, String> map) throws Exception {
        //1.检查预约日期是否进行了预约设置
        String orderDate = map.get("orderDate");//获取用户预约日期的字符串
        Date date = DateUtils.parseString2Date(orderDate);//将字符串转换为日期类型
        OrderSetting orderSetting = orderSettingDao.findOrderSettingByDate(date);//根据用户预约日期查询预约设置
        if (orderSetting == null) {
            return new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2.检查预约日期是否预约已满
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //3.检查用户是否重复预约（同一个用户在同一天预约了同一个套餐）
        String telephone = map.get("telephone");
        //根据用户输入的电话号码查询会员用户
        Member member = memberDao.findByTelephone(telephone);
        //检查当前用户是否为会员
        if (member != null) {
            //若用户已经是会员了，检查用户是否重复预约
            //根据当前用户的会员id、预约日期、套餐id查询Order表
            //会员id
            Integer memberId = member.getId();
            //套餐id
            String setmealId = map.get("setmealId");
            //将三个查询条件封装到Order对象里
            Order order = new Order(memberId,date,Integer.parseInt(setmealId));
            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList != null && orderList.size() > 0) {
                //若查询出数据，则代表已重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        } else {
            //若进入else，则代表当前用户不是会员，需要自动完成注册并进行预约
            member = new Member();
            member.setName(map.get("name"));
            member.setSex(map.get("sex"));
            member.setIdCard(map.get("idCard"));
            member.setPhoneNumber(telephone);
            member.setRegTime(date);
            //完成会员注册
            memberDao.add(member);
        }

        //4.执行预约
        Order order = new Order();
        order.setMemberId(member.getId());//会员id
        order.setOrderDate(date);//预约日期
        order.setSetmealId(Integer.parseInt(map.get("setmealId")));//套餐id
        order.setOrderType(Order.ORDERTYPE_WEIXIN);//预约方式
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
        //添加数据到预约表中
        orderDao.add(order);

        //5.更新当日的已预约人数
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        //6.返回执行结果
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    /**
     * 根据预约记录id查询对应的预约信息
     * @param id 预约记录id
     * @return 预约信息
     */
    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if (map != null) {
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
