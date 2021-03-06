package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tsccg.constant.RedisConstant;
import com.tsccg.entity.PageResult;
import com.tsccg.entity.QueryPageBean;
import com.tsccg.pojo.Setmeal;
import com.tsccg.service.SetmealService;
import com.tsccg.dao.SetmealDao;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: TSCCG
 * @Date: 2021/11/10 16:45
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    private String outPutPath;//从属性配置文件中获取静态文件生成路径

    /**
     * 添加套餐 1.添加套餐信息 2.根据套餐id向关系表中添加对应检查组
     * @param setmeal 套餐信息
     * @param checkgroupIds 套餐对应检查组id数组
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //1.向套餐表t_setmeal中添加套餐信息
        setmealDao.add(setmeal);
        //2.根据套餐id向关系表t_setmeal_checkgroup中添加对应检查组
        Integer setmealId = setmeal.getId();
        if(checkgroupIds != null && checkgroupIds.length > 0) {
            this.setConnection(setmealId,checkgroupIds);
        }
        //3.将存入数据库中的图片名称存入redis数据库的另一个set集合
        this.addImgToDB(setmeal.getImg());
        /*
            4.当添加套餐后需要生成静态页面
                生成套餐列表页面
                生成套餐详情页面(多个)
         */
        generateMobileStaticHtml();
    }

    private void generateMobileStaticHtml() {
        //1.准备要填入的数据
        List<Setmeal> setmealList = this.getAllSetmeal();
        //2.生成套餐列表页面
        generateMobileSetmealListHtml(setmealList);
        //3.生成套餐详情页面(多个)
        generateMobileSetmealDetailHtml(setmealList);
    }

    /**
     * 生成套餐列表页面
     * @param setmealList 当前所有套餐列表
     */
    private void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        //1.封装map
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("setmealList",setmealList);
        //2.调用通用生成类
        generateHtml("mobile_setmeal.ftl",
                "m_setmeal.html",dataMap);
    }

    /**
     * 生成套餐详情页面(多个)
     * @param setmealList 当前所有套餐列表
     */
    private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        //遍历套餐列表
        for (Setmeal setmeal : setmealList) {
            //封装map
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("setmeal",this.findDetailedMessageById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_"+setmeal.getId()+".html",
                    dataMap);
        }
    }

    /**
     * 通用地生成静态页面
     * @param templateName 模板名称
     * @param htmlPageName 要生成的静态页面名称
     * @param map 需要填入模板的数据
     */
    private void generateHtml(String templateName,String htmlPageName,Map<String,Object> map) {
        //获取配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            //获取模板对象
            Template template = configuration.getTemplate(templateName);
            //创建输出流
            out = new FileWriter(new File(outPutPath + "/" + htmlPageName));
            //填充数据并输出文件
            template.process(map,out);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 分页查询
     * @param queryPageBean 查询条件
     * @return 当前页数据
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //获取三个参数
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //使用PageHelper
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        //获取当前页记录以及总页数
        long total = page.getTotal();
        List<Setmeal> rows = page.getResult();
        return new PageResult(total,rows);
    }

    /**
     * 根据套餐id删除套餐信息
     *      1.将该套餐对应图片名从redis的数据库set集合中删除
     *      2.删除关联表中对应的检查组
     *      3.删除套餐表中的记录
     * @param id 套餐id
     */
    @Override
    public void deleteById(Integer id,String img) {
        //1.将该套餐对应图片名从redis的数据库set集合中删除
        this.deleteImgFromDB(img);
        //2.删除关联表中对应的检查组
        this.deleteConnection(id);
        //3.删除套餐表中的记录
        setmealDao.deleteById(id);
        /*
            4.当添加套餐后需要生成静态页面
                生成套餐列表页面
                生成套餐详情页面(多个)
         */
        generateMobileStaticHtml();
    }

    /**
     * 根据id查询套餐信息
     * @param id 套餐id
     * @return 套餐信息
     */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    /**
     * 根据套餐id查询对应检查组id
     * @param id 套餐id
     * @return 存有所有检查组id的List集合
     */
    @Override
    public List<Integer> findCheckGroupIds(Integer id) {
        return setmealDao.findCheckGroupIds(id);
    }

    /**
     * 更新套餐，同时需要更新和检查组的关联关系
     * @param setmeal 套餐信息
     * @param checkGroupIds 套餐对应检查组id
     */
    @Override
    public void update(Setmeal setmeal, Integer[] checkGroupIds,String oldImg) {
        //将旧图片从redis数据库集合中删除
        this.deleteImgFromDB(oldImg);
        //将新图片添加到redis数据库集合中
        this.addImgToDB(setmeal.getImg());

        //1.根据套餐id删除中间表数据（清理原有关联关系）
        Integer id = setmeal.getId();
        this.deleteConnection(id);
        //2.更新套餐基本信息
        setmealDao.update(setmeal);
        //3.向中间表添加数据(建立新的关联关系)
        if(checkGroupIds != null) {
            this.setConnection(id,checkGroupIds);
        }
        /*
            4.当添加套餐后需要生成静态页面
                生成套餐列表页面
                生成套餐详情页面(多个)
         */
        generateMobileStaticHtml();
    }

    /**
     * 获取所有套餐信息
     * @return 保存有所有套餐信息的List集合
     */
    @Override
    public List<Setmeal> getAllSetmeal() {
        return setmealDao.getAllSetmeal();
    }

    /**
     * 根据套餐id查询套餐详细信息：
     *      1.套餐基本信息
     *      2.套餐对应的所有检查组信息
     *      3.每个检查组所对应的检查项信息
     * @param id 套餐id
     * @return 套餐详细信息
     */
    @Override
    public Setmeal findDetailedMessageById(Integer id) {
        return setmealDao.findDetailedMessageById(id);
    }

    /**
     * 查询所有套餐预约数据
     * @return [{name:"套餐1",value:100},{name:"套餐2",value:200}...]
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    /**
     * 从redis的DB集合中删除图片名
     * @param img 待删除图片名
     */
    private void deleteImgFromDB(String img) {
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
    }

    /**
     * 将图片名添加到redis的DB集合中
     * @param img 待添加图片名
     */
    private void addImgToDB(String img) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
    }
    /**
     * 根据套餐id向关系表中添加关联检查组
     * @param setmealId 套餐id
     * @param checkgroupIds 检查组id数组
     */
    private void setConnection(Integer setmealId,Integer[] checkgroupIds) {
        if(setmealId != null && checkgroupIds != null) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setmeal_id",setmealId);
                map.put("checkgroup_id",checkgroupId);
                setmealDao.setConnection(map);
            }

        }
    }
    /**
     * 根据套餐id删除对应关联表数据
     * @param id 套餐id
     */
    private void deleteConnection(Integer id) {
        setmealDao.deleteConnection(id);
    }
}
