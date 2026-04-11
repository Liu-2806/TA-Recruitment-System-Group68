package com.bupt.ta.service;

import com.bupt.ta.dto.ApplicationQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.model.ApplicationStatus;

import java.util.Map;

/**
 * 申请服务接口。
 */
public interface ApplicationService {

    /**
     * 检查 TA 是否具备申请资格（资料完整性、截止时间、重复申请）。
     *
     * @param taUserId TA 用户 ID
     * @param jobId    岗位 ID
     * @return 资格检查结果
     */
    Map<String, Object> checkEligibility(String taUserId, String jobId);

    /**
     * 创建申请。
     *
     * @param taUserId   TA 用户 ID
     * @param jobId      岗位 ID
     * @param statement  申请说明
     * @return 申请详情
     */
    Map<String, Object> createApplication(String taUserId, String jobId, String statement);

    /**
     * 查询 TA 自己的申请列表。
     *
     * @param taUserId TA 用户 ID
     * @param query    查询条件
     * @return 分页申请结果
     */
    PageResult<Map<String, Object>> listApplicationsByTA(String taUserId, ApplicationQuery query);

    /**
     * 查询某岗位的申请列表（MO 侧）。
     *
     * @param jobId  岗位 ID
     * @param query  查询条件
     * @return 分页申请结果
     */
    PageResult<Map<String, Object>> listApplicationsByJob(String jobId, ApplicationQuery query);

    /**
     * 获取申请详情（MO 侧）。
     *
     * @param applicationId 申请 ID
     * @param moUserId      MO 用户 ID
     * @return 申请详情
     */
    Map<String, Object> getApplicationDetailForMO(String applicationId, String moUserId);

    Map<String, Object> withdrawApplicationByTA(String applicationId, String taUserId, String reason);

    /**
     * 更新申请状态（MO 侧录取/拒绝）。
     *
     * @param applicationId 申请 ID
     * @param moUserId      MO 用户 ID
     * @param newStatus     新状态
     * @param comment       备注
     */
    void updateStatusByMO(String applicationId, String moUserId, ApplicationStatus newStatus, String comment);
}
