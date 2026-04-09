package com.bupt.ta.repository;

import com.bupt.ta.model.Role;

import java.util.List;
import java.util.Map;

/**
 * 用户数据仓储接口。
 */
public interface UserRepository {

    List<Map<String, Object>> findAllByRole(Role role);

    Map<String, Object> findById(Role role, String userId);

    Map<String, Object> findByLoginIdentifier(Role role, String identifier);

    boolean existsByField(Role role, String field, String value);

    boolean existsAcrossRoles(String field, String value);

    String nextUserId(Role role);

    Map<String, Object> save(Role role, Map<String, Object> userRecord);

    Map<String, Object> update(Role role, Map<String, Object> userRecord);

    long countByRole(Role role);
}
