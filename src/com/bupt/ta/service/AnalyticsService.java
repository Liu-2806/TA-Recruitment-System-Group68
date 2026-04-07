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
     * 获取 MO 仪表盘概览数据。
     *
     * @param moUserId MO 用户 ID
     * @return 概览数据
     */
    Map<String, Object> getMODashboardOverview(String moUserId);

    /**
     * 获取 TA 工作量报表。
     *
     * @param query 查询参数
     * @return 分页报表
     */
    PageResult<Map<String, Object>> getTAWorkloadReport(Map<String, Object> query);

    /**
     * 获取 TA 工作量详情（详情弹窗）。
     *
     * @param taId TA 用户 ID
     * @return 详情对象
     */
    Map<String, Object> getTAWorkloadDetail(String taId);

    /**
     * 获取 TA 工作量分布总览。
     *
     * @param query 查询参数
     * @return 分布总览
     */
    Map<String, Object> getTAWorkloadDistributionSummary(Map<String, Object> query);
}
