package com.tsccg.service;

import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Member;

import java.util.List;
import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/11/24 15:17
 */
public interface MemberService {
    /**
     * 根据电话号码查询会员信息
     * @param telephone 会员电话号码
     * @return 会员基本信息
     */
    Member findByTelephone(String telephone);

    /**
     * 会员注册
     * @param member 会员信息
     */
    Result add(Member member);

    /**
     * 根据月份查询对应的会员数量
     * @param months 月份列表，往前12个月份
     * @return 每个月份对应的会员数量
     */
    List<Integer> findMemberCountByMonths(List<String> months);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 根据id删除会员
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 根据id查询会员信息
     * @param id
     * @return
     */
    Member findById(Integer id);

    /**
     * 根据会员id编辑会员信息
     * @param member 待更新会员信息
     */
    void edit(Member member);
}
