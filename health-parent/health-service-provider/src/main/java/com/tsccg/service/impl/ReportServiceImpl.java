package com.tsccg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tsccg.dao.MemberDao;
import com.tsccg.dao.OrderDao;
import com.tsccg.pojo.Setmeal;
import com.tsccg.service.ReportService;
import com.tsccg.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/11/30 16:32
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService{
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 查询运营统计数据
     * @return
     * Map数据格式：
     *      1.reportDate->yyyy-MM-dd
     *      2.todayNewMember -> number
     *      3.totalMember -> number
     *      4.thisWeekNewMember -> number
     *      5.thisMonthNewMember -> number
     *      6.todayOrderNumber -> number
     *      7.todayVisitsNumber -> number
     *      8.thisWeekOrderNumber -> number
     *      9.thisWeekVisitsNumber -> number
     *      10.thisMonthOrderNumber -> number
     *      11.thisMonthVisitsNumber -> number
     *      12.hotSetmeals -> List<Map<String,Object>>
     *               [
     *                   {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
     *                   {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
     *               ]
     */
    @Override
    public Map<String, Object> getBusinessReport() throws Exception {
        Map<String,Object> reportData = new HashMap<>();
        //1.当前日期
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        reportData.put("reportDate",today);
        //2.今日新增会员数量
        reportData.put("todayNewMember",memberDao.findMemberCountByDate(today));
        //3.总会员数量
        reportData.put("totalMember",memberDao.findMemberTotalCount());
        //4.本周新增会员数量
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());//获取本周周一日期
        reportData.put("thisWeekNewMember",memberDao.findMemberCountAfterDate(thisWeekMonday));
        //5.本月新增会员数量
        String thisMonthFirstDay = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());//获取本月第一天的日期
        reportData.put("thisMonthNewMember",memberDao.findMemberCountAfterDate(thisMonthFirstDay));
        //===================================================
        //6.本日预约人数
        reportData.put("todayOrderNumber",orderDao.findOrderCountByDate(today));
        //7.本日到诊人数
        reportData.put("todayVisitsNumber",orderDao.findVisitsCountByDate(today));
        //8.本周预约人数
        reportData.put("thisWeekOrderNumber",orderDao.findOrderCountAfterDate(thisWeekMonday));
        //9.本周到诊人数
        reportData.put("thisWeekVisitsNumber",orderDao.findVisitsCountAfterDate(thisWeekMonday));
        //10.本月预约人数
        reportData.put("thisMonthOrderNumber",orderDao.findOrderCountAfterDate(thisMonthFirstDay));
        //11.本月到诊人数
        reportData.put("thisMonthVisitsNumber",orderDao.findVisitsCountAfterDate(thisMonthFirstDay));
        //12.热门套餐hotSetmeals
        List<Map<String,Object>> hotSetmeals = orderDao.findHotSetmeal();
        reportData.put("hotSetmeals",hotSetmeals);
        return reportData;
    }
}
