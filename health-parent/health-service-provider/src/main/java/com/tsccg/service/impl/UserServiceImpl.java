package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tsccg.dao.PermissionDao;
import com.tsccg.dao.RoleDao;
import com.tsccg.dao.UserDao;
import com.tsccg.pojo.Permission;
import com.tsccg.pojo.Role;
import com.tsccg.pojo.User;
import com.tsccg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

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

    /**
     * 根据用户名查询数据库获取用户基本信息和关联的角色信息，同时需要查询角色对应的权限信息
     * @param username 输入的用户名
     * @return 用户信息：1.密码 2.角色 3.权限
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
        Set<Role> roles = roleDao.findByUserId(userId);
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
            }
            //将角色信息填入当前用户实体类属性中
            user.setRoles(roles);
        }
        //4.返回用户信息
        return user;
    }
}
