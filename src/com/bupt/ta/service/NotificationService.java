package com.bupt.ta.service;

import java.util.List;
import java.util.Map;

/**
 * 通知服务接口。
 */
public interface NotificationService {

    /**
     * 创建一条通知。
     *
     * @param notification 通知字段：recipientId, recipientRole, type, title, message,
     *                     relatedApplicationId, relatedPostingId 等
     * @return 已持久化的通知
     */
    Map<String, Object> create(Map<String, Object> notification);

    /**
     * 获取某用户的全部通知（按时间倒序）。
     */
    List<Map<String, Object>> listForUser(String userId);

    /**
     * 获取某用户未读 / 未处理通知的数量。
     */
    int unreadCount(String userId);

    /**
     * 用户手动确认（关闭）一条通知。会校验所有权。
     */
    void dismiss(String notificationId, String userId);

    /**
     * 标记某条通知的 actionStatus（用于撤销请求 APPROVED/REJECTED）。
     */
    void markActionResolved(String notificationId, String actionStatus);
}
