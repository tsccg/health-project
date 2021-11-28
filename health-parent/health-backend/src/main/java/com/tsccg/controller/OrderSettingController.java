package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.Result;
import com.tsccg.pojo.OrderSetting;
import com.tsccg.service.OrderSettingService;
import com.tsccg.utils.POIUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/11/20 16:25
 * 预约设置
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 文件上传，实现预约设置数据批量导入
     * @param excelFile 预约设置文件
     * @return 执行结果
     */
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    @RequestMapping("upload")
    public Result upload(@RequestParam MultipartFile excelFile) {
        try {
            //使用POI解析表格数据
            List<String[]> strings = POIUtils.readExcel(excelFile);
            //将解析数据进行整理，将字符串数组集合转换为OrderSetting对象集合
            List<OrderSetting> orderSettingList = new ArrayList<>();
            for (String[] string : strings) {
                //将日期和可预约人数封装到为OrderSetting对象
                OrderSetting orderSetting =
                        new OrderSetting(new Date(string[0]),Integer.parseInt(string[1]));
                //放入List集合
                orderSettingList.add(orderSetting);
            }
            //通过dubbo远程调用服务实现将批量数据导入数据库
            orderSettingService.add(orderSettingList);
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据月份查询对应的预约设置数据
     * @param date 查询条件：2021-11 yyyy-MM
     * @return 预约设置信息
     */
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {
        try {
            /*
            页面需要数据格式：
                [
                    { date: 1, number: 120, reservations: 100 },
                    { date: 3, number: 120, reservations: 1 },
                ]
            OrderSetting属性不一致，不宜使用
            可以使用Map集合，比较灵活
            */
            List<Map<String, Integer>> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据日期编辑对应的可预约人数
     * @param orderSetting 日期；可预约人数
     * @return 执行结果
     */
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
