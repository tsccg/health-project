package com.tsccg.controller;

import com.tsccg.constant.MessageConstant;
import com.tsccg.constant.RedisConstant;
import com.tsccg.entity.Result;
import com.tsccg.utils.SMSTXUtils;
import com.tsccg.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @Author: TSCCG
 * @Date: 2021/11/23 16:59
 * 短信验证码
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 用户体检预约时发送短信验证码
     * @param telephone 用户电话号码
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //随机生成短信验证码
        String validateCode = ValidateCodeUtils.generateValidateCode(4).toString();
        try {
            //将验证码发送给用户
            String[] params = {validateCode,"5"};
//            SMSTXUtils.sendShortMessage(SMSTXUtils.COMMON_VALIDATE_CODE,telephone,params);
            System.out.println("给用户发送的验证码：" + validateCode);
        } catch (Exception e) {
            //短信验证码发送失败
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码存入redis中，时效5分钟   key：15712345678-001
        jedisPool.getResource().setex(
                telephone + "-" + RedisConstant.SENDTYPE_ORDER,
                300, validateCode);
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     * 用户快速登录时发送短信验证码
     * @param telephone 用户电话号码
     * @return
     */
    @RequestMapping("/send6Login")
    public Result send6Login(String telephone){
        //随机生成6位短信验证码
        String validateCode = ValidateCodeUtils.generateValidateCode(6).toString();
        try {
            //将验证码发送给用户
            String[] params = {validateCode,"5"};
//            SMSTXUtils.sendShortMessage(SMSTXUtils.LOGIN_VALIDATE_CODE,telephone,params);
            System.out.println("给用户发送的验证码：" + validateCode);
        } catch (Exception e) {
            //短信验证码发送失败
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码存入redis中，时效5分钟   key：15712345678-002
        jedisPool.getResource().setex(
                telephone + "-" + RedisConstant.SENDTYPE_LOGIN,
                300, validateCode);
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
