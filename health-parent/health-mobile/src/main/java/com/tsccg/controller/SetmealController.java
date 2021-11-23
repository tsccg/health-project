package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.constant.RedisConstant;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Setmeal;
import com.tsccg.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: TSCCG
 * @Date: 2021/11/21 16:54
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    /**
     * 获取所有套餐信息
     * @return 所有套餐信息
     */
    @RequestMapping("/getAllSetmeal")
    public Result getAllSetmeal() {
        try {
            List<Setmeal> list = setmealService.getAllSetmeal();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    /**
     * 根据套餐id查询套餐详细信息：
     *      1.套餐基本信息
     *      2.套餐对应的所有检查组信息
     *      3.每个检查组所对应的检查项信息
     * @param id 套餐id
     * @return 套餐详细信息
     */
    @RequestMapping("/findDetailedMessageById")
    public Result findDetailedMessageById(Integer id) {
        try {
            Setmeal setmeal = setmealService.findDetailedMessageById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
