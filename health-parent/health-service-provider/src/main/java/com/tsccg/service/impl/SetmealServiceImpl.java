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
        this.addImgToDB(setmeal.getImg());
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

    /**
     * 根据套餐id删除套餐信息
     *      1.将该套餐对应图片名从redis的数据库set集合中删除
     *      2.删除关联表中对应的检查组
     *      3.删除套餐表中的记录
     * @param id 套餐id
     */
    @Override
    public void deleteById(Integer id,String img) {
        //1.将该套餐对应图片名从redis的数据库set集合中删除
        this.deleteImgFormDB(img);
        //2.删除关联表中对应的检查组
        this.deleteConnection(id);
        //3.删除套餐表中的记录
        setmealDao.deleteById(id);
    }


    /**
     * 根据id查询套餐信息
     * @param id 套餐id
     * @return 套餐信息
     */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    /**
     * 根据套餐id查询对应检查组id
     * @param id 套餐id
     * @return 存有所有检查组id的List集合
     */
    @Override
    public List<Integer> findCheckGroupIds(Integer id) {
        return setmealDao.findCheckGroupIds(id);
    }

    /**
     * 更新套餐，同时需要更新和检查组的关联关系
     * @param setmeal 套餐信息
     * @param checkGroupIds 套餐对应检查组id
     */
    @Override
    public void update(Setmeal setmeal, Integer[] checkGroupIds,String oldImg) {
        //将旧图片从redis数据库集合中删除
        this.deleteImgFormDB(oldImg);
        //将新图片添加到redis数据库集合中
        this.addImgToDB(setmeal.getImg());

        //1.根据套餐id删除中间表数据（清理原有关联关系）
        Integer id = setmeal.getId();
        this.deleteConnection(id);
        //2.更新套餐基本信息
        setmealDao.update(setmeal);
        //3.向中间表添加数据(建立新的关联关系)
        if(checkGroupIds != null) {
            this.setConnection(id,checkGroupIds);
        }
    }
    /**
     * 从redis的DB集合中删除图片名
     * @param img 待删除图片名
     */
    private void deleteImgFormDB(String img) {
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
    }

    /**
     * 将图片名添加到redis的DB集合中
     * @param img 待添加图片名
     */
    private void addImgToDB(String img) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
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
     * 根据套餐id删除对应关联表数据
     * @param id 套餐id
     */
    private void deleteConnection(Integer id) {
        setmealDao.deleteConnection(id);
    }
}
