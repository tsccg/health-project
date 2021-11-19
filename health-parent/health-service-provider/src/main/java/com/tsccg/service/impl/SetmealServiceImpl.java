package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tsccg.constant.RedisConstant;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.pojo.Setmeal;
import com.tsccg.service.SetmealService;
import com.tsccg.dao.SetmealDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: TSCCG
 * @Date: 2021/11/10 16:45
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;
    /**
     * 添加套餐 1.添加套餐信息 2.根据套餐id向关系表中添加对应检查组
     * @param setmeal 套餐信息
     * @param checkgroupIds 套餐对应检查组id数组
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //1.向套餐表t_setmeal中添加套餐信息
        setmealDao.add(setmeal);
        //2.根据套餐id向关系表t_setmeal_checkgroup中添加对应检查组
        Integer setmealId = setmeal.getId();
        if(checkgroupIds != null && checkgroupIds.length > 0) {
            this.setConnection(setmealId,checkgroupIds);
        }
        //3.将存入数据库中的图片名称存入redis数据库的另一个set集合
        jedisPool.getResource().sadd(
                RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }

    /**
     * 根据套餐id向关系表中添加关联检查组
     * @param setmealId 套餐id
     * @param checkgroupIds 检查组id数组
     */
    private void setConnection(Integer setmealId,Integer[] checkgroupIds) {
        if(setmealId != null && checkgroupIds != null) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setmeal_id",setmealId);
                map.put("checkgroup_id",checkgroupId);
                setmealDao.setConnection(map);
            }

        }
    }

    /**
     * 分页查询
     * @param queryPageBean 查询条件
     * @return 当前页数据
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //获取三个参数
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //使用PageHelper
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        //获取当前页记录以及总页数
        long total = page.getTotal();
        List<Setmeal> rows = page.getResult();
        return new PageResult(total,rows);
    }
}
