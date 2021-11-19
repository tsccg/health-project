package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.CheckItem;
import com.tsccg.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Author: TSCCG
 * @Date: 2021/11/05 22:44
 * 检查项管理
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    /**
     * 远程注入
     */
    @Reference
    private CheckItemService checkItemService;

    /**
     * 添加记录
     * @param checkItem 当前行的检查项数据
     * @return 返回执行结果：true/false message
     */
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

    /**
     * 分页查询
     * @param queryPageBean 分页查询条件：currentPage pageSize queryString
     * @return 分页结果：total rows
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return checkItemService.pageQuery(queryPageBean);
    }

    /**
     * 删除记录
     * @param id 待删除检查项的id
     * @return true/false message
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try{
            checkItemService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }

    /**
     * 根据id查询记录
     * @param id 目标id
     * @return 检查项数据
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try{
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 编辑记录
     * @param checkItem 所编辑的记录
     * @return true/false message
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem) {
        try{
            checkItemService.edit(checkItem);
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    /**
     * 查询所有数据
     * @return 返回装有所有数据的List集合
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        try{
            List<CheckItem> list = checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
