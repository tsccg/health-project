package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tsccg.constant.MessageConstant;
import com.tsccg.constant.RedisConstant;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Member;
import com.tsccg.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/11/24 14:57
 * 会员
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;
    /**
     * 会员验证码登录
     * @param map 会员登录参数 {telephone:手机号,validateCode:验证码}
     * @return 执行结果
     */
    @RequestMapping("/login")
    public Result login(HttpServletResponse response,@RequestBody Map<String, String> map) {
        //1.验证用户输入的验证码是否正确
        //获取用户输入的手机号
        String telephone = map.get("telephone");
        //获取用户输入的验证码
        String validateCode = map.get("validateCode");
        //从redis中获取保存的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + "-" + RedisConstant.SENDTYPE_LOGIN);
        //进行校验
        if (validateCodeInRedis == null || !validateCodeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //2.若验证码正确，判断用户是否为会员，如果不是会员则自动完成会员注册
        Member member = memberService.findByTelephone(telephone);
        if (member == null) {
            //自动完成会员注册
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        //3.向客户端写入Cookie，内容为用户手机号
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        //cookie路径
        cookie.setPath("/");
        //设置cookie在浏览器有30天有效期
        cookie.setMaxAge(60*60*24*30);
        response.addCookie(cookie);
        //4.将会员信息保存到redis中，使用手机号作为key，保存时长为30分钟
        String json = JSON.toJSON(member).toString();
        jedisPool.getResource().setex(telephone,60*30,json);
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
