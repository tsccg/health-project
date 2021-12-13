package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
import com.sun.org.apache.bcel.internal.generic.IFNULL;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Menu;
import com.tsccg.pojo.Role;
import com.tsccg.pojo.User;
import com.tsccg.service.MenuService;
import com.tsccg.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    /*@RequestMapping("/findMenuListByUserName")
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
    }*/
    @RequestMapping("/findMenuListByUserName")
    public Result findMenuListByUserName(@RequestBody Map<String,String> map) {
        String username = map.get("username");
//        System.out.println(username);
        try {
            LinkedHashSet<Menu> menuList = menuService.findMenuListByUserName(username);
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
            parentMenuList.add(0,new Menu("无上级菜单"));
            return new Result(true,MessageConstant.QUERY_MENU_LIST_SUCCESS,parentMenuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_MENU_LIST_FAIL);
        }
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Menu menu) {
//        System.out.println("上级菜单id：" + menu.getParentMenuId());
        try {
            return menuService.add(menu);
        } catch (Exception e) {
            return new Result(false,MessageConstant.ADD_MENU_FAIL);
        }
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = null;
        try {
            pageResult = menuService.findPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageResult;
    }

    /**
     * 根据菜单id删除菜单
     * @param id 菜单id
     * @return 执行结果
     */
    @PreAuthorize("hasAuthority('MENU_DELETE')")
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            menuService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_MENU_FAIL);
        }
    }

    /**
     * 根据菜单id查询菜单数据
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Menu menu = menuService.findById(id);
            return new Result(true,MessageConstant.QUERY_MENU_SUCCESS,menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_MENU_FAIL);
        }
    }

    /**
     * 编辑菜单
     * @param menu
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_EDIT')")
    @RequestMapping("/edit")
    public Result edit(@RequestBody Menu menu) {
        try {
            return menuService.edit(menu);
        } catch (Exception e) {
            return new Result(false,MessageConstant.EDIT_MENU_FAIL);
        }
    }

}
