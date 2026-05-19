package com.bupt.ta.repository.file;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NotificationDataRepository extends JsonFileRepositorySupport {
    private static final String FILE_PATH = "notifications/notifications.json";

    public List<Map<String, Object>> findAll() {
        return new ArrayList<>(readList(FILE_PATH, Collections.emptyList()));
    }

    public List<Map<String, Object>> findByRecipient(String recipientId) {
        List<Map<String, Object>> results = new ArrayList<>();
        if (recipientId == null) {
            return results;
        }
        for (Map<String, Object> record : findAll()) {
            if (recipientId.equals(String.valueOf(record.get("recipientId")))) {
                results.add(new LinkedHashMap<>(record));
            }
        }
        return results;
    }

    public Map<String, Object> findById(String notificationId) {
        if (notificationId == null) {
            return null;
        }
        for (Map<String, Object> record : findAll()) {
            if (notificationId.equals(String.valueOf(record.get("notificationId")))) {
                return new LinkedHashMap<>(record);
            }
        }
        return null;
    }

    public void save(Map<String, Object> notificationRecord) {
        List<Map<String, Object>> all = findAll();
        boolean replaced = false;
        for (int i = 0; i < all.size(); i++) {
            if (String.valueOf(all.get(i).get("notificationId")).equals(String.valueOf(notificationRecord.get("notificationId")))) {
                all.set(i, new LinkedHashMap<>(notificationRecord));
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            all.add(new LinkedHashMap<>(notificationRecord));
        }
        writeList(FILE_PATH, all);
    }

    public void delete(String notificationId) {
        if (notificationId == null) {
            return;
        }
        List<Map<String, Object>> all = findAll();
        all.removeIf(record -> notificationId.equals(String.valueOf(record.get("notificationId"))));
        writeList(FILE_PATH, all);
    }

    public String nextNotificationId() {
        return "NTF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
