package com.tsccg.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.CheckGroup;
import com.tsccg.pojo.CheckItem;
import com.tsccg.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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

    /**
     * 根据id查询检查组数据
     * @param id 检查组id
     * @return 返回查询结果及检查组数据
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
    /**
     * 根据检查组id查询其对应的所有检查项id
     * @param id 检查组id
     * @return 查询结果：1.true/false 2.提示信息 3.存有所有检查项id的List集合
     */
    @RequestMapping("/findCheckItemIds")
    public Result findCheckItemIds(Integer id) {
        try {
            List<Integer> checkItemIds = checkGroupService.findCheckItemIdsById(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
    /**
     * 编辑检查组
     * @param checkGroup 编辑后的检查组信息
     * @param checkItemIds 检查组对应检查项id
     * @return 编辑执行结果
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkItemIds) {
        try {
            checkGroupService.edit(checkGroup,checkItemIds);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }
    /**
     * 查询所有检查组
     * @return 返回装有所有检查组的List集合
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        try{
            List<CheckGroup> list = checkGroupService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
