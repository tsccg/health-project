package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tsccg.dao.MemberDao;
import com.tsccg.pojo.Member;
import com.tsccg.service.MemberService;
import com.tsccg.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/11/24 15:32
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService{
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password != null) {
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }

    /**
     * 根据月份查询对应的会员数量
     * @param months 月份列表，往前12个月份
     * @return 每个月份对应的会员数量
     */
    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {
        List<Integer> memberCounts = new ArrayList<>();
        for (String month : months) {
            String date = month+".31";
            Integer memberCount = memberDao.findMemberCountBeforeDate(date);
            memberCounts.add(memberCount);
        }
        return memberCounts;
    }


}
