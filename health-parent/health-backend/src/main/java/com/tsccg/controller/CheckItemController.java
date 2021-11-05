package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.Result;
import com.tsccg.pojo.CheckItem;
import com.tsccg.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: TSCCG
 * @Date: 2021/11/05 22:44
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    //远程注入
    @Reference
    private CheckItemService checkItemService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {//返回的表单数据是json格式，使用@RequestBody解析
        try {
            //服务调用成功
            checkItemService.add(checkItem);
        } catch (Exception e) {
            //服务调用失败
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }
}
