package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Menu;
import com.tsccg.pojo.Role;
import com.tsccg.pojo.User;
import com.tsccg.service.MenuService;
import com.tsccg.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author: TSCCG
 * @Date: 2021/12/08 18:50
 * 管理菜单
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Reference
    private MenuService menuService;
    @Reference
    private UserService userService;

    /**
     * 根据当前用户名获取对应的菜单列表
     * @param username 当前用户名
     * @return 菜单列表，json数组
     *  [
     *     {
     *         "path": "1",
     *         "title": "工作台",
     *         "icon":"fa-dashboard",
     *         "children": []
     *     },
     *     {
     *         "path": "2",
     *         "title": "会员管理",
     *         "icon":"fa-user-md",
     *         "children": [
     *             {
     *                 "path": "/2-1",
     *                 "title": "会员档案",
     *                 "linkUrl":"member.html",
     *             },
     *             {
     *                 "path": "/2-2",
     *                 "title": "体检上传",
     *                 "linkUrl":""
     *             }
     *         ]
     *     }
     * ]
     */
    @RequestMapping("/findMenuListByUserName")
    public Result findMenuListByUserName(String username) {
        try {
            List<Menu> menuList = new ArrayList<>();
            //根据用户名查询用户信息：用户对应角色、角色对应权限、角色对应菜单
            User user = userService.findByName(username);
            //获取并遍历关联的角色
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                //将角色关联的菜单都添加到menuList集合中
                LinkedHashSet<Menu> menus = role.getMenus();
                menuList.addAll(menus);
            }
            return new Result(true, MessageConstant.QUERY_MENU_LIST_SUCCESS,menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_MENU_LIST_FAIL);
        }
    }

    /**
     * 获取所有顶级菜单
     * @return
     */
    @RequestMapping("/findAllParentMenu")
    public Result findAllParentMenu() {
        try {
            List<Menu> parentMenuList = menuService.findAllParentMenu();
            return new Result(true,MessageConstant.QUERY_MENU_LIST_SUCCESS,parentMenuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_MENU_LIST_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Menu menu) {
//        System.out.println("上级菜单id：" + menu.getParentMenuId());
        try {
            return menuService.add(menu);
        } catch (Exception e) {
            return new Result(false,MessageConstant.ADD_MENU_FAIL);
        }
    }
}
