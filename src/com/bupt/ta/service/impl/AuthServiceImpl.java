package com.bupt.ta.service.impl;

import com.bupt.ta.exception.BusinessException;
import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;
import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.service.AuthService;
import com.bupt.ta.util.PasswordUtils;

import java.util.Map;

/**
 * 认证服务实现。
 */
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User authenticate(String username, String rawPassword, Role expectedRole) {
        if (expectedRole == null) {
            throw new BusinessException("请选择登录角色");
        }
        if (isBlank(username) || isBlank(rawPassword)) {
            throw new BusinessException("账号和密码不能为空");
        }

        Map<String, Object> userRecord = userRepository.findByLoginIdentifier(expectedRole, username);
        if (userRecord == null) {
            throw new BusinessException("用户名、密码或角色不正确");
        }

        if (!Boolean.TRUE.equals(userRecord.get("active"))) {
            throw new BusinessException("当前账号已被停用");
        }

        String storedPassword = stringValue(userRecord.get("password"));
        if (!isBlank(storedPassword)) {
            if (!storedPassword.equals(rawPassword)) {
                throw new BusinessException("用户名、密码或角色不正确");
            }
        } else {
            String salt = stringValue(userRecord.get("passwordSalt"));
            String passwordHash = stringValue(userRecord.get("passwordHash"));
            if (!PasswordUtils.matches(rawPassword, salt, passwordHash)) {
                throw new BusinessException("用户名、密码或角色不正确");
            }

            // 兼容旧数据：首次使用旧加密记录登录成功后，自动迁移为明文密码存储。
            userRecord.put("password", rawPassword);
            userRecord.remove("passwordSalt");
            userRecord.remove("passwordHash");
            userRepository.update(expectedRole, userRecord);
        }

        return toUser(userRecord);
    }

    private User toUser(Map<String, Object> userRecord) {
        User user = new User();
        user.setId(firstNonBlank(userRecord.get("id"), userRecord.get("taId"), userRecord.get("moId")));
        user.setUsername(stringValue(userRecord.get("username")));
        user.setDisplayName(firstNonBlank(userRecord.get("displayName"), userRecord.get("fullName")));
        user.setRole(Role.valueOf(stringValue(userRecord.get("role"))));
        return user;
    }

    private String firstNonBlank(Object... values) {
        if (values == null) {
            return null;
        }
        for (Object value : values) {
            String text = stringValue(value);
            if (!isBlank(text)) {
                return text;
            }
        }
        return null;
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value).trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
