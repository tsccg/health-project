package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tsccg.dao.CheckItemDao;
import com.tsccg.pojo.CheckItem;
import com.tsccg.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: TSCCG
 * @Date: 2021/11/05 23:03
 */
@Service(interfaceClass = CheckItemService.class)//使用dubbo提供的@Service
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }
}
