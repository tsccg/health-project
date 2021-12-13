package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tsccg.constant.MessageConstant;
import com.tsccg.dao.MenuDao;
import com.tsccg.dao.PermissionDao;
import com.tsccg.dao.RoleDao;
import com.tsccg.dao.UserDao;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.pojo.Menu;
import com.tsccg.pojo.Permission;
import com.tsccg.pojo.Role;
import com.tsccg.pojo.User;
import com.tsccg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: TSCCG
 * @Date: 2021/11/28 21:47
 * 用户服务接口实现类
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private MenuDao menuDao;

    /**
     * 根据用户名查询数据库获取用户基本信息和关联的角色信息，同时需要查询角色对应的权限信息
     * @param username 输入的用户名
     * @return 用户信息：1.密码 2.角色 3.权限 4.菜单
     */
    @Override
    public User findByName(String username) {
        //1.根据用户名查询用户基本信息(不包括关联的角色和权限)
        User user = userDao.findByName(username);
        if (user == null) {
            return null;
        }
        //2.根据上一步查出的用户id查询关联的角色
        Integer userId = user.getId();
        Set<Role> roles = roleDao.findRolesByUserId(userId);
        if (roles != null && roles.size() > 0) {
            //遍历角色集合
            for (Role role : roles) {
                //3.根据查出的角色id查询对应的权限
                Integer roleId = role.getId();
                Set<Permission> permissions = permissionDao.findByRoleId(roleId);
                if (permissions != null && permissions.size() > 0) {
                    //将权限信息填入当前角色实体类属性中
                    role.setPermissions(permissions);
                }
                /*//4.根据角色id查询关联的所有菜单
                LinkedHashSet<Menu> menus = menuDao.findAllMenuByRoleId(roleId);
                if (menus != null && menus.size() > 0) {
                    for (Menu parentMenu : menus) {
                        //根据当前一级菜单id和角色id查询对应的子菜单列表
                        Map<String,Integer> map = new HashMap<>();
                        map.put("parentMenuId",parentMenu.getId());
                        map.put("role_id",roleId);
                        List<Menu> childrenMenuList = menuDao.findChildrenMenuByParentIdAndRoleId(map);
                        if (childrenMenuList != null && childrenMenuList.size() > 0) {
                            //将子菜单列表填入当前顶级菜单实体类属性里
                            parentMenu.setChildren(childrenMenuList);
                        }
                    }
                    //将菜单信息填入当前角色实体类属性中
                    role.setMenus(menus);
                }*/
            }
            //将角色信息填入当前用户实体类属性中
            user.setRoles(roles);
        }
        //5.返回用户信息
        return user;
    }
    /**
     * 添加用户
     * @param roleIds 用户关联的角色id
     */
    @Override
    public void add(User user, Integer[] roleIds) {
        //1.检验用户名是否重复
        //根据用户名查询用户数量
        Integer count = userDao.findUserCountByUsername(user.getUsername());
        if (count > 0) {
            throw new RuntimeException(MessageConstant.USER_HAVE_EXISTS);
        }
        //2.添加用户基本信息
        userDao.add(user);
        //3.添加用户关联的角色
        if(roleIds != null) {
            for (Integer roleId : roleIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("user_id",user.getId());
                map.put("role_id",roleId);
                userDao.addRole(map);
            }
        }
    }
    /**
     * 分页查询
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //1.取出参数
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //2.调用分页助手
        PageHelper.startPage(currentPage,pageSize);
        //3.查询数据库
        Page<User> page = userDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id查询用户基本信息
     */
    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }
    /**
     * 根据id查询所关联的角色id列表
     */
    @Override
    public List<Integer> findRoleIds(Integer id) {
        return userDao.findRoleIds(id);
    }
    /**
     * 编辑用户
     * @param roleIds 用户关联的角色id
     * @return 执行结果
     */
    @Override
    public void edit(User user, Integer[] roleIds) {
        //1.更新用户信息
        userDao.edit(user);
        //2.检验用户名是否重复
        if (user.getUsername() != null && user.getUsername().length() > 0) {
            Integer count = userDao.findUserCountByUsername(user.getUsername());
            if (count > 1) {
                throw new RuntimeException(MessageConstant.USER_HAVE_EXISTS);
            }
        }
        if (roleIds != null) {
            //3.清除原有关联角色
            userDao.deleteRoleByUserId(user.getId());
            //4.建立新的关联角色
            for (Integer roleId : roleIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("user_id",user.getId());
                map.put("role_id",roleId);
                userDao.addRole(map);
            }
        }
    }
    /**
     * 删除用户
     * @param id 用户id
     */
    @Override
    public void deleteById(Integer id) {
        //1.删除用户关联的角色
        userDao.deleteRoleByUserId(id);
        //2.删除用户
        userDao.deleteById(id);
    }
}
