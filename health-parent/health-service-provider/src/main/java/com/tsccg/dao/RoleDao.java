package com.tsccg.dao;

import com.tsccg.pojo.Role;

import java.util.Set;

/**
 * @Author: TSCCG
 * @Date: 2021/11/28 21:56
 */
public interface RoleDao {
    /**
     * 根据用户Id查询关联的角色
     * @param userId 用户Id
     * @return 关联的角色，封装到Set集合中
     */
    Set<Role> findByUserId(Integer userId);
}
