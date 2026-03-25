package com.bupt.ta.service;

import java.io.InputStream;
import java.util.Map;

/**
 * 简历服务接口。
 */
public interface ResumeService {

    /**
     * 保存或替换 TA 简历。
     *
     * @param taUserId TA 用户 ID
     * @param fileName 文件名
     * @param content  文件流
     * @return 文件元数据
     */
    Map<String, Object> saveOrReplaceTAResume(String taUserId, String fileName, InputStream content);

    /**
     * 为 MO 打开某申请对应的简历下载信息。
     *
     * @param applicationId 申请 ID
     * @param moUserId      MO 用户 ID
     * @return 下载信息（文件名、流等）
     */
    Map<String, Object> openResumeStreamForMO(String applicationId, String moUserId);
}
