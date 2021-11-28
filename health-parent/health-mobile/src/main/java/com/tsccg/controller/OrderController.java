package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.constant.RedisConstant;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Order;
import com.tsccg.service.OrderService;
import com.tsccg.utils.SMSTXUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/11/23 19:08
 * 体检预约处理
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    /**
     * 体检预约
     * @param map 用户输入数据
     * @return 执行结果
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map<String,String> map) {
    //1.校验手机验证码
        //获取用户输入的手机号
        String telephone = map.get("telephone");
        //1.1从redis中获取保存的验证码   key：15712345678-001
        String validateCodeInRedis = jedisPool.getResource().get(telephone + "-" + RedisConstant.SENDTYPE_ORDER);
        //1.2获取用户输入的验证码
        String validateCode = map.get("validateCode");
        //1.3将用户输入的验证码与保存的验证码进行比对
        if (validateCodeInRedis == null || !validateCodeInRedis.equals(validateCode)) {
            //如果比对失败，则返回结果给页面
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
        //如果比对成功，则调用服务完成预约业务处理
    //2.调用体检预约服务
        Result result = null;
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        } catch (Exception e) {
            //调用服务失败，预约失败
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_FAIL);
        }
    //3.预约成功，发送短信通知
        if (result.isFlag()) {
            try {
                String[] params = {map.get("name"),map.get("orderDate"),"南阳中心医院"};
                SMSTXUtils.sendShortMessage(SMSTXUtils.ORDER_SUCCESS_NOTICE,telephone,params);
                System.out.println("发送预约成功短信通知");
            } catch (Exception e) {
                e.printStackTrace();
                //重发3次
            }
        }
        return result;
    }

    /**
     * 根据预约记录id查询对应的预约信息
     * @param id 预约记录id
     * @return 预约信息
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        /*
            {
                member:体检人姓名,
                setmeal:套餐名称,
                orderDate:预约日期,
                orderType:预约类型
            }
         */
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
