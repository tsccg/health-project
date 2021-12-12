package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tsccg.constant.MessageConstant;
import com.tsccg.dao.RoleDao;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Role;
import com.tsccg.service.RoleService;
import javafx.scene.media.MediaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/12/11 22:12
 */
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDao roleDao;

    /**
     * 添加角色
     */
    @Override
    public void add(Role role, Integer[] permissionIds, Integer[] menuIds) {
        //1.检验角色关键字是否重复
        Integer roleCount = roleDao.findRoleCountByKeyword(role.getKeyword());
        if (roleCount > 0) {
            throw new RuntimeException(MessageConstant.KEYWORD_CANNOT_BE_REPEATED);
        }
        //2.添加角色基本信息
        roleDao.add(role);
        Integer roleId = role.getId();//获取刚插入的记录id
        //3.添加角色关联的权限和菜单项
        setConnection(roleId,permissionIds,menuIds);
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
        Page<Role> page = roleDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id删除角色
     * @param id 角色id
     */
    @Override
    public void deleteById(Integer id) {
        //1.检验当前角色是否被用户引用
        Integer result = roleDao.findConnection(id);
        if (result > 0) {
            throw new RuntimeException(MessageConstant.HAVE_CONNECTION);
        }
        //2.清除角色关联数据
        //清除角色关联的所有菜单
        roleDao.deleteMenus(id);
        //清除角色关联的所有权限
        roleDao.deletePermissions(id);
        //3.删除角色
        roleDao.deleteById(id);
    }

    /**
     * 根据id查询角色基本信息
     * @param id
     * @return
     */
    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    /**
     * 根据id查询所关联的权限id列表
     */
    @Override
    public List<Integer> findPermissionIdsById(Integer id) {
        return roleDao.findPermissionIdsById(id);
    }
    /**
     * 根据id查询所关联的菜单id列表
     */
    @Override
    public List<Integer> findMenuIdsById(Integer id) {
        return roleDao.findMenuIdsById(id);
    }

    /**
     * 编辑角色
     * @param role
     * @param permissionIds
     * @param menuIds
     */
    @Override
    public void edit(Role role, Integer[] permissionIds, Integer[] menuIds) {
        //1.更新角色信息
        roleDao.edit(role);
        //2.检验关键字是否重复
        Integer roleCount = roleDao.findRoleCountByKeyword(role.getKeyword());
        if (roleCount > 1) {
            throw new RuntimeException(MessageConstant.KEYWORD_CANNOT_BE_REPEATED);
        }
        //3.清除角色原本关联的权限和菜单
        Integer roleId = role.getId();
        roleDao.deleteMenus(roleId);
        roleDao.deletePermissions(roleId);
        //4.重新为角色关联权限和菜单
        setConnection(roleId,permissionIds,menuIds);
    }

    /**
     * 为角色关联权限和菜单
     * @param roleId
     * @param permissionIds
     * @param menuIds
     */
    private void setConnection(Integer roleId, Integer[] permissionIds, Integer[] menuIds) {
        //3.添加角色关联的权限
        if (permissionIds != null) {
            for (Integer permissionId : permissionIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("role_id",roleId);
                map.put("permission_id",permissionId);
                roleDao.addPermission(map);
            }
        }
        //4.添加角色关联的菜单项
        if (menuIds != null) {
            for (Integer menusId : menuIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("role_id",roleId);
                map.put("menu_id",menusId);
                roleDao.addMenu(map);
            }
        }

    }
}
