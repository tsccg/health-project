package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tsccg.constant.MessageConstant;
import com.tsccg.dao.PermissionDao;
import com.tsccg.dao.RoleDao;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Permission;
import com.tsccg.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/12/10 15:59
 */
@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RoleDao roleDao;

    /**
     * 添加权限
     * @param permission
     */
    @Override
    public Result add(Permission permission) {
        //检查关键字是否重复
        String keyword = permission.getKeyword();
        if (keyword != null) {
            Integer keywordCount = permissionDao.findKeyWordCount(keyword);
            if (keywordCount > 0) {
                return new Result(false,MessageConstant.PERMISSION_KEYWORD_NON_REPEATABLE);
            }
        }
        //2.添加权限到权限表
        permissionDao.add(permission);
        //3.添加权限到角色权限关联表中(添加到管理员角色中)
        Map<String,Integer> map = new HashMap<>();
        map.put("role_id",1);
        map.put("permission_id",permission.getId());
        roleDao.addPermission(map);
        return new Result(true,MessageConstant.ADD_PERMISSION_SUCCESS);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
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
        Page<Permission> page = permissionDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id删除权限项
     * @param id
     */
    @Override
    public Result deleteById(Integer id) {
        //不能直接删除，需要检查当前权限项是否与角色关联，如果关联则不能删除
        long count = permissionDao.findRoleCountByPermissionId(id);
        if(count > 0) {
            return new Result(false, MessageConstant.PERMISSION_HAVE_CONNECTION);
        }
        permissionDao.deleteById(id);
        return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
    }

    /**
     * 根据id查询权限项
     * @param id
     * @return
     */
    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }
    /**
     * 更新权限信息
     * @param permission
     */
    @Override
    public void updateById(Permission permission) {
        permissionDao.updateById(permission);
        //更新之后检查数据库中的关键字数量
        Integer keywordCount = permissionDao.findKeyWordCount(permission.getKeyword());
        if (keywordCount > 1) {
            throw new RuntimeException(MessageConstant.PERMISSION_KEYWORD_NON_REPEATABLE);
        }
    }
}
