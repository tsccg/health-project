package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tsccg.dao.CheckItemDao;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.pojo.CheckItem;
import com.tsccg.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: TSCCG
 * @Date: 2021/11/05 23:03
 */
@Service(interfaceClass = CheckItemService.class)//使用dubbo提供的@Service
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;
    //添加记录
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }
    //分页查询
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //使用MyBatis 分页助手插件
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> checkItems = checkItemDao.selectByCondition(queryString);
        long total = checkItems.getTotal();
        List<CheckItem> rows = checkItems.getResult();
        return new PageResult(total,rows);
    }
    //删除记录
    @Override
    public void deleteById(Integer id) {
        //不能直接删除，需要检查当前检查项是否与检查组关联，如果关联则不能删除
        long count = checkItemDao.findCountCheckItemById(id);
        if(count > 0) {
            throw new RuntimeException("当前检查项被引用，不能删除");
        }
        checkItemDao.deleteById(id);
    }
    //根据id查询检查项
    @Override
    public CheckItem findById(Integer id) {
        CheckItem checkItem = checkItemDao.findById(id);
        return checkItem;
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }
}
