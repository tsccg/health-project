package com.tsccg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sun.tools.internal.xjc.addon.locator.SourceLocationAddOn;
import com.tsccg.constant.MessageConstant;
import com.tsccg.entity.Result;
import com.tsccg.service.MemberService;
import com.tsccg.service.ReportService;
import com.tsccg.service.SetmealService;
import com.tsccg.utils.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: TSCCG
 * @Date: 2021/11/29 17:21
 * 报表操作
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetmealService setmealService;
    @Reference
    private ReportService reportService;

    /**
     * 获取会员数据，供给会员统计折线图
     * @return 返回如下格式数据：
     * {
     *     "data":{
     *             "months":["2019.01","2019.02","2019.03","2019.04"],
     *             "memberCount":[3,4,8,10]
     *            },
     *     "flag":true,
     *     "message":"获取会员统计数据成功"
     * }
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() throws Exception {
        try {
            Map<String,Object> map = new HashMap<>();
            //1.获取months数据：当前时间往前的12个月
            List<String> months = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();//获取日历对象，默认为当前时间
            calendar.add(Calendar.MONTH,-12);//往前推12个月
            //循环12次
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH,1);//每循环一次，前进一个月
                Date date = calendar.getTime();//获取当前日历对象对应的日期
                //将日期转换为【2021.11】格式并添加到List集合中
                months.add(new SimpleDateFormat("yyyy.MM").format(date));
            }
            map.put("months",months);

            //2.memberCount数据：根据日期查询对应的会员数量
            List<Integer> memberCount = memberService.findMemberCountByMonths(months);
            map.put("memberCount",memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 查询套餐统计数据，供给套餐预约占比饼形图
    "data":{
              "setmealNames":["套餐1","套餐2","套餐3"],
              "setmealCount":[
                               {name:"套餐1",value:100},
                               {name:"套餐2",value:200},
                               {name:"套餐3",value:300},
                             ]
             },
      "flag":true,
      "message":"获取套餐统计数据成功"
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        try {
            //1.定义存放所有数据的Map
            Map<String,Object> map = new HashMap<>();
            //2.从数据库中查询套餐信息：[{name:"套餐1",value:100},{name:"套餐2",value:200}...]
            List<Map<String,Object>> list = setmealService.findSetmealCount();
            //添加setmealCount到map中
            map.put("setmealCount",list);

            //3.从2中查出的套餐信息中取出所有name,存入一个List集合
            List<String> setmealNames = new ArrayList<>();
            for (Map<String, Object> m : list) {
                String name = m.get("name").toString();
                setmealNames.add(name);
            }
            map.put("setmealNames",setmealNames);
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }

    }

    /**
     * 查询运营数据统计
     * @return
     * reportData:{
*                     reportDate:null,
*                     todayNewMember :0,
*                     totalMember :0,
*                     thisWeekNewMember :0,
*                     thisMonthNewMember :0,
*                     todayOrderNumber :0,
*                     todayVisitsNumber :0,
*                     thisWeekOrderNumber :0,
*                     thisWeekVisitsNumber :0,
*                     thisMonthOrderNumber :0,
*                     thisMonthVisitsNumber :0,
*                     hotSetmeal :[
*                         {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
*                         {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
*                     ]
*                 }
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        Map<String,Object> reportData = null;
        try {
            //模拟数据：
            // reportData.put("reportDate","2021.11.30");
//        reportData.put("todayNewMember",5);
//        reportData.put("totalMember",200);
//        reportData.put("thisWeekNewMember",20);
//        reportData.put("thisMonthNewMember",50);
//        reportData.put("todayOrderNumber",100);
//        reportData.put("todayVisitsNumber",80);
//        reportData.put("thisWeekOrderNumber",200);
//        reportData.put("thisWeekVisitsNumber",170);
//        reportData.put("thisMonthOrderNumber",500);
//        reportData.put("thisMonthVisitsNumber",400);
//        List<Map<String,Object>> hotSetmeal = new ArrayList<>();
//        Map<String,Object> map1 = new HashMap<>();
//        map1.put("name","阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐");
//        map1.put("setmeal_count",200);
//        map1.put("proportion",0.5);//占百分比
//        hotSetmeal.add(map1);
//
//        Map<String,Object> map2 = new HashMap<>();
//        map2.put("name","阳光爸妈升级肿瘤12项筛查体检套餐");
//        map2.put("setmeal_count",100);
//        map2.put("proportion",0.2);//占百分比
//        hotSetmeal.add(map2);
//
//        reportData.put("hotSetmeal",hotSetmeal);
            reportData = reportService.getBusinessReport();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,reportData);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletOutputStream out = null;
        XSSFWorkbook excel = null;
        try {
            //远程调用报表服务获取报表数据
            Map<String, Object> businessReport = reportService.getBusinessReport();
            //取出所有数据
            String reportDate = (String) businessReport.get("reportDate");
            Integer todayNewMember = (Integer) businessReport.get("todayNewMember");
            Integer totalMember = (Integer) businessReport.get("totalMember");
            Integer thisWeekNewMember = (Integer) businessReport.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) businessReport.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) businessReport.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) businessReport.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) businessReport.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) businessReport.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) businessReport.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) businessReport.get("thisMonthVisitsNumber");
            List<Map<String,Object>> hotSetmeals = (List<Map<String,Object>>) businessReport.get("hotSetmeals");

            //获取Excel模板文件在服务端的绝对路径
            String templateRealPath = request.getSession().getServletContext()
                    .getRealPath("template") + File.separator + "report_template.xlsx";
            //读取模板文件创建Excel表格对象
            excel = new XSSFWorkbook(new FileInputStream(new File(templateRealPath)));
            //取出第一张表
            XSSFSheet sheet = excel.getSheetAt(0);
            //写入本日日期
            XSSFRow row = sheet.getRow(2);//获取第3行
            XSSFCell cell = row.getCell(5);//获取第6个单元格
            cell.setCellValue(reportDate);//将日期写入单元格
            //写入本日新增会员
            sheet.getRow(4).getCell(5).setCellValue(todayNewMember);
            //写入总会员数
            sheet.getRow(4).getCell(7).setCellValue(totalMember);
            //写入本周新增会员
            sheet.getRow(5).getCell(5).setCellValue(thisWeekNewMember);
            //写入本月新增会员
            sheet.getRow(5).getCell(7).setCellValue(thisMonthNewMember);

            //写入本日预约人数
            sheet.getRow(7).getCell(5).setCellValue(todayOrderNumber);
            //写入本日到诊人数
            sheet.getRow(7).getCell(7).setCellValue(todayVisitsNumber);
            //写入本周预约人数
            sheet.getRow(8).getCell(5).setCellValue(thisWeekOrderNumber);
            //写入本周到诊人数
            sheet.getRow(8).getCell(7).setCellValue(thisWeekVisitsNumber);
            //写入本月预约人数
            sheet.getRow(9).getCell(5).setCellValue(thisMonthOrderNumber);
            //写入本月到诊人数
            sheet.getRow(9).getCell(7).setCellValue(thisMonthVisitsNumber);

            //写入热门套餐
            int rowNum = 12;
            for (Map<String, Object> hotSetmeal : hotSetmeals) {
                String name = (String)hotSetmeal.get("name");//套餐名
                Long setmeal_count = (Long)hotSetmeal.get("setmeal_count");//套餐预约数量
                BigDecimal proportion = (BigDecimal)hotSetmeal.get("proportion");//套餐预约占比
                String attention = (String)hotSetmeal.get("attention");//备注
                XSSFRow row1 = sheet.getRow(rowNum++);
                //写入单元格
                row1.getCell(4).setCellValue(name);
                row1.getCell(5).setCellValue(setmeal_count);
                row1.getCell(6).setCellValue(proportion.doubleValue());
                row1.getCell(7).setCellValue(attention);
            }
            //通过输出流进行文件下载,基于浏览器作为客户端进行下载
            out = response.getOutputStream();
            //设置格式
            //"application/vnd.ms-excel"：代表Excel文件类型
            response.setContentType("application/vnd.ms-excel");
            //指定以附件形式进行下载
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            excel.write(out);
            out.flush();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        } finally {
            //关闭资源
            if (out != null) {
                out.close();
            }
            if (excel != null) {
                excel.close();
            }
        }

    }

}
