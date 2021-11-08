package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tsccg.dao.CheckGroupDao;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.pojo.CheckGroup;
import com.tsccg.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/11/08 17:50
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 添加检查组合，同时需要设置检查组合和检查项的关联关系
     * @param checkItemIds 请求组中所有检查项的id
     * @param checkGroup 检查组基本数据
     */
    @Override
    public void add(Integer[] checkItemIds,CheckGroup checkGroup) {
        checkGroupDao.add(checkGroup);
        //获取当前检查组记录插入后生成的自增id
        Integer checkGroupId = checkGroup.getId();
        setConnection(checkItemIds,checkGroupId);
    }
    /**
     * 设置检查组和检查项的关联关系,往检查组和检查项的关联表中添加数据
     * @param checkItemIds 请求组中所有检查项的id
     * @param checkGroupId 当前请求组记录的id
     */
    private void setConnection(Integer[] checkItemIds,Integer checkGroupId) {
        if(checkItemIds != null && checkItemIds.length > 0) {
            for(Integer checkItemId:checkItemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroup_id",checkGroupId);
                map.put("checkitem_id",checkItemId);
                checkGroupDao.setConnection(map);
            }
        }
    }
    /**
     * 分页查询
     * @param queryPageBean 分页查询条件： 1.当前页码 2.每页展示数据条数 3.查询条件
     * @return 返回分页结果：1.总记录条数 2.当前页的所有记录
     */
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //拿出三个分页查询条件
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //使用分页助手
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);
        //获取总记录条数
        long total = page.getTotal();
        //获取当前页的记录
        List<CheckGroup> rows = page.getResult();
        return new PageResult(total,rows);
    }
    /**
     * 删除检查项以及对应关联表的关联数据
     * @param id 待删除检查项id
     */
    @Override
    public void deleteById(Integer id) {
        //先删除检查项对应的关联数据，再删除检查项数据
        this.deleteConnectionById(id);
        checkGroupDao.deleteCheckGroupById(id);
    }

    /**
     * 通过id删除检查项id对应的关联数据
     * @param id 待删除的检查项记录id
     */
    public void deleteConnectionById(Integer id) {
        checkGroupDao.deleteConnectionById(id);
    }
}
