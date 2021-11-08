package com.tsccg.service;

import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.pojo.CheckGroup;

/**
 * @Author: TSCCG
 * @Date: 2021/11/08 17:44
 * 检查组服务接口
 */
public interface CheckGroupService {
    /**
     * 添加检查项
     * @param checkItemIds 请求组中所有检查项的id
     * @param checkGroup 检查组基本数据
     */
    void add(Integer[] checkItemIds,CheckGroup checkGroup);

    /**
     * 分页查询
     * @param queryPageBean 分页查询条件： 1.当前页码 2.每页展示数据条数 3.查询条件
     * @return 返回分页结果：1.总记录条数 2.当前页的所有记录
     */
    PageResult pageQuery(QueryPageBean queryPageBean);

    /**
     * 删除检查项
     * @param id 待删除记录id
     */
    void deleteById(Integer id);
}
