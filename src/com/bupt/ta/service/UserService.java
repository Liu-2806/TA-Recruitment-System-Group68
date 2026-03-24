package com.bupt.ta.service;

import com.bupt.ta.dto.PageResult;
import com.bupt.ta.model.User;

import java.util.Map;

/**
 * 用户管理服务接口。
 */
public interface UserService {

    /**
     * 注册 TA 账号。
     *
     * @param params 注册参数
     * @return 新建用户
     */
    User registerTA(Map<String, Object> params);

    /**
     * 创建 MO 账号（Admin 操作）。
     *
     * @param params 创建参数
     * @return 新建 MO 用户
     */
    User createMO(Map<String, Object> params);

    /**
     * 查询 MO 列表。
     *
     * @param query 查询参数
     * @return 分页结果
     */
    PageResult<User> searchMOs(Map<String, Object> query);

    /**
     * 根据用户 ID 获取 MO 详情。
     *
     * @param moUserId MO 用户 ID
     * @return 用户详情
     */
    User getMOById(String moUserId);

    /**
     * 管理员更新 MO 信息。
     *
     * @param params 更新参数
     */
    void updateMOByAdmin(Map<String, Object> params);

    /**
     * 管理员重置密码。
     *
     * @param userId      用户 ID
     * @param rawPassword 新明文密码
     */
    void resetPasswordByAdmin(String userId, String rawPassword);
}
