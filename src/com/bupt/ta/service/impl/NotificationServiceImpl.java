package com.bupt.ta.service.impl;

import com.bupt.ta.repository.file.NotificationDataRepository;
import com.bupt.ta.service.NotificationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NotificationServiceImpl implements NotificationService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final NotificationDataRepository notificationDataRepository;

    public NotificationServiceImpl(NotificationDataRepository notificationDataRepository) {
        this.notificationDataRepository = notificationDataRepository;
    }

    @Override
    public Map<String, Object> create(Map<String, Object> notification) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification payload is required.");
        }
        String recipientId = String.valueOf(notification.getOrDefault("recipientId", "")).trim();
        if (recipientId.isEmpty()) {
            throw new IllegalArgumentException("Notification recipientId is required.");
        }
        Map<String, Object> record = new LinkedHashMap<>(notification);
        record.put("notificationId", notificationDataRepository.nextNotificationId());
        record.put("createdAt", LocalDateTime.now().format(FORMATTER));
        record.putIfAbsent("status", "UNREAD");
        record.putIfAbsent("actionStatus", "");
        record.putIfAbsent("requiresAction", Boolean.FALSE);
        notificationDataRepository.save(record);
        return record;
    }

    @Override
    public List<Map<String, Object>> listForUser(String userId) {
        List<Map<String, Object>> records = notificationDataRepository.findByRecipient(userId);
        records.sort(Comparator.comparing(
            (Map<String, Object> record) -> parseDateTime(record.get("createdAt")),
            Comparator.nullsLast(Comparator.reverseOrder())
        ));
        return records;
    }

    @Override
    public int unreadCount(String userId) {
        int count = 0;
        for (Map<String, Object> record : notificationDataRepository.findByRecipient(userId)) {
            String status = String.valueOf(record.getOrDefault("status", "UNREAD")).toUpperCase(Locale.ROOT);
            if (!"DISMISSED".equals(status)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void dismiss(String notificationId, String userId) {
        Map<String, Object> record = notificationDataRepository.findById(notificationId);
        if (record == null) {
            return;
        }
        if (userId != null && !userId.equals(String.valueOf(record.get("recipientId")))) {
            throw new IllegalStateException("You cannot dismiss another user's notification.");
        }
        notificationDataRepository.delete(notificationId);
    }

    @Override
    public void markActionResolved(String notificationId, String actionStatus) {
        Map<String, Object> record = notificationDataRepository.findById(notificationId);
        if (record == null) {
            return;
        }
        record.put("actionStatus", actionStatus == null ? "" : actionStatus.trim().toUpperCase(Locale.ROOT));
        record.put("requiresAction", Boolean.FALSE);
        record.put("status", "READ");
        notificationDataRepository.save(record);
    }

    private LocalDateTime parseDateTime(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(String.valueOf(value), FORMATTER);
        } catch (Exception ex) {
            return null;
        }
    }
}
