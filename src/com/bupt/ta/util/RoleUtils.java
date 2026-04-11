package com.bupt.ta.util;

import com.bupt.ta.exception.BusinessException;
import com.bupt.ta.model.Role;

import java.util.Locale;

/**
 * 角色解析工具。
 */
public final class RoleUtils {

    private RoleUtils() {
    }

    public static Role parseRole(String rawRole) {
        if (rawRole == null) {
            throw new BusinessException("请选择登录角色");
        }

        String normalized = rawRole.trim().toUpperCase(Locale.ENGLISH);
        if ("APPLICANT".equals(normalized)) {
            return Role.TA;
        }

        try {
            return Role.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("无效的角色类型");
        }
    }
}
