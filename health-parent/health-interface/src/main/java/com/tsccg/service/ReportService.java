package com.tsccg.service;

import java.util.Map;

/**
 * @Author: TSCCG
 * @Date: 2021/11/30 16:29
 */
public interface ReportService {
    /**
     * 查询运营数据统计
     * @return
     * Map数据格式：
     *      reportDate->yyyy-MM-dd
     *      todayNewMember -> number
     *      totalMember -> number
     *      thisWeekNewMember -> number
     *      thisMonthNewMember -> number
     *      todayOrderNumber -> number
     *      todayVisitsNumber -> number
     *      thisWeekOrderNumber -> number
     *      thisWeekVisitsNumber -> number
     *      thisMonthOrderNumber -> number
     *      thisMonthVisitsNumber -> number
     *      hotSetmeals -> List<Setmeal>
     */
    Map<String,Object> getBusinessReport() throws Exception;
}
