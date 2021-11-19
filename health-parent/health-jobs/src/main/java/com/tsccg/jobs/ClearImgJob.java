package com.tsccg.jobs;

import com.tsccg.constant.RedisConstant;
import com.tsccg.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @Author: TSCCG
 * @Date: 2021/11/19 16:21
 * 自定义job，实现每天定时清理垃圾图片
 */
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        //从连接池中获取Jedis对象
        Jedis jedis = jedisPool.getResource();
        //获取差值集合
        Set<String> sdiff = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,
                RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(sdiff != null) {
            for (String imgName : sdiff) {
                //删除七牛云服务器上的图片
                QiniuUtils.deleteFileFromQiniu(imgName);
                //从Redis数据库的set集合中删除图片名称
                jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES,imgName);
            }
        }
        //关闭连接
        jedis.close();
    }
}
