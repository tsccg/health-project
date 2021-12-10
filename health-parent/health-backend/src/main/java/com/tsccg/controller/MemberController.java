package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.CheckItem;
import com.tsccg.pojo.Member;
import com.tsccg.service.MemberService;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.service.MemberService;
import com.tsccg.utils.DateUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author tsccg
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Reference
    private MemberService memberService;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PreAuthorize("hasAuthority('MEMBER_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=null;
        try{
            pageResult= memberService.findPage(queryPageBean);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pageResult;
    }

    /**
     * 添加会员
     * @param member 会员信息
     * @return 执行结果
     */
    @PreAuthorize("hasAuthority('MEMBER_ADD')")//权限校验
    @RequestMapping("/add")
    public Result add(@RequestBody Member member) {//返回的表单数据是json格式，使用@RequestBody解析
        try {
            //完善参数
            member.setRegTime(DateUtils.getToday());//添加注册日期
            //服务调用
            return memberService.add(member);
        } catch (Exception e) {
            //服务调用失败
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MEMBER_FAIL);
        }
//        return new Result(true,MessageConstant.ADD_MEMBER_SUCCESS);
    }

    /**
     * 根据id删除会员
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('MEMBER_DELETE')")
    @RequestMapping("/delete")
//    @PreAuthorize("hasAuthority('MEMBER_DELETE')")
    public Result delete(Integer id) {
        try{
            memberService.deleteById(id);
        } catch(Exception e) {
            return new Result(false,MessageConstant.DELETE_MEMBER_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_MEMBER_SUCCESS);
    }

    /**
     * 根据id查询会员信息，适用于：数据回显
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('MEMBER_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Member member = memberService.findById(id);
            return new Result(true,MessageConstant.QUERY_MEMBER_SUCCESS,member);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_MEMBER_FAIL);
        }
    }

    /**
     * 根据会员id编辑会员信息
     * @param member 待更新会员信息
     * @return 执行结果
     */
    @PreAuthorize("hasAuthority('MEMBER_EDIT')")
    @RequestMapping("/edit")
    public Result edit(@RequestBody Member member) {
        try {
//            System.out.println("会员id："+member.getId());
            memberService.edit(member);
            return new Result(true,MessageConstant.EDIT_MEMBER_SUCCESS,member);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_MEMBER_FAIL);
        }
    }
}
