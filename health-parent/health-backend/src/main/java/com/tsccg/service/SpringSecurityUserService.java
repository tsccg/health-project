package com.tsccg.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.pojo.Permission;
import com.tsccg.pojo.Role;
import com.tsccg.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: TSCCG
 * @Date: 2021/11/28 21:12
 * 权限查询类
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;

    /**
     * 根据用户名查询用户信息
     * @param username 输入的用户名
     * @return 用户信息：1.密码 2.角色 3.权限
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询用户信息，返回一个我们自己写的User实体类对象
        User user = userService.findByName(username);
        if (user == null) {
            //用户名为空，阻止登录
            return null;
        }
        //2.为当前用户授权
        List<GrantedAuthority> list = new ArrayList<>();
        //获取当前用户对应的所有角色并遍历
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            //获取当前角色对应所有权限并遍历
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //授予权限
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        //3.将从数据库中查出的用户名、加密密码、用户角色、用户权限封装到Spring Security框架提供的User类中，
        //返回给Spring Security框架
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
    }
}
