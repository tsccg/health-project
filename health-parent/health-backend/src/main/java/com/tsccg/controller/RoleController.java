package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Role;
import com.tsccg.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: TSCCG
 * @Date: 2021/12/11 22:07
 * 角色管理
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Reference
    private RoleService roleService;

    /**
     * 添加角色
     * @param menuIds 关联的菜单id数组
     * @param permissionIds 关联的权限id数组
     * @param role 角色基本信息
     * @return 执行结果
     */
    @PreAuthorize("hasAuthority('ROLE_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Role role,Integer[] permissionIds,Integer[] menuIds) {
        /*System.out.println(Arrays.toString(permissionIds));
        System.out.println(Arrays.toString(menuIds));*/
        try {
            roleService.add(role,permissionIds,menuIds);
            return new Result(true,MessageConstant.ADD_ROLE_SUCCESS);
        } catch (Exception e) {
            if (e.getMessage().equals(MessageConstant.KEYWORD_CANNOT_BE_REPEATED)) {
                return new Result(false,MessageConstant.KEYWORD_CANNOT_BE_REPEATED);
            }
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_ROLE_FAIL);
        }
    }
    /**
     * 分页查询
     */
    @PreAuthorize("hasAuthority('ROLE_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = null;
        try {
            pageResult =  roleService.findPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageResult;
    }

    /**
     * 删除角色
     * @param id 角色id
     * @return 执行结果
     */
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            roleService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_ROLE_SUCCESS);
        } catch (Exception e) {
            if (e.getMessage().equals(MessageConstant.HAVE_CONNECTION)) {
                return new Result(false,MessageConstant.HAVE_CONNECTION);
            }
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_ROLE_FAIL);
        }
    }
    /**
     * 根据id查询角色基本信息
     */
    @PreAuthorize("hasAuthority('ROLE_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Role role = roleService.findById(id);
            return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ROLE_FAIL);
        }
    }

    /**
     * 根据id查询所关联的权限id列表
     */
    @PreAuthorize("hasAuthority('ROLE_QUERY')")
    @RequestMapping("/findPermissionIds")
    public Result findPermissionIds(Integer id) {
        try {
            List<Integer> permissionIds = roleService.findPermissionIdsById(id);
            return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,permissionIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ROLE_FAIL);
        }
    }
    /**
     * 根据id查询所关联的菜单id列表
     */
    @PreAuthorize("hasAuthority('ROLE_QUERY')")
    @RequestMapping("/findMenuIds")
    public Result findMenuIds(Integer id) {
        try {
            List<Integer> menuIds = roleService.findMenuIdsById(id);
            return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,menuIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ROLE_FAIL);
        }
    }
    /**
     * 编辑角色
     * @param menuIds 关联的菜单id数组
     * @param permissionIds 关联的权限id数组
     * @param role 角色基本信息
     * @return 执行结果
     */
    @PreAuthorize("hasAuthority('ROLE_EDIT')")
    @RequestMapping("/edit")
    public Result edit(@RequestBody Role role,Integer[] permissionIds,Integer[] menuIds) {
        /*System.out.println(Arrays.toString(permissionIds));
        System.out.println(Arrays.toString(menuIds));*/
        try {
            roleService.edit(role,permissionIds,menuIds);
            return new Result(true,MessageConstant.EDIT_ROLE_SUCCESS);
        } catch (Exception e) {
            if (e.getMessage().equals(MessageConstant.KEYWORD_CANNOT_BE_REPEATED)) {
                return new Result(false,MessageConstant.KEYWORD_CANNOT_BE_REPEATED);
            }
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_ROLE_FAIL);
        }
    }
}
