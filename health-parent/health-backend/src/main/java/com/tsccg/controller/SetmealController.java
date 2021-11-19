package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tsccg.constant.MessageConstant;
import com.tsccg.constant.RedisConstant;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.entity.Result;
import com.tsccg.pojo.Setmeal;
import com.tsccg.service.SetmealService;
import com.tsccg.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * @Author: TSCCG
 * @Date: 2021/11/10 15:41
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    //redis连接池
    @Autowired
    private JedisPool jedisPool;
    /**
     * 上传图片文件到七牛云
     * @param imgFile 待上传图片文件
     * @return 上传结果：true/false 提示信息 上传后文件名
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
//        System.out.println(imgFile.getOriginalFilename());
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            //截取后缀suffix .jpg
            int lastIndexOf = originalFilename.lastIndexOf(".");
            String suffix = originalFilename.substring(lastIndexOf);
            //随机生成一个文件名 xxx + .jpg
            String fileName = UUID.randomUUID() + suffix;
            //调用文件上传工具类
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //将上传成功的图片名称存入redis数据库的一个set集合
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加套餐
     * @param setmeal 套餐信息
     * @param checkgroupIds 套餐对应检查组id数组
     * @return 执行结果
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds) {
        System.out.println(Arrays.toString(checkgroupIds));
        try {
            setmealService.add(setmeal,checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页查询
     * @param queryPageBean 查询条件
     * @return 当前页数据
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.findPage(queryPageBean);
    }
}
