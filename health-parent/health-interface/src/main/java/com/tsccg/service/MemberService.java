package com.tsccg.service;

import com.tsccg.pojo.Member;

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
    void add(Member member);
}
