package com.tsccg;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/11/22 21:15
 */
public class FreemarkTest {

//    @Test
//    public void test1() throws IOException, TemplateException {
//        //1.创建Configuration对象，直接new，参数为freemarker的版本号
//        Configuration configuration = new Configuration(Configuration.getVersion());
//        //2.设置模板所在目录
//        configuration.setDirectoryForTemplateLoading(
//                new File("D:\\Source\\ftl"));
//        //3.设置字符集
//        configuration.setDefaultEncoding("utf-8");
//        //4.加载模板文件，创建一个模板对象
//        Template template = configuration.getTemplate("helloFTL.ftl");
//        //5.准备要填入模板中的数据，通常用Map进行构造
//        Map map = new HashMap();
//        map.put("name","TSCCG");
//        map.put("message","Hello Freemarker!!");
//        map.put("success",false);
//        //6.准备输出流对象，用于输出静态文件
//        Writer out = new FileWriter("D:\\Source\\ftl\\helloFTL4.html");
//        //7.输出
//        template.process(map,out);
//        //8.关闭资源
//        out.close();
//    }

    @Test
    public void test2() throws IOException, TemplateException {
        //1.创建Configuration对象，直接new，参数为freemarker的版本号
        Configuration configuration = new Configuration(Configuration.getVersion());
        //2.设置模板所在目录
        configuration.setDirectoryForTemplateLoading(
                new File("D:\\Source\\ftl"));
        //3.设置字符集
        configuration.setDefaultEncoding("utf-8");
        //4.加载模板文件，创建一个模板对象
        Template template = configuration.getTemplate("helloFTL.ftl");
        //5.准备要填入模板中的数据，通常用Map进行构造
        Map map = new HashMap();
        map.put("name","TSCCG");
        map.put("message","Hello Freemarker!!");
        map.put("success",false);
//        list
        List<Map> foodList = new ArrayList();
        Map map1 = new HashMap();
        map1.put("name","苹果");
        map1.put("price","2元");

        Map map2 = new HashMap();
        map2.put("name","香蕉");
        map2.put("price","4元");

        Map map3 = new HashMap();
        map3.put("name","菠萝");
        map3.put("price","10元");
        foodList.add(map1);
        foodList.add(map2);
        foodList.add(map3);
        map.put("foodList",foodList);
        //6.准备输出流对象，用于输出静态文件
        Writer out = new FileWriter("D:\\Source\\ftl\\helloFTL5.html");
        //7.输出
        template.process(map,out);
        //8.关闭资源
        out.close();
    }
}
