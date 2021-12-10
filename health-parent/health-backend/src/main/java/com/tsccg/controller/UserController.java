package com.tsccg.controller;

import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: TSCCG
 * @Date: 2021/11/29 00:25
 * 用户操作
 */
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 获取当前登录用户的名字
     * @return 当前用户名
     */
    @RequestMapping("/getUsername")
    public Result getUsername() {
        try {
            //当Spring Security认证完毕后，会将当前用户信息保存到框架提供的上下文对象中
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
