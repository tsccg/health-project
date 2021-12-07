package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tsccg.constant.MessageConstant;
import com.tsccg.dao.MemberDao;
import com.tsccg.dao.OrderDao;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
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
    @Autowired
    private OrderDao orderDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 会员注册
     * @param member 会员信息
     */
    @Override
    public Result add(Member member) {
        //根据电话号码判断是否重复注册
        String phoneNumber = member.getPhoneNumber();
        if (memberDao.findByTelephone(phoneNumber) != null) {
            return new Result(false, MessageConstant.HAS_ADD);
        }
        String password = member.getPassword();
        if (password != null) {
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
        return new Result(true,MessageConstant.ADD_MEMBER_SUCCESS);
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

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //取出分页参数
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //调用分页助手
        PageHelper.startPage(currentPage,pageSize);
        //查询数据库
        Page<Member> page = memberDao.selectByCondition(queryString);
        //返回查询结果数据
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id删除会员
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        //1.删除会员对应的预约数据
        orderDao.deleteOrderByMemberId(id);
        //2.删除会员数据
        memberDao.deleteById(id);
    }
    /**
     * 根据id查询会员信息
     * @param id 会员id
     * @return
     */
    @Override
    public Member findById(Integer id) {
        return memberDao.findById(id);
    }

    /**
     * 根据会员id编辑会员信息
     * @param member 待更新会员信息
     */
    @Override
    public void edit(Member member) {
        String password = member.getPassword();
        if (password != null) {
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.edit(member);
    }

}
