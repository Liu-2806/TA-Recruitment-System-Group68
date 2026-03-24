package com.bupt.ta.service;

import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.dto.PageResult;

import java.util.Map;

/**
 * 岗位服务接口。
 */
public interface JobService {

    /**
     * 查询开放岗位（TA 侧）。
     *
     * @param query 查询条件
     * @return 分页岗位结果
     */
    PageResult<Map<String, Object>> searchOpenJobs(JobQuery query);

    /**
     * 获取岗位详情。
     *
     * @param jobId 岗位 ID
     * @return 岗位详情
     */
    Map<String, Object> getJobById(String jobId);

    /**
     * 创建岗位（MO 侧）。
     *
     * @param moUserId MO 用户 ID
     * @param params   岗位参数
     * @return 岗位详情
     */
    Map<String, Object> createJob(String moUserId, Map<String, Object> params);

    /**
     * 查询 MO 自己发布的岗位。
     *
     * @param moUserId MO 用户 ID
     * @param query    查询条件
     * @return 分页岗位结果
     */
    PageResult<Map<String, Object>> listJobsByMO(String moUserId, JobQuery query);

    /**
     * 管理员查询全系统岗位。
     *
     * @param query 查询条件
     * @return 分页岗位结果
     */
    PageResult<Map<String, Object>> searchAllJobsForAdmin(JobQuery query);
}
