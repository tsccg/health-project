package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tsccg.dao.CheckGroupDao;
import com.tsccg.pojo.CheckGroup;
import com.tsccg.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
}
