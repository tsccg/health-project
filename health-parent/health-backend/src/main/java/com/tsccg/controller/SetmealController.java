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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
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
            assert originalFilename != null;
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
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds) {
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
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.findPage(queryPageBean);
    }

    /**
     * 删除套餐，同时还要将套餐对应图片从redis数据库集合中删除
     * @param id 套餐id
     * @param img 套餐对应图片名称
     * @return 执行结果
     */
    @PreAuthorize("hasAuthority('SETMEAL_DELETE')")
    @RequestMapping("/delete")
    public Result delete(Integer id,String img) {
//        System.out.println(id);
//        System.out.println(img);
        try {
            setmealService.deleteById(id,img);
            return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }

    /**
     * 根据id查询套餐信息
     * @param id 套餐id
     * @return 套餐信息
     */
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 根据套餐id查询对应检查组
     * @param id 套餐id
     * @return 查询结果：1.true/false 2.提示信息 3.存有所有检查组id的List集合
     */
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @RequestMapping("/findCheckGroupIds")
    public Result findCheckGroupIds(Integer id) {
        try {
            List<Integer> checkGroupIds = setmealService.findCheckGroupIds(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupIds);
        } catch(Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 编辑套餐
     * 同时要更新redis集合数据，1.将旧图片名从DB集合中删除 2.新图片名存入DB集合
     * @param setmeal 套餐信息
     * @param checkgroupIds 套餐对应检查组id
     * @return 执行结果
     */
    @PreAuthorize("hasAuthority('SETMEAL_Edit')")
    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkgroupIds,String oldImg) {
//        System.out.println(setmeal.toString());
//        System.out.println(Arrays.toString(checkgroupIds));
//        System.out.println("old:" + oldImg);
//        System.out.println("new:" + setmeal.getImg());
        try {
            setmealService.update(setmeal,checkgroupIds,oldImg);
            return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }

    }
    /**
     * 获取所有套餐列表
     * @return
     */
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<Setmeal> setmealList = setmealService.getAllSetmeal();
            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmealList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }
}
