package com.tsccg.dao;

import com.github.pagehelper.Page;
import com.tsccg.pojo.Member;

import java.util.List;

public interface MemberDao {
    /**
     * 查询所有会员
     */
    public List<Member> findAll();
    /**
     * 根据条件查询会员
     */
    public Page<Member> selectByCondition(String queryString);
    /**
     * 新增会员
     */
    public void add(Member member);
    /**
     * 根据id删除会员
     */
    public void deleteById(Integer id);
    /**
     * 根据id查询会员
     */
    public Member findById(Integer id);
    /**
     * 根据电话号码查询会员
     */
    public Member findByTelephone(String telephone);
    /**
     * 编辑会员信息
     */
    public void edit(Member member);
    /**
     * 根据日期统计会员数，统计指定日期之前的会员数
     */
    public Integer findMemberCountBeforeDate(String date);
    /**
     * 根据日期统计会员数
     */
    public Integer findMemberCountByDate(String date);
    /**
     * 根据日期统计会员数，统计指定日期之后的会员数
     */
    public Integer findMemberCountAfterDate(String date);
    /**
     * 查询总会员数
     */
    public Integer findMemberTotalCount();

}
