package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Order;
import com.tsccg.service.OrderService;
import com.tsccg.utils.SMSTXUtils;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author: TSCCG
 * @Date: 2021/12/04 00:21
 * 后台管理预约信息
 */
@RestController
@RequestMapping("/localOrder")
public class LocalOrderController {
    @Reference
    private OrderService orderService;

    /**
     * 通过后台进行预约
     * @param map 预约信息
     * @param setmealIds 当前预约对应的所有套餐id
     * @return 执行结果
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map<String,String> map,Integer[] setmealIds) {
//        System.out.println("map" + map);
//        System.out.println("setmealIds" + Arrays.toString(setmealIds));
        System.out.println("test2");
        Result result = null;
        try {
            /*
                1.完善预约信息
             */
            //修正预约日期
//            Date orderDate = DateUtils.parseString2Date(map.get("orderDate"));
//            Calendar calendar = new GregorianCalendar();
//            calendar.setTime(orderDate);
//            calendar.add(Calendar.DATE,1); //把日期往后增加一天,正数往后推,负数往前移动
//            orderDate=calendar.getTime(); //这个时间就是日期往后推一天的结果
//            map.put("orderDate",DateUtils.parseDate2String(orderDate));
            //添加预约类型
            map.put("orderType",Order.ORDERTYPE_TELEPHONE);
            /*
                2.调用预约服务
             */
            for (Integer setmealId : setmealIds) {
                map.put("setmealId", setmealId.toString());
                result = orderService.order(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
        /*
            3.发送预约成功通知短信
         */
        assert result != null;
        if (result.isFlag()) {
            try {
                String[] params = {map.get("name"),map.get("orderDate"),"南阳市中心医院"};
//                SMSTXUtils.sendShortMessage(SMSTXUtils.ORDER_SUCCESS_NOTICE,map.get("telephone"),params);
                System.out.println("已发送预约成功短信通知");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 分页查询预约信息列表
     * @param queryPageBean 1.当前页码、2.每页记录数、3.查询条件
     * @return 1.total:总记录数。2.rows:当前页结果
     */
    @RequestMapping("/findOrdersPage")
    public PageResult findOrdersPage(@RequestBody QueryPageBean queryPageBean) {
        return orderService.findOrdersPage(queryPageBean);
    }
    /**
     * 修改预约状态
     * @param map 1.预约id 2.要修改的预约状态：未到诊 ；已到诊
     */
    @RequestMapping("/orderStatusSwitch")
    public Result orderStatusSwitch(@RequestBody Map<String,String> map) {
//        System.out.println(map.get("id"));
//        System.out.println(map.get("orderStatus"));
        try {
            orderService.editOrderStatus(map);
            return new Result(true,MessageConstant.EDIT_ORDER_STATUS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_ORDER_STATUS_FAIL);
        }
    }

    /**
     * 取消预约：根据id删除预约信息
     * @param id 预约记录id
     * @return 执行结果
     */
    @RequestMapping("/deleteOrderById")
    public Result deleteOrderById(Integer id) {
        try {
            orderService.deleteOrderById(id);
            return new Result(true,MessageConstant.DELETE_ORDER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_ORDER_FAIL);
        }
    }

    /**
     * 根据id查询预约基本信息
     * @param id 预约记录id
     * @return 预约基本信息
     * 1.name 2.telephone 3.orderDate 4.birthday 5.sex 6.age 7.idCard
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map<String,Object> map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    /**
     * 查询当前预约所对应的套餐id
     * @param id 预约id
     * @return 套餐id
     */
    @RequestMapping("/findSetmealIds")
    public Result findSetmealIds(Integer id) {
        try {
            List<Integer> setmealIds = orderService.findSetmealIds(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmealIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 修改预约数据
     * @param map 修改后的预约数据
     * @param setmealIds 修改后的套餐id
     * @return 执行结果
     */
    @RequestMapping("/editOrder")
    public Result editOrder(@RequestBody Map<String,String> map,Integer[] setmealIds) {
        Result result = null;
        try {
            /*
                1.调用服务层更新预约数据
             */
            result = orderService.editOrder(map,setmealIds[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_ORDER_FAIL);
        }
        /*
            2.发送预约修改成功通知短信
         */
        assert result != null;
        if (result.isFlag()) {
            try {
                String[] params = {"预约修改成功，"+map.get("name"),map.get("orderDate"),"南阳市中心医院"};
//                SMSTXUtils.sendShortMessage(SMSTXUtils.ORDER_SUCCESS_NOTICE,map.get("telephone"),params);
                System.out.println("已发送预约修改成功短信通知");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
