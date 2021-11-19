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
        if(checkItemIds != null && checkItemIds.length > 0) {
            setConnection(checkItemIds,checkGroupId);
        }
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
     * 删除检查组以及对应关联表的关联数据
     * @param id 待删除检查组id
     */
    @Override
    public void deleteById(Integer id) {
        //先删除检查项对应的关联数据，再删除检查项数据
        this.deleteConnectionById(id);
        checkGroupDao.deleteCheckGroupById(id);
    }

    /**
     * 通过id删除检查组id对应的关联数据
     * @param id 待删除的检查组记录id
     */
    public void deleteConnectionById(Integer id) {
        checkGroupDao.deleteConnectionById(id);
    }
     /**
     * 根据id查询检查组数据
     * @param id 检查组id
     * @return 返回检查组数据
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据检查组id查询其对应的所有检查项id
     * @param id 检查组id
     * @return 查询结果：存有所有检查项id的List集合
     */
    @Override
    public List<Integer> findCheckItemIdsById(Integer id) {
        return checkGroupDao.findCheckItemIdsById(id);
    }
    /**
     * 编辑检查组，同时需要更新和检查项的关联关系
     * @param checkGroup 编辑后的检查组信息
     * @param checkItemIds 检查组对应检查项id
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        //1.根据检查组id删除中间表数据（清理原有关联关系）
        this.deleteConnectionById(checkGroup.getId());
        //2.更新检查组数据
        checkGroupDao.edit(checkGroup);
        //3.重新建立检查组和检查项关联关系
        this.setConnection(checkItemIds,checkGroup.getId());
    }
    /**
     * 查询所有检查组
     * @return 返回装有所有检查组的List集合
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
