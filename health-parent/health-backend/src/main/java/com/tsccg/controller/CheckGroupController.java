package com.tsccg.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
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
    /**
     * 分页查询
     * @param queryPageBean 分页查询条件： 1.当前页码 2.每页展示数据条数 3.查询条件
     * @return 返回分页结果：1.总记录条数 2.当前页的所有记录
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return checkGroupService.pageQuery(queryPageBean);
    }

    /**
     * 根据id删除检查组及对应关联表记录
     * @param id 待删除记录id
     * @return 删除结果
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try{
            checkGroupService.deleteById(id);
        } catch(Exception e) {
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
