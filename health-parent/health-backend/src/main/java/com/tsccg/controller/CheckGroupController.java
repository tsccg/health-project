package com.tsccg.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.Result;
import com.tsccg.pojo.CheckGroup;
import com.tsccg.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @Author: TSCCG
 * @Date: 2021/11/08 17:26
 * 检查组管理
 */
@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {
    /**
     * 远程注入
     */
    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 添加检查组
     * @param checkItemIds 请求组中所有检查项的id
     * @param checkGroup 检查组基本数据
     * @return 返回执行结果
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkItemIds){
        try {
            //====测试====
//            System.out.println(Arrays.toString(checkItemIds));
//            System.out.println(checkGroup);
            //====测试====
            checkGroupService.add(checkItemIds,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }
}
