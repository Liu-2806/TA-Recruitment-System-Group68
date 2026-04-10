package com.bupt.ta.service;

import java.util.List;
import java.util.Map;

/**
 * 个人资料服务接口。
 */
public interface ProfileService {

    /**
     * 获取 TA 资料。
     *
     * @param taUserId TA 用户 ID
     * @return TA 资料
     */
    Map<String, Object> getTAProfile(String taUserId);

    /**
     * 更新 TA 资料。
     *
     * @param taUserId TA 用户 ID
     * @param params   更新参数
     */
    void updateTAProfile(String taUserId, Map<String, Object> params);

    /**
     * 获取 MO 资料。
     *
     * @param moUserId MO 用户 ID
     * @return MO 资料
     */
    Map<String, Object> getMOProfile(String moUserId);

    /**
     * 更新 MO 资料。
     *
     * @param moUserId MO 用户 ID
     * @param params   更新参数
     */
    void updateMOProfile(String moUserId, Map<String, Object> params);

    /**
     * 获取技能标签全集。
     *
     * @return 技能标签列表
     */
    List<String> listAllSkillTags();
}
