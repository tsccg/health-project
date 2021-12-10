package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Permission;
import com.tsccg.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: TSCCG
 * @Date: 2021/12/10 15:39
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Reference
    private PermissionService permissionService;

    /**
     * 添加权限项
     * @param permission 权限信息
     * @return 执行结果
     * hasAuthority('PERMISSION_ADD');
     */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")//权限校验
    @PreAuthorize("hasAuthority('PERMISSION_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission) {
        try {
            return permissionService.add(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL);
        }
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PreAuthorize("hasAuthority('PERMISSION_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = null;
        try {
            pageResult = permissionService.findPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageResult;
    }

    /**
     * 切换权限启动状态
     * @param id
     * @param enable
     * @return
     */
    @PreAuthorize("hasAuthority('PERMISSION_EDIT')")
    @RequestMapping("/switchEnable")
    public Result switchEnable(Integer id,Integer enable) {
        try {
            permissionService.updateById(new Permission(id,enable));
            if (enable == 0) {
                return new Result(true,MessageConstant.PERMISSION_ENABLE_HAS_CLOSE);
            } else if (enable == 1){
                return new Result(true,MessageConstant.PERMISSION_ENABLE_HAS_OPEN);
            } else {
                return new Result(false,MessageConstant.EDIT_PERMISSION_ENABLE_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_PERMISSION_ENABLE_FAIL);
        }
    }
    /**
     * 根据id删除权限项
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            return permissionService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_PERMISSION_FAIL);
        }
    }

    /**
     * 根据id查询权限项
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('PERMISSION_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Permission permission = permissionService.findById(id);
            return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }

    /**
     * 编辑权限项
     * @param permission
     * @return
     */
    @PreAuthorize("hasAuthority('PERMISSION_EDIT')")
    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission) {
        try {
            permissionService.updateById(permission);
            return new Result(true,MessageConstant.EDIT_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().equals(MessageConstant.PERMISSION_KEYWORD_NON_REPEATABLE)) {
                return new Result(false,MessageConstant.PERMISSION_KEYWORD_NON_REPEATABLE);
            }
            return new Result(false,MessageConstant.EDIT_PERMISSION_FAIL);
        }
    }
}
