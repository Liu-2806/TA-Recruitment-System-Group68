package com.bupt.ta.service;

import com.bupt.ta.dto.PageResult;

import java.util.Map;

/**
 * 统计分析服务接口。
 */
public interface AnalyticsService {

    /**
     * 获取管理员仪表盘概览数据。
     *
     * @return 概览数据
     */
    Map<String, Object> getSystemOverview();

    /**
     * 获取 TA 工作量报表。
     *
     * @param query 查询参数
     * @return 分页报表
     */
    PageResult<Map<String, Object>> getTAWorkloadReport(Map<String, Object> query);
}
