package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.User;
import com.tsccg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: TSCCG
 * @Date: 2021/11/29 00:25
 * 用户操作
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;//自动注入密码加密对象
    /**
     * 获取当前登录用户的名字
     * @return 当前用户名
     */
    @RequestMapping("/getUsername")
    public Result getUsername() {
        try {
            //当Spring Security认证完毕后，会将当前用户信息保存到框架提供的上下文对象中
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }
    }

    /**
     * 添加用户
     * @param roleIds 用户关联的角色id
     * @return 执行结果
     */
    @PreAuthorize("hasAuthority('USER_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody User user, Integer[] roleIds) {
        try {
            //对密码进行加密
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.add(user,roleIds);
            return new Result(true,MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            if (e.getMessage().equals(MessageConstant.USER_HAVE_EXISTS)) {
                return new Result(false,MessageConstant.USER_HAVE_EXISTS);
            }
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_USER_FAIL);
        }
    }
    /**
     * 分页查询
     */
    @PreAuthorize("hasAuthority('USER_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = null;
        try {
            pageResult =  userService.findPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageResult;
    }

    /**
     * 切换状态
     */
    @PreAuthorize("hasAuthority('USER_EDIT')")
    @RequestMapping("/switchStation")
    public Result switchStation(@RequestBody User user) {
        try {
            userService.edit(user,null);
            if ("1".equals(user.getStation())) {
                return new Result(true,MessageConstant.HAS_OPEN);
            } else {
                return new Result(true,MessageConstant.HAS_CLOSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SWITCH_FAIL);
        }
    }

    /**
     * 根据id查询用户基本信息
     */
    @PreAuthorize("hasAuthority('USER_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            User user = userService.findById(id);
            return new Result(true,MessageConstant.QUERY_USER_SUCCESS,user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_USER_FAIL);
        }
    }

    /**
     * 根据id查询所关联的角色id列表
     */
    @PreAuthorize("hasAuthority('USER_QUERY')")
    @RequestMapping("/findRoleIds")
    public Result findRoleIds(Integer id) {
        try {
            List<Integer> roleIds = userService.findRoleIds(id);
            return new Result(true,MessageConstant.QUERY_USER_SUCCESS,roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_USER_FAIL);
        }
    }
    /**
     * 编辑用户
     * @param roleIds 用户关联的角色id
     * @return 执行结果
     */
    @PreAuthorize("hasAuthority('USER_EDIT')")
    @RequestMapping("/edit")
    public Result edit(@RequestBody User user, Integer[] roleIds) {
        try {
            //对密码进行加密
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.edit(user,roleIds);
            return new Result(true,MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            if (e.getMessage().equals(MessageConstant.USER_HAVE_EXISTS)) {
                return new Result(false,MessageConstant.USER_HAVE_EXISTS);
            }
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_USER_FAIL);
        }
    }
    /**
     * 删除用户
     * @param id 用户id
     * @return 执行结果
     */
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id) {
        try {
            userService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_USER_FAIL);
        }
    }

}
