package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.Result;
import com.tsccg.pojo.OrderSetting;
import com.tsccg.service.OrderSettingService;
import com.tsccg.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @RequestMapping("upload")
    public Result upload(@RequestParam MultipartFile excelFile) {
        try {
            //使用POI解析表格数据
            List<String[]> strings = POIUtils.readExcel(excelFile);
            //将解析数据进行整理，将字符串数组集合转换为OrderSetting对象集合
            List<OrderSetting> orderSettingList = new ArrayList<>();
            for (String[] string : strings) {
                //日期
                String date = string[0];
                //可预约人数
                String num = string[1];
                //封装为OrderSetting对象
                OrderSetting orderSetting =
                        new OrderSetting(new Date(date),Integer.parseInt(num));
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

}
